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

public class JobFinderImpersonater {
  private static Logger LOGGER = Logger.getLogger(JobFinderImpersonater.class.getName());

  private static final int CACHE_MINUTES = 15;
  private static final int CACHE_REFRESH_INITIAL_DELAY = 0;
  private static final int CACHE_REFRESH_DURATION = CACHE_MINUTES - 1;

  private final ScheduledExecutorService newSingleThreadScheduledExecutor;
  private final LoadingCache<Boolean, List<ParameterizedJob>> cache;

  public JobFinderImpersonater() {
    this.cache =
        CacheBuilder.newBuilder() //
            .refreshAfterWrite(Duration.ofMinutes(CACHE_MINUTES)) //
            .build(
                new CacheLoader<Boolean, List<ParameterizedJob>>() {
                  @Override
                  public List<ParameterizedJob> load(final Boolean impersonate) throws Exception {
                    LOGGER.log(Level.FINE, "Loading the cache with impersonate " + impersonate);
                    return doGetAllParameterizedJobs(impersonate);
                  }
                });
    this.newSingleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    this.newSingleThreadScheduledExecutor.scheduleWithFixedDelay(
        () -> {
          LOGGER.log(Level.FINE, "Triggering cache refresh");
          this.cache.asMap().keySet().parallelStream().forEach((key) -> this.cache.refresh(key));
        },
        CACHE_REFRESH_INITIAL_DELAY,
        CACHE_REFRESH_DURATION,
        TimeUnit.MINUTES);
  }

  public List<ParameterizedJob> getAllParameterizedJobs(
      final boolean impersonate, final boolean useCache) {
    if (useCache && impersonate) {
      try {
        LOGGER.log(Level.FINE, "Using the cache");
        return this.cache.get(impersonate);
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
