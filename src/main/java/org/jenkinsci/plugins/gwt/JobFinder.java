package org.jenkinsci.plugins.gwt;

import static com.google.common.base.Strings.isNullOrEmpty;
import hudson.security.ACL;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jenkins.model.Jenkins;
import jenkins.model.ParameterizedJobMixIn.ParameterizedJob;

import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;

public final class JobFinder {

  private JobFinder() {}

  public static List<FoundJob> findAllJobsWithTrigger(String queryStringToken) {

    List<FoundJob> found = new ArrayList<>();

    List<ParameterizedJob> candidateProjects = getAllParameterizedJobs();
    for (ParameterizedJob candidateJob : candidateProjects) {
      GenericTrigger genericTriggerOpt = findGenericTrigger(candidateJob.getTriggers());
      if (genericTriggerOpt != null) {
        found.add(new FoundJob(candidateJob.getFullName(), genericTriggerOpt));
      }
    }

    candidateProjects = getAllParameterizedJobsByImpersonation();
    for (ParameterizedJob candidateJob : candidateProjects) {
      if (!isIncluded(candidateJob.getFullName(), found)
          && authenticationTokenMatches(candidateJob, queryStringToken)) {
        GenericTrigger genericTriggerOpt = findGenericTrigger(candidateJob.getTriggers());
        if (genericTriggerOpt != null) {
          found.add(new FoundJob(candidateJob.getFullName(), genericTriggerOpt));
        }
      }
    }

    return found;
  }

  private static boolean isIncluded(String searchFor, List<FoundJob> includedJobs) {
    for (FoundJob includedJob : includedJobs) {
      if (includedJob.getFullName().equals(searchFor)) {
        return true;
      }
    }
    return false;
  }

  private static List<ParameterizedJob> getAllParameterizedJobs() {
    List<ParameterizedJob> candidateProjects =
        Jenkins.getInstance().getAllItems(ParameterizedJob.class);
    return candidateProjects;
  }

  private static boolean authenticationTokenMatches(
      ParameterizedJob candidateJob, String queryStringToken) {
    hudson.model.BuildAuthorizationToken authToken = candidateJob.getAuthToken();

    boolean jobHasAuthToken = authToken != null && !isNullOrEmpty(authToken.getToken());
    if (jobHasAuthToken) {
      boolean authTokenMatchesQueryToken = authToken.getToken().equals(queryStringToken);
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
    SecurityContext orig = ACL.impersonate(ACL.SYSTEM);
    List<ParameterizedJob> jobs = getAllParameterizedJobs();
    // Return to previous authentication context
    SecurityContextHolder.setContext(orig);
    return jobs;
  }

  private static GenericTrigger findGenericTrigger(Map<TriggerDescriptor, Trigger<?>> triggers) {
    if (triggers == null) {
      return null;
    }
    for (Trigger<?> candidate : triggers.values()) {
      if (candidate instanceof GenericTrigger) {
        return (GenericTrigger) candidate;
      }
    }
    return null;
  }
}
