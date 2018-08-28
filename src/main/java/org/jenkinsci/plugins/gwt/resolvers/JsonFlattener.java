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
    doFlatternJson(key, regexFilter, resolved, resolvedVariables);

    if (resolved != null && !(resolved instanceof String)) {
      final String variableName = toVariableName(key);
      resolvedVariables.put(variableName, filter(GSON.toJson(resolved).toString(), regexFilter));
    }

    return resolvedVariables;
  }

  @SuppressWarnings("unchecked")
  private void doFlatternJson(
      final String key,
      final String regexFilter,
      final Object resolved,
      final Map<String, String> resolvedVariables) {
    if (resolved instanceof List) {
      int i = 0;
      for (final Object o : (List<?>) resolved) {
        doFlatternJson(key + "_" + i, regexFilter, o, resolvedVariables);
        i++;
      }
    } else if (resolved instanceof Map) {
      for (final Entry<String, Object> entry : ((Map<String, Object>) resolved).entrySet()) {
        doFlatternJson(
            key + "_" + entry.getKey(), regexFilter, entry.getValue(), resolvedVariables);
      }
    } else if (resolved != null) {
      final String variableName = toVariableName(key);
      String string = resolved.toString();
      if (!(resolved instanceof String)) {
        string = GSON.toJson(resolved).toString();
      }
      resolvedVariables.put(variableName, filter(string, regexFilter));
    }
  }
}
