package org.jenkinsci.plugins.gwt.global;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import java.io.Serializable;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

public class JobFinderConfigPathItem extends AbstractDescribableImpl<JobFinderConfigPathItem>
    implements Serializable {
  private static final long serialVersionUID = 1L;
  private String path;

  @DataBoundConstructor
  public JobFinderConfigPathItem(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }

  @DataBoundSetter
  public void setPath(final String path) {
    this.path = path;
  }

  @Extension
  public static class DescriptorImpl extends Descriptor<JobFinderConfigPathItem> {
    @NonNull
    @Override
    public String getDisplayName() {
      return "job finder config path item";
    }
  }
}
