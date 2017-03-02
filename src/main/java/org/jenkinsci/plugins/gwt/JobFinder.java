package org.jenkinsci.plugins.gwt;

import java.util.ArrayList;
import java.util.List;

import hudson.model.AbstractProject;
import jenkins.model.Jenkins;

public final class JobFinder {
  private JobFinder() {}

  public static List<GenericTrigger> findAllJobsWithTrigger() {
    List<GenericTrigger> found = new ArrayList<>();
    List<AbstractProject> candidateProjects =
        Jenkins.getInstance().getAllItems(AbstractProject.class);
    for (AbstractProject<?, ?> candidateJob : candidateProjects) {
      GenericTrigger genericTriggerOpt = candidateJob.getTrigger(GenericTrigger.class);
      if (genericTriggerOpt != null) {
        found.add(genericTriggerOpt);
      }
    }
    return found;
  }
}
