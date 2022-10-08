package org.jenkinsci.plugins.gwt.resolvers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.jenkinsci.plugins.gwt.GenericHeaderVariable;
import org.jenkinsci.plugins.gwt.GenericRequestVariable;
import org.jenkinsci.plugins.gwt.GenericVariable;

public class VariablesResolver {
  private List<GenericVariable> configuredGenericVariables = new ArrayList<>();
  private final List<GenericRequestVariable> configuredGenericRequestVariables;
  private final String incomingPostContent;
  private final Map<String, String[]> incomingParameterMap;
  private final RequestParameterResolver requestParameterResolver = new RequestParameterResolver();
  private final RequestHeaderResolver requestHeaderResolver = new RequestHeaderResolver();
  private final PostContentParameterResolver postContentParameterResolver =
      new PostContentParameterResolver();
  private final List<GenericHeaderVariable> configuredGenericHeaderVariables;
  private final Map<String, List<String>> incomingHeaders;
  private final boolean shouldNotFlattern;

  public VariablesResolver(
      final Map<String, List<String>> incomingHeaders,
      final Map<String, String[]> incomingParameterMap,
      final String incomingPostContent,
      final List<GenericVariable> configuredGenericVariables,
      final List<GenericRequestVariable> configuredGenericRequestVariables,
      final List<GenericHeaderVariable> configuredGenericHeaderVariables,
      final boolean shouldNotFlattern) {
    this.incomingPostContent = this.firstNotNull(incomingPostContent, "");
    this.configuredGenericVariables =
        this.firstNotNull(configuredGenericVariables, new ArrayList<GenericVariable>());
    this.incomingParameterMap =
        this.firstNotNull(incomingParameterMap, new HashMap<String, String[]>());
    this.configuredGenericRequestVariables =
        this.firstNotNull(
            configuredGenericRequestVariables, new ArrayList<GenericRequestVariable>());
    this.configuredGenericHeaderVariables =
        this.firstNotNull(configuredGenericHeaderVariables, new ArrayList<GenericHeaderVariable>());
    this.incomingHeaders = incomingHeaders;
    this.shouldNotFlattern = shouldNotFlattern;
  }

  private <T> T firstNotNull(final T o1, final T o2) {
    if (o1 != null) {
      return o1;
    }
    return o2;
  }

  public Map<String, String> getVariables() {
    final Map<String, String> resolvedVariables = new TreeMap<>();
    resolvedVariables.putAll(
        this.requestHeaderResolver.getRequestHeaders(
            this.configuredGenericHeaderVariables, this.incomingHeaders));
    resolvedVariables.putAll(
        this.requestParameterResolver.getRequestParameters(
            this.configuredGenericRequestVariables, this.incomingParameterMap));
    resolvedVariables.putAll(
        this.postContentParameterResolver.getPostContentParameters(
            this.configuredGenericVariables, this.incomingPostContent, this.shouldNotFlattern));
    return resolvedVariables;
  }
}
