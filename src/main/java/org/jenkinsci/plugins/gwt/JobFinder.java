package org.jenkinsci.plugins.gwt;

import hudson.model.Job;
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
    // Impersinate to get all jobs even without read grants
    SecurityContext orig = ACL.impersonate(ACL.SYSTEM);
    List<ParameterizedJob> candidateProjects =
        Jenkins.getInstance().getAllItems(ParameterizedJob.class);
    // Return to previous authentication context
    SecurityContextHolder.setContext(orig);

    for (ParameterizedJob candidateJob : candidateProjects) {
      hudson.model.BuildAuthorizationToken authToken = candidateJob.getAuthToken();

      // do nothing with this job, this does not have a auth token
      if (authToken != null && authToken.getToken() != null) {

        if (!authToken.getToken().equals(queryStringToken)) {
          continue;
        }

      } else {

        try {
          // Try search again the job with the authentication context
          // of the user to make sure it only can find it if
          // authenticated
          Job<?, ?> j = Jenkins.getInstance().getItemByFullName(candidateJob.getName(), Job.class);
          if (j == null) {
            continue;
          }
        } catch (Exception x) {
          throw x;
        } finally {
          // Just handle any error
        }
      }
      GenericTrigger genericTriggerOpt = findGenericTrigger(candidateJob.getTriggers());
      if (genericTriggerOpt != null) {
        found.add(new FoundJob(candidateJob.getFullDisplayName(), genericTriggerOpt));
      }
    }
    return found;
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
