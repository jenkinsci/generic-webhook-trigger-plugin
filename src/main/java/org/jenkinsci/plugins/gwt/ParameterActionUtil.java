package org.jenkinsci.plugins.gwt;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newArrayList;
import hudson.model.ParameterValue;
import hudson.model.BooleanParameterValue;
import hudson.model.ParameterDefinition;
import hudson.model.ParametersAction;
import hudson.model.ParametersDefinitionProperty;
import hudson.model.StringParameterValue;

import java.util.List;
import java.util.Map;

public class ParameterActionUtil {

  public static ParametersAction createParameterAction(
      final ParametersDefinitionProperty parametersDefinitionProperty,
      final Map<String, String> resolvedVariables) {
    final List<ParameterValue> parameterList =
        getParametersWithRespectToDefaultValues(parametersDefinitionProperty, resolvedVariables);
    return new ParametersAction(parameterList);
  }

  /** To keep any default values set there. */
  static List<ParameterValue> getParametersWithRespectToDefaultValues(
      final ParametersDefinitionProperty parametersDefinitionProperty,
      final Map<String, String> resolvedVariables) {
    final List<ParameterValue> parameterList = newArrayList();
    if (parametersDefinitionProperty != null) {
      for (final ParameterDefinition parameterDefinition :
          parametersDefinitionProperty.getParameterDefinitions()) {
        final String param = parameterDefinition.getName();
        final ParameterValue defaultParameterValue = parameterDefinition.getDefaultParameterValue();
        if (defaultParameterValue != null) {
          String value = null;
          if (!isNullOrEmpty(resolvedVariables.get(param))) {
            value = resolvedVariables.get(param);
          } else {
            value = defaultParameterValue.getValue().toString();
          }
          if (defaultParameterValue.getValue() instanceof Boolean) {
            parameterList.add(
                new BooleanParameterValue(
                    param, Boolean.parseBoolean(value), parameterDefinition.getDescription()));
          } else {
            parameterList.add(
                new StringParameterValue(param, value, parameterDefinition.getDescription()));
          }
        }
      }
    }
    return parameterList;
  }
}
