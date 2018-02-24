package org.jenkinsci.plugins.gwt;

import static com.google.common.base.Charsets.UTF_8;
import static hudson.util.HttpResponses.okJSON;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.StaplerRequest;

import com.google.common.annotations.VisibleForTesting;

import hudson.Extension;
import hudson.model.UnprotectedRootAction;
import hudson.security.csrf.CrumbExclusion;

@Extension
public class GenericWebHookRequestReceiver extends CrumbExclusion implements UnprotectedRootAction {

  private static final String NO_JOBS_MSG =
      "Did not find any jobs to trigger! "
          + "The user invoking /generic-webhook-trigger/invoke must have read permission to any jobs that should be triggered. "
          + "You may try adding http://user:passw@url... "
          + "or configuring and passing an authentication token like ...trigger/invoke?token=TOKENHERE";
  private static final String URL_NAME = "generic-webhook-trigger";
  private static final Logger LOGGER =
      Logger.getLogger(GenericWebHookRequestReceiver.class.getName());

  public HttpResponse doInvoke(final StaplerRequest request) {
    String postContent = null;
    Map<String, String[]> parameterMap = null;
    Map<String, List<String>> headers = null;
    try {
      headers = getHeaders(request);
      parameterMap = request.getParameterMap();
      postContent = IOUtils.toString(request.getInputStream(), UTF_8.name());
    } catch (final IOException e) {
      LOGGER.log(SEVERE, "", e);
    }

    final String givenToken = getGivenToken(headers, parameterMap);
    return doInvoke(headers, parameterMap, postContent, givenToken);
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
    return null;
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
    if (foundJobs.isEmpty()) {
      LOGGER.log(INFO, NO_JOBS_MSG);
      triggerResultsMap.put("ANY", NO_JOBS_MSG);
    }
    for (final FoundJob foundJob : foundJobs) {
      try {
        LOGGER.log(INFO, "Triggering " + foundJob.getFullName());
        LOGGER.log(FINE, " with:\n\n" + postContent + "\n\n");
        final GenericTriggerResults triggerResults =
            foundJob.getGenericTrigger().trigger(headers, parameterMap, postContent);
        triggerResultsMap.put(foundJob.getFullName(), triggerResults);
      } catch (final Throwable t) {
        LOGGER.log(SEVERE, foundJob.getFullName(), t);
        final String msg =
            "Exception occurred, full stack trace in Jenkins server log. Thrown in: "
                + t.getStackTrace()[0].getClassName()
                + ":"
                + t.getStackTrace()[0].getLineNumber();
        triggerResultsMap.put(foundJob.getFullName(), msg);
      }
    }
    final Map<String, Object> response = new HashMap<>();
    response.put("triggerResults", triggerResultsMap);
    return okJSON(response);
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
