package org.jenkinsci.plugins.gwt.jobfinder;

import hudson.security.ACL;
import java.util.List;
import java.util.logging.Logger;
import jenkins.model.Jenkins;
import jenkins.model.ParameterizedJobMixIn.ParameterizedJob;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;

public class JobFinderImpersonater {
  private static Logger LOGGER = Logger.getLogger(JobFinderImpersonater.class.getName());

  public JobFinderImpersonater() {}

  public List<ParameterizedJob> getAllParameterizedJobs(final boolean impersonate) {
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
