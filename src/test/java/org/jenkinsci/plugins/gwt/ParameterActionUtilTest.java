package org.jenkinsci.plugins.gwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jenkinsci.plugins.gwt.ParameterActionUtil.createParameterAction;

import com.google.common.collect.ImmutableMap;
import hudson.model.BooleanParameterDefinition;
import hudson.model.ParametersAction;
import hudson.model.ParametersDefinitionProperty;
import hudson.model.StringParameterDefinition;
import java.util.Map;
import org.junit.Test;

public class ParameterActionUtilTest {

  @Test
  public void testThatStringKeepsItsDefaultValueWhenNoParameterSupplied() {
    final ParametersDefinitionProperty parametersDefinitionProperty =
        new ParametersDefinitionProperty(
            new StringParameterDefinition("name", "the default value") //
            );
    final Map<String, String> resolvedVariables =
        ImmutableMap.<String, String>builder() //
            .build();

    final ParametersAction actual =
        createParameterAction(parametersDefinitionProperty, resolvedVariables);

    assertThat(actual) //
        .hasSize(1);
    assertThat(actual.getAllParameters().get(0).getValue()) //
        .isEqualTo("the default value");
  }

  @Test
  public void testThatStringDoesNotKeepItsDefaultValueWhenParameterSupplied() {
    final ParametersDefinitionProperty parametersDefinitionProperty =
        new ParametersDefinitionProperty(
            new StringParameterDefinition("name", "the default value") //
            );
    final Map<String, String> resolvedVariables =
        ImmutableMap.<String, String>of( //
            "name", "this is supplied");

    final ParametersAction actual =
        createParameterAction(parametersDefinitionProperty, resolvedVariables);

    assertThat(actual) //
        .hasSize(1);
    assertThat(actual.getAllParameters().get(0).getValue()) //
        .isEqualTo("this is supplied");
  }

  @Test
  public void testThatBooleanKeepsItsDefaultValueWhenNoParameterSupplied() {
    final ParametersDefinitionProperty parametersDefinitionProperty =
        new ParametersDefinitionProperty(
            new BooleanParameterDefinition("name", true, null) //
            );
    final Map<String, String> resolvedVariables =
        ImmutableMap.<String, String>builder() //
            .build();

    final ParametersAction actual =
        createParameterAction(parametersDefinitionProperty, resolvedVariables);

    assertThat(actual) //
        .hasSize(1);
    assertThat(actual.getAllParameters().get(0).getValue()) //
        .isEqualTo(true);
  }

  @Test
  public void testThatBooleanDoesNotKeepItsDefaultValueWhenParameterSuppliedAndFalseIsFalse() {
    final ParametersDefinitionProperty parametersDefinitionProperty =
        new ParametersDefinitionProperty(
            new BooleanParameterDefinition("name", true, null) //
            );
    final Map<String, String> resolvedVariables =
        ImmutableMap.<String, String>of( //
            "name", "false");

    final ParametersAction actual =
        createParameterAction(parametersDefinitionProperty, resolvedVariables);

    assertThat(actual) //
        .hasSize(1);
    assertThat(actual.getAllParameters().get(0).getValue()) //
        .isEqualTo(false);
  }

  @Test
  public void testThatBooleanDoesNotKeepItsDefaultValueWhenParameterSuppliedAndTrueIsTrue() {
    final ParametersDefinitionProperty parametersDefinitionProperty =
        new ParametersDefinitionProperty(
            new BooleanParameterDefinition("name", true, null) //
            );
    final Map<String, String> resolvedVariables =
        ImmutableMap.<String, String>of( //
            "name", "true");

    final ParametersAction actual =
        createParameterAction(parametersDefinitionProperty, resolvedVariables);

    assertThat(actual) //
        .hasSize(1);
    assertThat(actual.getAllParameters().get(0).getValue()) //
        .isEqualTo(true);
  }
}
