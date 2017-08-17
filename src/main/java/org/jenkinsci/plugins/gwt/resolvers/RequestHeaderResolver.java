package org.jenkinsci.plugins.gwt.resolvers;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;
import static org.jenkinsci.plugins.gwt.resolvers.FlattenerUtils.filter;
import static org.jenkinsci.plugins.gwt.resolvers.FlattenerUtils.toVariableName;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jenkinsci.plugins.gwt.GenericHeaderVariable;

import com.google.common.base.Optional;

public class RequestHeaderResolver {
  public RequestHeaderResolver() {}

  public Map<String, String> getRequestHeaders(
      List<GenericHeaderVariable> configuredGenericHeaderVariables,
      Map<String, Enumeration<String>> incomingHeaders) {
    final Map<String, String> found = new HashMap<>();
    for (final String headerName : incomingHeaders.keySet()) {
      final Optional<GenericHeaderVariable> configuredVariable =
          findConfiguredVariable(configuredGenericHeaderVariables, headerName);
      if (!configuredVariable.isPresent()) {
        continue;
      }
      final Enumeration<String> headerEnumeration = incomingHeaders.get(headerName);
      final List<String> headers =
          Collections.list(headerEnumeration); // "depletes" headerEnumeration
      int i = 0;
      for (final String headerValue : headers) {
        final String regexpFilter = configuredVariable.get().getRegexpFilter();
        final String filteredValue = filter(headerValue, regexpFilter);
        found.put(toVariableName(headerName) + "_" + i, filteredValue);
        final boolean firstValue = i == 0;
        if (firstValue) {
          //Users will probably expect this variable for parameters that are never a list
          found.put(toVariableName(headerName), filteredValue);
        }
        i++;
      }
      incomingHeaders.put(
          headerName,
          Collections.enumeration(
              headers)); // "replete" headerEnumeration, so it can be reused by other jobs later on
    }
    return found;
  }

  private Optional<GenericHeaderVariable> findConfiguredVariable(
      List<GenericHeaderVariable> configuredGenericHeaderVariables, String headerName) {
    for (final GenericHeaderVariable ghv : configuredGenericHeaderVariables) {
      if (ghv.getHeaderName().equals(headerName)) {
        return of(ghv);
      }
    }
    return absent();
  }
}
