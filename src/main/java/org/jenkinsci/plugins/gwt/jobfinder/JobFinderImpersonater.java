package org.jenkinsci.plugins.gwt.jobfinder;

import java.util.List;

import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;

import hudson.security.ACL;
import jenkins.model.Jenkins;
import jenkins.model.ParameterizedJobMixIn.ParameterizedJob;

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
