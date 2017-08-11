package org.jenkinsci.plugins.gwt;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.jenkinsci.plugins.gwt.ExpressionType.JSONPath;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;

import org.kohsuke.stapler.DataBoundConstructor;

public class GenericVariable extends AbstractDescribableImpl<GenericVariable> {

  @Extension public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();

  public static class DescriptorImpl extends Descriptor<GenericVariable> {
    @Override
    public String getDisplayName() {
      return "";
    }
  }

  private final ExpressionType expressionType;
  private final String key;
  private final String value;
  private final String regexpFilter;

  @DataBoundConstructor
  public GenericVariable(
      String key, String value, ExpressionType expressionType, String regexpFilter) {
    this.key = checkNotNull(key, "Variable name");
    this.value = checkNotNull(value, "Variable expression");
    if (expressionType == null) {
      this.expressionType = JSONPath;
    } else {
      this.expressionType = expressionType;
    }
    this.regexpFilter = regexpFilter;
  }

  public String getRegexpFilter() {
    return regexpFilter;
  }

  public ExpressionType getExpressionType() {
    return expressionType;
  }

  public String getKey() {
    return key;
  }

  public String getVariableName() {
    return key;
  }

  public String getValue() {
    return value;
  }

  public String getExpression() {
    return value;
  }

  @Override
  public String toString() {
    return "GenericVariable [expressionType="
        + expressionType
        + ", key="
        + key
        + ", value="
        + value
        + ", regexpFilter="
        + regexpFilter
        + "]";
  }
}
