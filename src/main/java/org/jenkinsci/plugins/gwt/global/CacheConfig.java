package org.jenkinsci.plugins.gwt.global;

import hudson.Extension;
import java.io.Serializable;
import java.util.Optional;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

@Extension
public class CacheConfig extends GlobalConfiguration implements Serializable {

  private static final long serialVersionUID = -3077539230674127483L;
  private static final int DEFAULT_GET_JOBS_CACHE_MINUTES = 15;

  public static CacheConfig get() {
    return GlobalConfiguration.all().get(CacheConfig.class);
  }

  private boolean cacheGetJobs;
  private int cacheGetJobsMinutes;

  public CacheConfig(final boolean cacheGetJobs, final Integer cacheGetJobsMinutes) {
    this.cacheGetJobs = cacheGetJobs;
    this.cacheGetJobsMinutes = cacheGetJobsMinutes;
  }

  public CacheConfig() {
    this.load();
  }

  @Override
  public boolean configure(final StaplerRequest req, final JSONObject json) throws FormException {
    req.bindJSON(this, json);
    this.save();
    return true;
  }

  @DataBoundSetter
  public void setCacheGetJobs(final boolean cacheGetJobs) {
    this.cacheGetJobs = cacheGetJobs;
  }

  public boolean isCacheGetJobs() {
    return this.cacheGetJobs;
  }

  @DataBoundSetter
  public void setCacheGetJobsMinutes(final int cacheGetJobsMinutes) {
    if (cacheGetJobsMinutes < 1) {
      this.cacheGetJobsMinutes = 1;
    }
    this.cacheGetJobsMinutes = cacheGetJobsMinutes;
  }

  public int getCacheGetJobsMinutes() {
    return Optional.ofNullable(this.cacheGetJobsMinutes).orElse(DEFAULT_GET_JOBS_CACHE_MINUTES);
  }
}
