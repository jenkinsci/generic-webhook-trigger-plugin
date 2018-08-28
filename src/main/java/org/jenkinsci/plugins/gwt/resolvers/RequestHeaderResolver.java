package org.jenkinsci.plugins.gwt.resolvers;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;
import static org.jenkinsci.plugins.gwt.resolvers.FlattenerUtils.filter;
import static org.jenkinsci.plugins.gwt.resolvers.FlattenerUtils.toVariableName;

import com.google.common.base.Optional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.jenkinsci.plugins.gwt.GenericHeaderVariable;

public class RequestHeaderResolver {
  public RequestHeaderResolver() {}

  public Map<String, String> getRequestHeaders(
      final List<GenericHeaderVariable> configuredGenericHeaderVariables,
      final Map<String, List<String>> incomingHeaders) {
    final Map<String, String> found = new HashMap<>();
    for (final Entry<String, List<String>> headersEntry : incomingHeaders.entrySet()) {
      final String headerName = headersEntry.getKey();
      final Optional<GenericHeaderVariable> configuredVariable =
          findConfiguredVariable(configuredGenericHeaderVariables, headerName);
      if (!configuredVariable.isPresent()) {
        continue;
      }
      final List<String> headers = headersEntry.getValue();
      int i = 0;
      for (final String headerValue : headers) {
        final String regexpFilter = configuredVariable.get().getRegexpFilter();
        final String filteredValue = filter(headerValue, regexpFilter);
        found.put(toVariableName(headerName).toLowerCase() + "_" + i, filteredValue);
        final boolean firstValue = i == 0;
        if (firstValue) {
          // Users will probably expect this variable for parameters that are never a list
          found.put(toVariableName(headerName).toLowerCase(), filteredValue);
        }
        i++;
      }
      incomingHeaders.put(headerName, headers);
    }
    return found;
  }

  private Optional<GenericHeaderVariable> findConfiguredVariable(
      final List<GenericHeaderVariable> configuredGenericHeaderVariables, final String headerName) {
    for (final GenericHeaderVariable ghv : configuredGenericHeaderVariables) {
      if (ghv.getHeaderName().equalsIgnoreCase(headerName)) {
        return of(ghv);
      }
    }
    return absent();
  }
}
