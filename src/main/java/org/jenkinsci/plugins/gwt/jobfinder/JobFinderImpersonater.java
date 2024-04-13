package org.jenkinsci.plugins.gwt.jobfinder;

import hudson.Extension;
import hudson.model.Item;
import hudson.model.listeners.ItemListener;
import hudson.security.ACL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import jenkins.model.Jenkins;
import jenkins.model.ParameterizedJobMixIn.ParameterizedJob;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;

@Extension
public class JobFinderImpersonater extends ItemListener {
  private static Logger LOGGER = Logger.getLogger(JobFinderImpersonater.class.getName());
  private static final Map<String, ParameterizedJob> JOBS_WITH_GWT = new ConcurrentHashMap<>();

  public List<ParameterizedJob> getAllParameterizedJobs(final boolean impersonate) {
    if (LOGGER.isLoggable(Level.FINEST)) {
      LOGGER.finest("There are " + this.JOBS_WITH_GWT.size() + " jobs in cache:");
      final TreeSet<String> sortedSet = new TreeSet<>(this.JOBS_WITH_GWT.keySet());
      for (final String job : sortedSet) {
        LOGGER.finest("  " + job);
      }
    }
    if (impersonate) {
      LOGGER.log(Level.FINE, "Using the cache");
      return new ArrayList<>(this.JOBS_WITH_GWT.values());
    }
    LOGGER.log(
        Level.FINE,
        "Not using the cache because jobs are not retreieved with impersonation SYSTEM. "
            + "SYSTEM is only impersonated when using a token."
            + " If SYSTEM is not impersonated, only jobs available for the currently authenticated user is found.");
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

  @Override
  public void onLoaded() {
    for (final ParameterizedJob job : doGetAllParameterizedJobs(true)) {
      this.putJob(job);
    }
    LOGGER.info("Loaded " + this.JOBS_WITH_GWT.size() + " jobs in cache");
  }

  @Override
  public void onUpdated(final Item job) {
    this.putJob(job);
  }

  @Override
  public void onCreated(final Item job) {
    this.putJob(job);
  }

  @Override
  public void onDeleted(final Item job) {
    this.deleteJob(job);
  }

  @Override
  public void onCopied(final Item src, final Item job) {
    this.putJob(job);
  }

  @Override
  public void onLocationChanged(
      final Item item, final String oldFullName, final String newFullName) {
    this.JOBS_WITH_GWT.remove(oldFullName);
    this.putJob(item);
  }

  private void putJob(final Item job) {
    if (job instanceof ParameterizedJob) {
      final ParameterizedJob parameterizedJob = (ParameterizedJob) job;
      final boolean hasGenericTrigger =
          GenericTriggerFinder.findGenericTrigger(parameterizedJob.getTriggers()) != null;
      if (hasGenericTrigger) {
        this.JOBS_WITH_GWT.put(parameterizedJob.getFullName(), parameterizedJob);
      }
    }
  }

  private void deleteJob(final Item job) {
    this.JOBS_WITH_GWT.remove(job.getFullName());
  }
}
