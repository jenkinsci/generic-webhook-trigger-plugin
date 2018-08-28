package org.jenkinsci.plugins.gwt.jobfinder;

import hudson.security.ACL;
import java.util.List;
import jenkins.model.Jenkins;
import jenkins.model.ParameterizedJobMixIn.ParameterizedJob;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;

public class JobFinderImpersonater {
  public List<ParameterizedJob> getAllParameterizedJobsByImpersonation() {
    // Impersinate to get all jobs even without read grants
    final SecurityContext orig = ACL.impersonate(ACL.SYSTEM);
    final List<ParameterizedJob> jobs = Jenkins.getInstance().getAllItems(ParameterizedJob.class);
    // Return to previous authentication context
    SecurityContextHolder.setContext(orig);
    return jobs;
  }
}
