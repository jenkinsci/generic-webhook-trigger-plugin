package org.jenkinsci.plugins.gwt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;
import jenkins.model.Jenkins;
import jenkins.model.ParameterizedJobMixIn.ParameterizedJob;

public final class JobFinder {
  private JobFinder() {}

  public static List<GenericTrigger> findAllJobsWithTrigger() {
    List<GenericTrigger> found = new ArrayList<>();
    List<ParameterizedJob> candidateProjects =
        Jenkins.getInstance().getAllItems(ParameterizedJob.class);
    for (ParameterizedJob candidateJob : candidateProjects) {
      GenericTrigger genericTriggerOpt = findGenericTrigger(candidateJob.getTriggers());
      if (genericTriggerOpt != null) {
        found.add(genericTriggerOpt);
      }
    }
    return found;
  }

  private static GenericTrigger findGenericTrigger(Map<TriggerDescriptor, Trigger<?>> triggers) {
    if (triggers == null) return null;
    for (Trigger<?> candidate : triggers.values())
      if (candidate instanceof GenericTrigger) return (GenericTrigger) candidate;
    return null;
  }
}
