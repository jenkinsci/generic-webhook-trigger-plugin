package org.jenkinsci.plugins.gwt;

import static com.google.common.base.Charsets.UTF_8;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.SEVERE;
import static org.jenkinsci.plugins.gwt.GenericResponse.jsonResponse;
import static org.kohsuke.stapler.HttpResponses.ok;

import com.google.common.annotations.VisibleForTesting;
import hudson.Extension;
import hudson.model.UnprotectedRootAction;
import hudson.security.csrf.CrumbExclusion;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.jenkinsci.plugins.gwt.jobfinder.JobFinder;
import org.jenkinsci.plugins.gwt.whitelist.WhitelistException;
import org.jenkinsci.plugins.gwt.whitelist.WhitelistVerifier;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.StaplerRequest;

@Extension
public class GenericWebHookRequestReceiver extends CrumbExclusion implements UnprotectedRootAction {

  /** A value of -1 will make sure the quiet period of the job will be used. */
  private static final int RESPECT_JOBS_QUIET_PERIOD = -1;

  private static final String NO_JOBS_MSG =
      "Did not find any jobs with "
          + GenericTrigger.class.getSimpleName()
          + " configured! "
          + "If you are using a token, you need to pass it like ...trigger/invoke?token=TOKENHERE. "
          + "If you are not using a token, you need to authenticate like http://user:passsword@jenkins/generic-webhook... ";
  private static final String URL_NAME = "generic-webhook-trigger";
  private static final Logger LOGGER =
      Logger.getLogger(GenericWebHookRequestReceiver.class.getName());

  public HttpResponse doInvoke(final StaplerRequest request) {
    String postContent = null;
    Map<String, String[]> parameterMap = null;
    Map<String, List<String>> headers = null;
    try {
      headers = this.getHeaders(request);
      parameterMap = request.getParameterMap();
      postContent = IOUtils.toString(request.getInputStream(), UTF_8.name());
    } catch (final IOException e) {
      LOGGER.log(SEVERE, "", e);
      return jsonResponse(500, "Unable to read inputstream: " + e.getMessage());
    }

    try {
      WhitelistVerifier.verifyWhitelist(request.getRemoteAddr(), headers, postContent);
    } catch (final WhitelistException e) {
      return jsonResponse(
          403,
          "Sender, "
              + request.getRemoteHost()
              + ", with headers "
              + headers
              + " did not pass whitelist.\n"
              + e.getMessage());
    }

    final String givenToken = this.getGivenToken(headers, parameterMap);
    return this.doInvoke(headers, parameterMap, postContent, givenToken);
  }

  @VisibleForTesting
  String getGivenToken(
      final Map<String, List<String>> headers, final Map<String, String[]> parameterMap) {
    if (parameterMap.containsKey("token")) {
      return parameterMap.get("token")[0];
    }
    if (headers.containsKey("token")) {
      return headers.get("token").get(0);
    }
    if (headers.containsKey("authorization")) {
      for (final String candidateValue : headers.get("authorization")) {
        if (candidateValue.startsWith("Bearer ")) {
          return candidateValue.substring(7);
        }
      }
    }
    if (headers.containsKey("x-gitlab-token")) {
      return headers.get("x-gitlab-token").get(0);
    }
    return null;
  }

  @VisibleForTesting
  int getGivenQuietPeriod(
      final Map<String, List<String>> headers, final Map<String, String[]> parameterMap) {
    if (parameterMap.containsKey("jobQuietPeriod")) {
      try {
        return Integer.parseInt(parameterMap.get("jobQuietPeriod")[0]);
      } catch (final Exception e) {
        return RESPECT_JOBS_QUIET_PERIOD;
      }
    }
    if (headers.containsKey("jobQuietPeriod")) {
      try {
        return Integer.parseInt(headers.get("jobQuietPeriod").get(0));
      } catch (final Exception e) {
        return RESPECT_JOBS_QUIET_PERIOD;
      }
    }

    /** A value of -1 will make sure the quiet period of the job will be used. */
    return RESPECT_JOBS_QUIET_PERIOD;
  }

  @VisibleForTesting
  Map<String, List<String>> getHeaders(final StaplerRequest request) {
    final Map<String, List<String>> headers = new HashMap<>();
    final Enumeration<String> headersEnumeration = request.getHeaderNames();
    while (headersEnumeration.hasMoreElements()) {
      final String headerName = headersEnumeration.nextElement();
      headers.put(headerName.toLowerCase(), Collections.list(request.getHeaders(headerName)));
    }
    return headers;
  }

  @VisibleForTesting
  HttpResponse doInvoke(
      final Map<String, List<String>> headers,
      final Map<String, String[]> parameterMap,
      final String postContent,
      final String givenToken) {

    final List<FoundJob> foundJobs = JobFinder.findAllJobsWithTrigger(givenToken);
    final Map<String, Object> triggerResultsMap = new HashMap<>();
    boolean allSilent = true;
    boolean errors = false;
    for (final FoundJob foundJob : foundJobs) {
      try {
        LOGGER.log(FINE, "Triggering " + foundJob.getFullName());
        LOGGER.log(FINE, " with:\n\n" + postContent + "\n\n");
        final GenericTrigger genericTrigger = foundJob.getGenericTrigger();

        int quietPeriod = RESPECT_JOBS_QUIET_PERIOD;
        if (genericTrigger.getOverrideQuietPeriod()) {
          quietPeriod = this.getGivenQuietPeriod(headers, parameterMap);
        }

        final GenericTriggerResults triggerResults =
            genericTrigger.trigger(headers, parameterMap, postContent, quietPeriod);
        if (!genericTrigger.isSilentResponse()) {
          allSilent = false;
          triggerResultsMap.put(foundJob.getFullName(), triggerResults);
        }
      } catch (final Throwable t) {
        LOGGER.log(SEVERE, foundJob.getFullName(), t);
        final String msg = this.createMessageFromException(t);
        triggerResultsMap.put(foundJob.getFullName(), msg);
        errors = true;
      }
    }
    if (allSilent && foundJobs.size() > 0) {
      return ok();
    }
    if (errors) {
      return jsonResponse(500, "There were errors when triggering jobs.", triggerResultsMap);
    } else {
      if (foundJobs.isEmpty()) {
        LOGGER.log(Level.FINE, NO_JOBS_MSG);
        return jsonResponse(404, NO_JOBS_MSG);
      } else {
        return jsonResponse(200, "Triggered jobs.", triggerResultsMap);
      }
    }
  }

  String createMessageFromException(final Throwable t) {
    String stacktraceInfo = "";
    if (t.getStackTrace().length > 0) {
      stacktraceInfo =
          "Thrown in: "
              + t.getStackTrace()[0].getClassName()
              + ":"
              + t.getStackTrace()[0].getLineNumber();
    }
    final String msg =
        "Exception occurred ("
            + t.getClass()
            + ": "
            + t.getMessage()
            + "), full stack trace in Jenkins server log. "
            + stacktraceInfo;
    return msg;
  }

  @Override
  public String getIconFileName() {
    return null;
  }

  @Override
  public String getDisplayName() {
    return null;
  }

  @Override
  public String getUrlName() {
    return URL_NAME;
  }

  @Override
  public boolean process(
      final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
      throws IOException, ServletException {
    final String pathInfo = request.getPathInfo();
    if (pathInfo != null && pathInfo.startsWith("/" + URL_NAME + "/")) {
      chain.doFilter(request, response);
      return true;
    }
    return false;
  }
}
