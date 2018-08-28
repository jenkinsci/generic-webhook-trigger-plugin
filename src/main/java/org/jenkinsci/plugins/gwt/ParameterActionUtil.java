package org.jenkinsci.plugins.gwt;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newArrayList;

import com.cloudbees.plugins.credentials.CredentialsParameterValue;
import hudson.model.BooleanParameterValue;
import hudson.model.ParameterDefinition;
import hudson.model.ParameterValue;
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
        final String paramName = parameterDefinition.getName();
        final ParameterValue defaultParameterValue = parameterDefinition.getDefaultParameterValue();
        if (defaultParameterValue != null) {
          final ParameterValue parameterValue =
              getParameterValue(
                  resolvedVariables, parameterDefinition, paramName, defaultParameterValue);
          parameterList.add(parameterValue);
        }
      }
    }
    return parameterList;
  }

  private static ParameterValue getParameterValue(
      final Map<String, String> resolvedVariables,
      final ParameterDefinition parameterDefinition,
      final String paramName,
      final ParameterValue defaultParameterValue) {
    final String stringValue = getStringValue(resolvedVariables, paramName, defaultParameterValue);
    if (defaultParameterValue.getValue() instanceof Boolean) {
      return new BooleanParameterValue(
          paramName, Boolean.parseBoolean(stringValue), parameterDefinition.getDescription());
    }
    if (defaultParameterValue instanceof CredentialsParameterValue) {
      return new CredentialsParameterValue(
          paramName, stringValue, parameterDefinition.getDescription());
    }
    return new StringParameterValue(paramName, stringValue, parameterDefinition.getDescription());
  }

  private static String getStringValue(
      final Map<String, String> resolvedVariables,
      final String param,
      final ParameterValue defaultParameterValue) {
    if (!isNullOrEmpty(resolvedVariables.get(param))) {
      return resolvedVariables.get(param);
    }
    if (defaultParameterValue.getValue() == null) {
      return "";
    }
    return defaultParameterValue.getValue().toString();
  }
}
