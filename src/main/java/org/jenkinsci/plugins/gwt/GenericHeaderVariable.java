package org.jenkinsci.plugins.gwt;

import static com.google.common.base.Preconditions.checkNotNull;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import org.kohsuke.stapler.DataBoundConstructor;

public class GenericHeaderVariable extends AbstractDescribableImpl<GenericHeaderVariable> {

  @Extension public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();

  public static class DescriptorImpl extends Descriptor<GenericHeaderVariable> {
    @Override
    public String getDisplayName() {
      return "";
    }
  }

  private final String key;
  private final String regexpFilter;

  @DataBoundConstructor
  public GenericHeaderVariable(String key, String regexpFilter) {
    this.key = checkNotNull(key, "Variable name");
    this.regexpFilter = regexpFilter;
  }

  public String getKey() {
    return key;
  }

  public String getRegexpFilter() {
    return regexpFilter;
  }

  public String getHeaderName() {
    return key;
  }

  @Override
  public String toString() {
    return "GenericHeaderVariable [key=" + key + ", regexpFilter=" + regexpFilter + "]";
  }
}
