package org.jenkinsci.plugins.gwt.resolvers;

import static com.google.common.base.Preconditions.checkNotNull;

public class FlattenerUtils {

  public static String filter(String string, String regexpFilter) {
    if (string == null || regexpFilter == null || regexpFilter.isEmpty()) {
      return string;
    }
    return string.replaceAll(regexpFilter, "");
  }

  public static String toVariableName(String variableName) {
    return checkNotNull(variableName, "variable name must be set") //
        .replaceAll("\\s", "_") //
        .replaceAll("-", "_");
  }
}
