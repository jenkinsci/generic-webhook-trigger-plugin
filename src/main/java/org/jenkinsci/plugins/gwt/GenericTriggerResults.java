package org.jenkinsci.plugins.gwt;

import java.util.Map;

public class GenericTriggerResults {

  private final Map<String, String> resolvedVariables;
  private final String regexpFilterText;
  private final String regexpFilterExpression;
  private final String url;
  private final long id;
  private final boolean triggered;

  public GenericTriggerResults(
      final String url,
      final long id,
      final boolean triggered,
      final Map<String, String> resolvedVariables,
      final String regexpFilterText,
      final String regexpFilterExpression) {
    this.url = url;
    this.id = id;
    this.triggered = triggered;
    this.resolvedVariables = resolvedVariables;
    this.regexpFilterText = regexpFilterText;
    this.regexpFilterExpression = regexpFilterExpression;
  }

  public boolean isTriggered() {
    return this.triggered;
  }

  public long getId() {
    return this.id;
  }

  public String getUrl() {
    return this.url;
  }

  public String getRegexpFilterExpression() {
    return this.regexpFilterExpression;
  }

  public String getRegexpFilterText() {
    return this.regexpFilterText;
  }

  public Map<String, String> getResolvedVariables() {
    return this.resolvedVariables;
  }
}
