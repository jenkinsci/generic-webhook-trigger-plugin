package org.jenkinsci.plugins.gwt.resolvers;

public class FlattenerUtils {

  public static String filter(String string, String regexpFilter) {
    if (string == null || regexpFilter == null || regexpFilter.isEmpty()) {
      return string;
    }
    return string.replaceAll(regexpFilter, "");
  }

  public static String toVariableName(String mixedString) {
    if (mixedString == null) {
      return null;
    }
    return mixedString.replaceAll("\\s", "_").replaceAll("-", "_");
  }
}
