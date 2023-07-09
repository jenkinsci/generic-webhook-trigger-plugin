package org.jenkinsci.plugins.gwt.resolvers;

import static com.google.common.collect.Maps.newHashMap;
import static org.jenkinsci.plugins.gwt.resolvers.FlattenerUtils.filter;
import static org.jenkinsci.plugins.gwt.resolvers.FlattenerUtils.toVariableName;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JsonFlattener {
  private static Gson GSON =
      new GsonBuilder() //
          .serializeNulls() //
          .create();

  public JsonFlattener() {}

  public Map<String, String> flattenJson(
      final String key, final String regexFilter, final Object resolved) {
    final Map<String, String> resolvedVariables = newHashMap();
    this.doFlattenJson(key, regexFilter, resolved, resolvedVariables);

    if (resolved != null && !(resolved instanceof String)) {
      final String variableName = toVariableName(key);
      resolvedVariables.put(
          variableName, filter(JsonFlattener.GSON.toJson(resolved).toString(), regexFilter));
    }

    return resolvedVariables;
  }

  @SuppressWarnings("unchecked")
  private void doFlattenJson(
      final String key,
      final String regexFilter,
      final Object resolved,
      final Map<String, String> resolvedVariables) {
    if (resolved instanceof List) {
      int i = 0;
      for (final Object o : (List<?>) resolved) {
        this.doFlattenJson(key + "_" + i, regexFilter, o, resolvedVariables);
        i++;
      }
    } else if (resolved instanceof Map) {
      for (final Entry<String, Object> entry : ((Map<String, Object>) resolved).entrySet()) {
        this.doFlattenJson(
            key + "_" + entry.getKey(), regexFilter, entry.getValue(), resolvedVariables);
      }
    } else if (resolved != null) {
      final String variableName = toVariableName(key);
      JsonFlattener.putVariable(resolvedVariables, variableName, resolved, regexFilter);
    }
  }

  public static void putVariable(
      final Map<String, String> resolvedVariables,
      final String variableName,
      final Object variableValue,
      final String regexFilter) {
    String string = variableValue.toString();
    if (!(variableValue instanceof String)) {
      string = JsonFlattener.GSON.toJson(variableValue).toString();
    }
    resolvedVariables.put(variableName, filter(string, regexFilter));
  }
}
