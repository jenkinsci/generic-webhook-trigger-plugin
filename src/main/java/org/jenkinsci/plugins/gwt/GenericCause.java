package org.jenkinsci.plugins.gwt;

import hudson.model.Cause;

import java.util.Map;

public class GenericCause extends Cause {

  private final Map<String, String> resolvedVariables;
  private final String postContent;
  private final boolean printContributedVariables;
  private final boolean printPostContent;

  public GenericCause(
      String postContent,
      Map<String, String> resolvedVariables,
      boolean printContributedVariables,
      boolean printPostContent) {
    this.postContent = postContent;
    this.resolvedVariables = resolvedVariables;
    this.printContributedVariables = printContributedVariables;
    this.printPostContent = printPostContent;
  }

  public boolean isPrintContributedVariables() {
    return printContributedVariables;
  }

  public boolean isPrintPostContent() {
    return printPostContent;
  }

  public Map<String, String> getResolvedVariables() {
    return resolvedVariables;
  }

  public String getPostContent() {
    return postContent;
  }

  @Override
  public String getShortDescription() {
    return "Generic Cause";
  }
}
