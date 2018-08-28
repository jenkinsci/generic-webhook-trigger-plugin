package org.jenkinsci.plugins.gwt.bdd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jenkinsci.plugins.gwt.GenericHeaderVariable;
import org.jenkinsci.plugins.gwt.GenericRequestVariable;
import org.jenkinsci.plugins.gwt.GenericVariable;

public class FeatureState {
  private Map<String, List<String>> headers = new HashMap<>();
  private Map<String, String[]> parameterMap = new HashMap<>();
  private String postContent;
  private List<GenericVariable> genericVariables = new ArrayList<>();
  private List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
  private List<GenericHeaderVariable> genericHeaderVariables = new ArrayList<>();
  private String regexpFilterText;
  private String regexpFilterExpression;

  public Map<String, List<String>> getHeaders() {
    return headers;
  }

  public void setHeaders(final Map<String, List<String>> headers) {
    this.headers = headers;
  }

  public Map<String, String[]> getParameterMap() {
    return parameterMap;
  }

  public void setParameterMap(final Map<String, String[]> parameterMap) {
    this.parameterMap = parameterMap;
  }

  public String getPostContent() {
    return postContent;
  }

  public void setPostContent(final String postContent) {
    this.postContent = postContent;
  }

  public List<GenericVariable> getGenericVariables() {
    return genericVariables;
  }

  public void setGenericVariables(final List<GenericVariable> genericVariables) {
    this.genericVariables = genericVariables;
  }

  public List<GenericRequestVariable> getGenericRequestVariables() {
    return genericRequestVariables;
  }

  public void setGenericRequestVariables(
      final List<GenericRequestVariable> genericRequestVariables) {
    this.genericRequestVariables = genericRequestVariables;
  }

  public List<GenericHeaderVariable> getGenericHeaderVariables() {
    return genericHeaderVariables;
  }

  public void setGenericHeaderVariables(final List<GenericHeaderVariable> genericHeaderVariables) {
    this.genericHeaderVariables = genericHeaderVariables;
  }

  public String getRegexpFilterText() {
    return regexpFilterText;
  }

  public void setRegexpFilterText(final String regexpFilterText) {
    this.regexpFilterText = regexpFilterText;
  }

  public String getRegexpFilterExpression() {
    return regexpFilterExpression;
  }

  public void setRegexpFilterExpression(final String regexpFilterExpression) {
    this.regexpFilterExpression = regexpFilterExpression;
  }

  @Override
  public String toString() {
    return "FeatureState [headers="
        + headers
        + ", parameterMap="
        + parameterMap
        + ", postContent="
        + postContent
        + ", genericVariables="
        + genericVariables
        + ", genericRequestVariables="
        + genericRequestVariables
        + ", genericHeaderVariables="
        + genericHeaderVariables
        + ", regexpFilterText="
        + regexpFilterText
        + ", regexpFilterExpression="
        + regexpFilterExpression
        + "]";
  }
}
