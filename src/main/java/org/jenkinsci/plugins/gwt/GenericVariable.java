package org.jenkinsci.plugins.gwt;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.jenkinsci.plugins.gwt.ExpressionType.JSONPath;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

public class GenericVariable extends AbstractDescribableImpl<GenericVariable> {

  @Extension public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();

  public static class DescriptorImpl extends Descriptor<GenericVariable> {
    @Override
    public String getDisplayName() {
      return "";
    }
  }

  private ExpressionType expressionType;
  private final String key;
  private final String value;
  private String regexpFilter;
  private String defaultValue;

  @DataBoundConstructor
  public GenericVariable(String key, String value) {
    this.key = checkNotNull(key, "Variable name");
    this.value = checkNotNull(value, "Variable expression");
  }

  @DataBoundSetter
  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  @DataBoundSetter
  public void setExpressionType(ExpressionType expressionType) {
    this.expressionType = expressionType;
  }

  @DataBoundSetter
  public void setRegexpFilter(String regexpFilter) {
    this.regexpFilter = regexpFilter;
  }

  public String getRegexpFilter() {
    return regexpFilter;
  }

  public ExpressionType getExpressionType() {
    if (expressionType == null) {
      return JSONPath;
    }
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
