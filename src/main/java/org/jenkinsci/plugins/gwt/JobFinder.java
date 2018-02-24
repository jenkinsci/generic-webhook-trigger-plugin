package org.jenkinsci.plugins.gwt;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;

import hudson.security.ACL;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;
import jenkins.model.Jenkins;
import jenkins.model.ParameterizedJobMixIn.ParameterizedJob;

public final class JobFinder {

  private JobFinder() {}

  public static List<FoundJob> findAllJobsWithTrigger(final String givenToken) {

    final List<FoundJob> found = new ArrayList<>();

    List<ParameterizedJob> candidateProjects = getAllParameterizedJobs();
    for (final ParameterizedJob candidateJob : candidateProjects) {
      final GenericTrigger genericTriggerOpt = findGenericTrigger(candidateJob.getTriggers());
      if (genericTriggerOpt != null) {
        found.add(new FoundJob(candidateJob.getFullName(), genericTriggerOpt));
      }
    }

    candidateProjects = getAllParameterizedJobsByImpersonation();
    for (final ParameterizedJob candidateJob : candidateProjects) {
      if (!isIncluded(candidateJob.getFullName(), found)
          && authenticationTokenMatches(candidateJob, givenToken)) {
        final GenericTrigger genericTriggerOpt = findGenericTrigger(candidateJob.getTriggers());
        if (genericTriggerOpt != null) {
          found.add(new FoundJob(candidateJob.getFullName(), genericTriggerOpt));
        }
      }
    }

    return found;
  }

  private static boolean isIncluded(final String searchFor, final List<FoundJob> includedJobs) {
    for (final FoundJob includedJob : includedJobs) {
      if (includedJob.getFullName().equals(searchFor)) {
        return true;
      }
    }
    return false;
  }

  private static List<ParameterizedJob> getAllParameterizedJobs() {
    final List<ParameterizedJob> candidateProjects =
        Jenkins.getInstance().getAllItems(ParameterizedJob.class);
    return candidateProjects;
  }

  @SuppressWarnings("deprecation")
  private static boolean authenticationTokenMatches(
      final ParameterizedJob candidateJob, final String givenToken) {
    final hudson.model.BuildAuthorizationToken authToken = candidateJob.getAuthToken();

    final boolean jobHasAuthToken = authToken != null && !isNullOrEmpty(authToken.getToken());
    if (jobHasAuthToken && givenToken != null) {
      final boolean authTokenMatchesQueryToken = authToken.getToken().equals(givenToken);
      if (authTokenMatchesQueryToken) {
        return true;
      } else {
        return false;
      }
    }
    return false;
  }

  private static List<ParameterizedJob> getAllParameterizedJobsByImpersonation() {
    // Impersinate to get all jobs even without read grants
    final SecurityContext orig = ACL.impersonate(ACL.SYSTEM);
    final List<ParameterizedJob> jobs = getAllParameterizedJobs();
    // Return to previous authentication context
    SecurityContextHolder.setContext(orig);
    return jobs;
  }

  private static GenericTrigger findGenericTrigger(
      final Map<TriggerDescriptor, Trigger<?>> triggers) {
    if (triggers == null) {
      return null;
    }
    for (final Trigger<?> candidate : triggers.values()) {
      if (candidate instanceof GenericTrigger) {
        return (GenericTrigger) candidate;
      }
    }
    return null;
  }
}
