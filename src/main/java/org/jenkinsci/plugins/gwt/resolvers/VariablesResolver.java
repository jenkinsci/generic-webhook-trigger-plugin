package org.jenkinsci.plugins.gwt.resolvers;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.jenkinsci.plugins.gwt.GenericHeaderVariable;
import org.jenkinsci.plugins.gwt.GenericRequestVariable;
import org.jenkinsci.plugins.gwt.GenericVariable;

import com.google.common.collect.Lists;

public class VariablesResolver {
  private List<GenericVariable> configuredGenericVariables = Lists.newArrayList();
  private final List<GenericRequestVariable> configuredGenericRequestVariables;
  private final String incomingPostContent;
  private final Map<String, String[]> incomingParameterMap;
  private final RequestParameterResolver requestParameterResolver = new RequestParameterResolver();
  private final RequestHeaderResolver requestHeaderResolver = new RequestHeaderResolver();
  private final PostContentParameterResolver postContentParameterResolver =
      new PostContentParameterResolver();
  private final List<GenericHeaderVariable> configuredGenericHeaderVariables;
  private final Map<String, Enumeration<String>> incomingHeaders;

  public VariablesResolver(
      Map<String, Enumeration<String>> incomingHeaders,
      Map<String, String[]> incomingParameterMap,
      String incomingPostContent,
      List<GenericVariable> configuredGenericVariables,
      List<GenericRequestVariable> configuredGenericRequestVariables,
      List<GenericHeaderVariable> configuredGenericHeaderVariables) {
    this.incomingPostContent = incomingPostContent;
    this.configuredGenericVariables = configuredGenericVariables;
    this.incomingParameterMap = incomingParameterMap;
    this.configuredGenericRequestVariables = configuredGenericRequestVariables;
    this.configuredGenericHeaderVariables = configuredGenericHeaderVariables;
    this.incomingHeaders = incomingHeaders;
  }

  public Map<String, String> getVariables() {
    Map<String, String> resolvedVariables = newHashMap();
    resolvedVariables.putAll(
        requestHeaderResolver.getRequestHeaders(configuredGenericHeaderVariables, incomingHeaders));
    resolvedVariables.putAll(
        requestParameterResolver.getRequestParameters(
            configuredGenericRequestVariables, incomingParameterMap));
    resolvedVariables.putAll(
        postContentParameterResolver.getPostContentParameters(
            configuredGenericVariables, incomingPostContent));
    return resolvedVariables;
  }
}
