package org.jenkinsci.plugins.gwt.resolvers;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.fromNullable;
import static com.google.common.collect.Maps.newHashMap;
import static org.jenkinsci.plugins.gwt.FlattenerUtils.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jenkinsci.plugins.gwt.GenericRequestVariable;

import com.google.common.base.Optional;

public class RequestParameterResolver {
  public RequestParameterResolver() {}

  public Map<String, String> getRequestParameters(
      List<GenericRequestVariable> configuredGenericRequestVariables,
      Map<String, String[]> incomingParameterMap) {
    Map<String, String> resolvedVariables = newHashMap();
    if (incomingParameterMap != null) {
      for (String requestParamName : incomingParameterMap.keySet()) {
        Optional<String> mappedRequestParameterOpt =
            findMappedRequestParameter(configuredGenericRequestVariables, requestParamName);
        if (!mappedRequestParameterOpt.isPresent()) {
          continue;
        }
        String regexpFilter = mappedRequestParameterOpt.get();
        String[] values = incomingParameterMap.get(requestParamName);
        if (values.length == 1) {
          resolvedVariables.put(requestParamName, filter(values[0], regexpFilter));
        } else {
          List<String> foundFilteredValues = new ArrayList<>();
          for (String valueCandidate : values) {
            String filteredValue = filter(valueCandidate, regexpFilter);
            if (!filteredValue.isEmpty()) {
              foundFilteredValues.add(filteredValue);
            }
          }
          if (foundFilteredValues.size() == 1) {
            String filteredValue = foundFilteredValues.get(0);
            resolvedVariables.put(requestParamName, filteredValue);
          } else {
            for (int i = 0; i < foundFilteredValues.size(); i++) {
              String filteredValue = foundFilteredValues.get(i);
              resolvedVariables.put(requestParamName + "_" + i, filteredValue);
            }
          }
        }
      }
    }
    return resolvedVariables;
  }

  private Optional<String> findMappedRequestParameter(
      List<GenericRequestVariable> genericRequestVariables, String requestParamName) {
    if (genericRequestVariables != null) {
      for (GenericRequestVariable v : genericRequestVariables) {
        if (v.getKey().equals(requestParamName)) {
          return fromNullable(v.getRegexpFilter());
        }
      }
    }
    return absent();
  }
}
