package org.jenkinsci.plugins.gwt.global;

import com.google.common.annotations.VisibleForTesting;
import hudson.Extension;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;
import org.jenkinsci.plugins.gwt.Renderer;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

@Extension
public class JobFinderConfig extends GlobalConfiguration implements Serializable {
  private static final long serialVersionUID = 1L;

  public static JobFinderConfig get() {
    return GlobalConfiguration.all().get(JobFinderConfig.class);
  }

  private List<JobFinderConfigPathItem> jobFinderConfigPathItemList = new ArrayList<>();

  @VisibleForTesting
  public JobFinderConfig(final List<JobFinderConfigPathItem> jobFinderConfigPathItemList) {
    this.jobFinderConfigPathItemList = jobFinderConfigPathItemList;
  }

  public JobFinderConfig() {
    load();
  }

  @Override
  public boolean configure(final StaplerRequest req, final JSONObject json) throws FormException {
    req.bindJSON(this, json);
    save();
    return true;
  }

  public boolean isEnabled() {
    return !this.jobFinderConfigPathItemList.isEmpty();
  }

  @DataBoundSetter
  public void setJobFinderConfigPathItemList(
      final List<JobFinderConfigPathItem> jobFinderPathListItemList) {
    this.jobFinderConfigPathItemList = jobFinderPathListItemList;
  }

  public List<JobFinderConfigPathItem> getJobFinderConfigPathItemList() {
    return jobFinderConfigPathItemList;
  }

  public List<String> getFullNameList(Map<String, String> resolvedVariables) {
    List<String> fullNameList = new ArrayList<>();
    for (final JobFinderConfigPathItem item : jobFinderConfigPathItemList) {
      List<String> jobNameList = Renderer.render(item.getPath(), resolvedVariables);
      if (jobNameList.isEmpty()) {
        continue;
      }
      fullNameList.addAll(jobNameList);
    }
    return fullNameList;
  }
}
