package org.jenkinsci.plugins.gwt;

import java.util.Map;

import hudson.model.Cause;

public class GenericCause extends Cause {

  private final Map<String, String> resolvedVariables;
  private final String postContent;

  public GenericCause(String postContent, Map<String, String> resolvedVariables) {
    this.postContent = postContent;
    this.resolvedVariables = resolvedVariables;
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
