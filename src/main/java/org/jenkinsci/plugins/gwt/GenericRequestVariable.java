package org.jenkinsci.plugins.gwt;

import static com.google.common.base.Preconditions.checkNotNull;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import org.kohsuke.stapler.DataBoundConstructor;

public class GenericRequestVariable extends AbstractDescribableImpl<GenericRequestVariable> {

  @Extension public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();

  public static class DescriptorImpl extends Descriptor<GenericRequestVariable> {
    @Override
    public String getDisplayName() {
      return "";
    }
  }

  private final String key;
  private final String regexpFilter;

  @DataBoundConstructor
  public GenericRequestVariable(String key, String regexpFilter) {
    this.key = checkNotNull(key, "Variable name");
    this.regexpFilter = regexpFilter;
  }

  public String getRegexpFilter() {
    return regexpFilter;
  }

  public String getKey() {
    return key;
  }

  public String getParameterName() {
    return key;
  }

  @Override
  public String toString() {
    return "GenericRequestVariable [key=" + key + ", regexpFilter=" + regexpFilter + "]";
  }
}
