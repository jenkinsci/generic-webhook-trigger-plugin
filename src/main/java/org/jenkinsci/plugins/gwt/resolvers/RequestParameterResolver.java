package org.jenkinsci.plugins.gwt.resolvers;

import static com.google.common.base.Optional.absent;
import static com.google.common.collect.Maps.newHashMap;
import static org.jenkinsci.plugins.gwt.resolvers.FlattenerUtils.filter;
import static org.jenkinsci.plugins.gwt.resolvers.FlattenerUtils.toVariableName;

import java.util.List;
import java.util.Map;

import org.jenkinsci.plugins.gwt.GenericRequestVariable;

import com.google.common.base.Optional;

public class RequestParameterResolver {
  public RequestParameterResolver() {}

  public Map<String, String> getRequestParameters(
      List<GenericRequestVariable> configuredGenericRequestVariables,
      Map<String, String[]> incomingParameterMap) {
    final Map<String, String> resolvedVariables = newHashMap();
    if (incomingParameterMap != null) {
      for (final String requestParamName : incomingParameterMap.keySet()) {
        final Optional<GenericRequestVariable> configuredVariable =
            findConfiguredVariable(configuredGenericRequestVariables, requestParamName);
        if (!configuredVariable.isPresent()) {
          continue;
        }
        final String[] values = incomingParameterMap.get(requestParamName);
        for (int i = 0; i < values.length; i++) {
          final String filteredValue =
              filter(values[i], configuredVariable.get().getRegexpFilter());
          resolvedVariables.put(toVariableName(requestParamName) + "_" + i, filteredValue);
          final boolean firstAndOnlyValue = i == 0 && values.length == 1;
          if (firstAndOnlyValue) {
            //Users will probably expect this variable for parameters that are never a list
            resolvedVariables.put(toVariableName(requestParamName), filteredValue);
          }
        }
      }
    }
    return resolvedVariables;
  }

  private Optional<GenericRequestVariable> findConfiguredVariable(
      List<GenericRequestVariable> genericRequestVariables, String requestParamName) {
    if (genericRequestVariables != null) {
      for (final GenericRequestVariable v : genericRequestVariables) {
        if (v.getParameterName().equals(requestParamName)) {
          return Optional.of(v);
        }
      }
    }
    return absent();
  }
}
