package org.jenkinsci.plugins.gwt;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Nonnull;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.EnvironmentContributor;
import hudson.model.Run;
import hudson.model.TaskListener;

@Extension
public class GenericWebhookEnvironmentContributor extends EnvironmentContributor {
  @Override
  public void buildEnvironmentFor(
      @Nonnull Run r, @Nonnull EnvVars envs, @Nonnull TaskListener listener)
      throws IOException, InterruptedException {
    GenericCause cause = (GenericCause) r.getCause(GenericCause.class);
    if (cause != null) {
      listener
          .getLogger()
          .println(
              GenericWebhookEnvironmentContributor.class.getSimpleName()
                  + " Received:\n\n"
                  + cause.getPostContent()
                  + "\n\n");
      Map<String, String> resolvedVariables = cause.getResolvedVariables();
      listener.getLogger().println("Contributing variables:\n");
      for (String variable : resolvedVariables.keySet()) {
        String resolved = cause.getResolvedVariables().get(variable);
        listener.getLogger().println("    " + variable + " = " + resolved);
        envs.override(variable, resolved);
      }
      listener.getLogger().println("\n");
    }
  }
}
