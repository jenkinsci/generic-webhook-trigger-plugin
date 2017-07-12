package org.jenkinsci.plugins.gwt.resolvers;

import static com.google.common.collect.Maps.newHashMap;

import java.util.List;
import java.util.Map;

import org.jenkinsci.plugins.gwt.GenericRequestVariable;
import org.jenkinsci.plugins.gwt.GenericVariable;

import com.google.common.collect.Lists;

public class VariablesResolver {
  private List<GenericVariable> configuredGenericVariables = Lists.newArrayList();
  private final List<GenericRequestVariable> configuredGenericRequestVariables;
  private final String incomingPostContent;
  private final Map<String, String[]> incomingParameterMap;
  private final RequestParameterResolver requestParameterResolver = new RequestParameterResolver();
  private final PostContentParameterResolver postContentParameterResolver =
      new PostContentParameterResolver();

  public VariablesResolver(
      Map<String, String[]> incomingParameterMap,
      String incomingPostContent,
      List<GenericVariable> configuredGenericVariables,
      List<GenericRequestVariable> configuredGenericRequestVariables) {
    this.incomingPostContent = incomingPostContent;
    this.configuredGenericVariables = configuredGenericVariables;
    this.incomingParameterMap = incomingParameterMap;
    this.configuredGenericRequestVariables = configuredGenericRequestVariables;
  }

  public Map<String, String> getVariables() {
    Map<String, String> resolvedVariables = newHashMap();
    resolvedVariables.putAll(
        requestParameterResolver.getRequestParameters(
            configuredGenericRequestVariables, incomingParameterMap));
    resolvedVariables.putAll(
        postContentParameterResolver.getPostContentParameters(
            configuredGenericVariables, incomingPostContent));
    return resolvedVariables;
  }
}
