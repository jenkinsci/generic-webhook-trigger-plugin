package org.jenkinsci.plugins.gwt.jobfinder;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.security.MessageDigest.isEqual;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import hudson.model.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jenkins.model.ParameterizedJobMixIn.ParameterizedJob;
import org.jenkinsci.plugins.gwt.FoundJob;
import org.jenkinsci.plugins.gwt.GenericTrigger;
import org.jenkinsci.plugins.plaincredentials.StringCredentials;

public final class JobFinder {

  private static Logger LOG = Logger.getLogger(JobFinder.class.getName());

  private JobFinder() {}

  private static JobFinderImpersonater jobFinderImpersonater = new JobFinderImpersonater();

  @VisibleForTesting
  static void setJobFinderImpersonater(final JobFinderImpersonater jobFinderImpersonater) {
    JobFinder.jobFinderImpersonater = jobFinderImpersonater;
  }

  public static List<FoundJob> findAllJobsWithTrigger(final String givenToken) {

    final List<FoundJob> found = new ArrayList<>();

    final boolean impersonate = !isNullOrEmpty(givenToken);
    final List<ParameterizedJob> candidateProjects =
        jobFinderImpersonater.getAllParameterizedJobs(impersonate);
    for (final ParameterizedJob candidateJob : candidateProjects) {
      final GenericTrigger genericTriggerOpt =
          GenericTriggerFinder.findGenericTrigger(candidateJob.getTriggers());
      if (genericTriggerOpt != null) {
        final String configuredToken =
            determineTokenValue(
                candidateJob,
                genericTriggerOpt.getToken(),
                genericTriggerOpt.getTokenCredentialId());
        final boolean authenticationTokenMatches =
            authenticationTokenMatches(givenToken, configuredToken);
        if (authenticationTokenMatches) {
          final FoundJob foundJob = new FoundJob(candidateJob.getFullName(), genericTriggerOpt);
          found.add(foundJob);
        }
      }
    }

    return found;
  }

  private static String determineTokenValue(
      final Item item, final String token, final String tokenCredentialsId) {
    if (isNullOrEmpty(tokenCredentialsId)) {
      LOG.log(Level.FINE, "Found no credential configured in " + item.getFullDisplayName());
      return token;
    }
    if (!isNullOrEmpty(tokenCredentialsId) && !isNullOrEmpty(token)) {
      LOG.log(
          Level.WARNING,
          "The job "
              + item.getFullDisplayName()
              + " is configured with both static token and token from credential "
              + tokenCredentialsId
              + ".");
    }
    final Optional<StringCredentials> credentialsOpt =
        org.jenkinsci.plugins.gwt.global.CredentialsHelper.findCredentials(
            tokenCredentialsId, item);
    if (credentialsOpt.isPresent()) {
      LOG.log(
          Level.FINE,
          "Found credential from "
              + tokenCredentialsId
              + " configured in "
              + item.getFullDisplayName());
      return credentialsOpt.get().getSecret().getPlainText();
    }
    LOG.log(
        Level.SEVERE,
        "Cannot find credential ("
            + tokenCredentialsId
            + ") configured in "
            + item.getFullDisplayName());
    return token;
  }

  private static boolean authenticationTokenMatches(
      final String givenToken, final String configuredToken) {
    final boolean noTokenGiven = isNullOrEmpty(givenToken);
    final boolean noTokenConfigured = isNullOrEmpty(configuredToken);
    return authenticationTokenMatchesGeneric(configuredToken, givenToken)
        || noTokenGiven && noTokenConfigured;
  }

  /** This is the token configured in this plugin. */
  private static boolean authenticationTokenMatchesGeneric(
      final String configuredToken, final String givenToken) {
    final boolean jobHasConfiguredToken = !isNullOrEmpty(configuredToken);
    final boolean tokenWasGiven = !isNullOrEmpty(givenToken);
    if (jobHasConfiguredToken && tokenWasGiven) {
      return isEqual(configuredToken.getBytes(UTF_8), givenToken.getBytes(UTF_8));
    }
    if (!jobHasConfiguredToken && !tokenWasGiven) {
      return true;
    }
    return false;
  }
}
