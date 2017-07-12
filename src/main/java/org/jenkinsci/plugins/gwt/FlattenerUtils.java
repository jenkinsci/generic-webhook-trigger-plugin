package org.jenkinsci.plugins.gwt;

public class FlattenerUtils {

  public static String filter(String string, String regexpFilter) {
    if (string == null || regexpFilter == null || regexpFilter.isEmpty()) {
      return string;
    }
    return string.replaceAll(regexpFilter, "");
  }

  public static String noWhitespace(String mixedString) {
    return mixedString.replaceAll("\\s", "_");
  }
}
