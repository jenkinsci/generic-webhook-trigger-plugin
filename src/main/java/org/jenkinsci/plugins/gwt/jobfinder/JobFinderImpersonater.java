package org.jenkinsci.plugins.gwt.jobfinder;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import hudson.security.ACL;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import jenkins.model.Jenkins;
import jenkins.model.ParameterizedJobMixIn.ParameterizedJob;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.jenkinsci.plugins.gwt.global.CacheConfig;

public class JobFinderImpersonater {
  private static final boolean DO_IMPERSONATE = true;
  private static Logger LOGGER = Logger.getLogger(JobFinderImpersonater.class.getName());
  private static final long CACHE_REFRESH_INITIAL_DELAY = 0;
  private ScheduledExecutorService scheduledExecutorService = null;
  private LoadingCache<Boolean, List<ParameterizedJob>> loadingCache = null;
  private boolean cacheGetJobs = false;
  private int cacheGetJobsMinutes = 0;

  public JobFinderImpersonater() {}

  public List<ParameterizedJob> getAllParameterizedJobs(final boolean impersonate) {
    this.reconfigureCachingIfNecessary();
    final boolean useCache = CacheConfig.get().isCacheGetJobs();
    if (useCache && impersonate) {
      try {
        LOGGER.log(Level.FINE, "Using the cache");
        return this.getCachedJobs();
      } catch (final ExecutionException e) {
        LOGGER.log(Level.SEVERE, "Was unable to getAllParameterizedJobs from cache.", e);
        return doGetAllParameterizedJobs(impersonate);
      }
    } else if (useCache && !impersonate) {
      LOGGER.log(
          Level.INFO,
          "Not using the cache because jobs are not retreieved with impersonation SYSTEM. "
              + "SYSTEM is only impersonated when using a token."
              + " If SYSTEM is not impersonated, only jobs available for the currently authenticated user is found.");
    }
    LOGGER.log(Level.FINE, "Not using the cache");
    return doGetAllParameterizedJobs(impersonate);
  }

  private List<ParameterizedJob> getCachedJobs() throws ExecutionException {
    return this.loadingCache.get(DO_IMPERSONATE);
  }

  synchronized void reconfigureCachingIfNecessary() {
    final boolean configCacheGetJobs = CacheConfig.get().isCacheGetJobs();
    final int configCacheGetJobsMinutes = CacheConfig.get().getCacheGetJobsMinutes();
    final boolean shouldReconfigure =
        this.cacheGetJobs != configCacheGetJobs
            || this.cacheGetJobsMinutes != configCacheGetJobsMinutes;
    if (shouldReconfigure) {
      LOGGER.log(
          Level.INFO,
          "Reconfiguring cache, was (enabled: "
              + this.cacheGetJobs
              + ", minutes: "
              + this.cacheGetJobsMinutes
              + ") changing to (enabled: "
              + configCacheGetJobs
              + ", minutes: "
              + configCacheGetJobsMinutes
              + ")");
    } else {
      return;
    }

    if (configCacheGetJobs) {
      this.stopCaching();
      this.startCaching();
      try {
        // Make a call to add the entry to cache
        this.getCachedJobs();
      } catch (final ExecutionException e) {
        LOGGER.log(Level.SEVERE, "Was unable to trigger cache", e);
      }
    } else {
      this.stopCaching();
    }
    this.cacheGetJobs = configCacheGetJobs;
    this.cacheGetJobsMinutes = configCacheGetJobsMinutes;
  }

  private void startCaching() {
    final int cacheMinutes = CacheConfig.get().getCacheGetJobsMinutes();
    final int cacheRefreshDuration = cacheMinutes > 1 ? cacheMinutes - 1 : cacheMinutes;

    this.loadingCache =
        CacheBuilder.newBuilder() //
            .refreshAfterWrite(Duration.ofMinutes(cacheMinutes)) //
            .build(
                new CacheLoader<Boolean, List<ParameterizedJob>>() {
                  @Override
                  public List<ParameterizedJob> load(final Boolean impersonate) throws Exception {
                    LOGGER.log(Level.FINE, "Loading the cache with impersonate " + impersonate);
                    return doGetAllParameterizedJobs(impersonate);
                  }
                });
    this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    this.scheduledExecutorService.scheduleWithFixedDelay(
        () -> {
          LOGGER.log(Level.FINE, "Triggering cache refresh");
          this.loadingCache.asMap().keySet().forEach((key) -> this.loadingCache.refresh(key));
        },
        CACHE_REFRESH_INITIAL_DELAY,
        cacheRefreshDuration,
        TimeUnit.MINUTES);
  }

  private void stopCaching() {
    if (this.scheduledExecutorService != null) {
      this.scheduledExecutorService.shutdown();
    }
    if (this.loadingCache != null) {
      this.loadingCache.invalidateAll();
    }
  }

  private static List<ParameterizedJob> doGetAllParameterizedJobs(final boolean impersonate) {
    SecurityContext orig = null;
    try {
      if (impersonate) {
        orig = ACL.impersonate(ACL.SYSTEM);
      }
      return Jenkins.getInstance().getAllItems(ParameterizedJob.class);
    } finally {
      if (impersonate) {
        SecurityContextHolder.setContext(orig);
      }
    }
  }
}
