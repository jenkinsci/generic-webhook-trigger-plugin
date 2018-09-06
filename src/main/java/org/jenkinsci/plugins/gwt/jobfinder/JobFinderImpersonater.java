package org.jenkinsci.plugins.gwt.jobfinder;

import hudson.security.ACL;
import java.util.List;
import jenkins.model.Jenkins;
import jenkins.model.ParameterizedJobMixIn.ParameterizedJob;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;

public class JobFinderImpersonater {
  public List<ParameterizedJob> getAllParameterizedJobs(boolean impersonate) {
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
