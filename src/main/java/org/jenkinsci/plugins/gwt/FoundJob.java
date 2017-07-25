package org.jenkinsci.plugins.gwt;

public class FoundJob {

  private final String fullName;
  private final GenericTrigger genericTrigger;

  public FoundJob(String fullName, GenericTrigger genericTrigger) {
    this.fullName = fullName;
    this.genericTrigger = genericTrigger;
  }

  public GenericTrigger getGenericTrigger() {
    return genericTrigger;
  }

  public String getFullName() {
    return fullName;
  }
}
