package org.jenkinsci.plugins.gwt;

public class FoundJob {

  private final String jobName;
  private final GenericTrigger genericTrigger;

  public FoundJob(String jobName, GenericTrigger genericTrigger) {
    this.jobName = jobName;
    this.genericTrigger = genericTrigger;
  }

  public GenericTrigger getGenericTrigger() {
    return genericTrigger;
  }

  public String getJobName() {
    return jobName;
  }
}
