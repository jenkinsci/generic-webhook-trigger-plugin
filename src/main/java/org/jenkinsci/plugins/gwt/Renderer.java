package org.jenkinsci.plugins.gwt;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.base.Strings.nullToEmpty;
import static java.util.logging.Level.FINE;
import static java.util.regex.Pattern.compile;

import com.google.common.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class Renderer {
  private static Logger LOGGER = Logger.getLogger(Renderer.class.getSimpleName());

  @VisibleForTesting
  public static boolean isMatching(
      final String renderedRegexpFilterText, final String regexpFilterExpression) {
    final boolean noFilterConfigured =
        isNullOrEmpty(renderedRegexpFilterText) && isNullOrEmpty(regexpFilterExpression);
    if (noFilterConfigured) {
      return true;
    }
    final boolean isMatching =
        compile(nullToEmpty(regexpFilterExpression)) //
            .matcher(nullToEmpty(renderedRegexpFilterText)) //
            .find();
    if (!isMatching) {
      LOGGER.log(
          FINE,
          "Not triggering \""
              + regexpFilterExpression
              + "\" not matching \""
              + renderedRegexpFilterText
              + "\".");
    }
    return isMatching;
  }

  @VisibleForTesting
  public static String renderText(
      String regexpFilterText, final Map<String, String> resolvedVariables) {
    if (isNullOrEmpty(regexpFilterText)) {
      return "";
    }
    final List<String> variables = getVariablesInResolveOrder(resolvedVariables.keySet());
    for (final String variable : variables) {
      regexpFilterText =
          replaceKey(regexpFilterText, resolvedVariables.get(variable), "$" + variable);
      regexpFilterText =
          replaceKey(regexpFilterText, resolvedVariables.get(variable), "${" + variable + "}");
    }
    return regexpFilterText;
  }

  private static String replaceKey(
      String regexpFilterText, final String resolvedVariable, final String key) {
    try {
      regexpFilterText =
          regexpFilterText //
              .replace(key, resolvedVariable);
    } catch (final IllegalArgumentException e) {
      throw new RuntimeException("Tried to replace " + key + " with " + resolvedVariable, e);
    }
    return regexpFilterText;
  }

  @VisibleForTesting
  static List<String> getVariablesInResolveOrder(final Set<String> unsorted) {
    final List<String> variables = new ArrayList<>(unsorted);
    Collections.sort(
        variables,
        new Comparator<String>() {
          @Override
          public int compare(final String o1, final String o2) {
            if (o1.length() == o2.length()) {
              return o1.compareTo(o2);
            } else if (o1.length() > o2.length()) {
              return -1;
            }
            return 1;
          }
        });
    return variables;
  }
}
