package org.jenkinsci.plugins.gwt.bdd;

public class GenericVariablePojo {
  private final String variable;
  private final String expression;
  private final String expressionType;
  private final String defaultValue;
  private final String regexpFilter;

  public GenericVariablePojo(
      final String variable,
      final String expression,
      final String expressionType,
      final String defaultValue,
      final String regexpFilter) {
    this.variable = variable;
    this.expression = expression;
    this.expressionType = expressionType;
    this.defaultValue = defaultValue;
    this.regexpFilter = regexpFilter;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public String getExpression() {
    return expression;
  }

  public String getExpressionType() {
    return expressionType;
  }

  public String getRegexpFilter() {
    return regexpFilter;
  }

  public String getVariable() {
    return variable;
  }

  @Override
  public String toString() {
    return "GenericVariablePojo [variable="
        + variable
        + ", expression="
        + expression
        + ", expressionType="
        + expressionType
        + ", defaultValue="
        + defaultValue
        + ", regexpFilter="
        + regexpFilter
        + "]";
  }
}
