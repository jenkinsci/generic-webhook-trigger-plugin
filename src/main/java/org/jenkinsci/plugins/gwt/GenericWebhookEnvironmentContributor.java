package org.jenkinsci.plugins.gwt;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.base.Charsets;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.EnvironmentContributor;
import hudson.model.Run;
import hudson.model.TaskListener;

@Extension
public class GenericWebhookEnvironmentContributor extends EnvironmentContributor {
  private static final String CONTRIBUTING_VARIABLES = "Contributing variables:";

  @SuppressWarnings("unchecked")
  @Override
  public void buildEnvironmentFor(
      @SuppressWarnings("rawtypes") @Nonnull final Run r,
      @Nonnull final EnvVars envs,
      @Nonnull final TaskListener listener)
      throws IOException, InterruptedException {
    final boolean shouldLog = shouldLog(r);
    final GenericCause cause = (GenericCause) r.getCause(GenericCause.class);
    if (cause != null) {
      if (shouldLog) {
        listener
            .getLogger()
            .println(
                GenericWebhookEnvironmentContributor.class.getSimpleName()
                    + " Received:\n\n"
                    + cause.getPostContent()
                    + "\n\n");
      }
      final Map<String, String> resolvedVariables = cause.getResolvedVariables();
      if (shouldLog) {
        listener.getLogger().println(CONTRIBUTING_VARIABLES + "\n");
      }
      for (final String variable : resolvedVariables.keySet()) {
        final String resolved = cause.getResolvedVariables().get(variable);
        if (shouldLog) {
          listener.getLogger().println("    " + variable + " = " + resolved);
        }
        envs.override(variable, resolved);
      }
      if (shouldLog) {
        listener.getLogger().println("\n");
      }
    }
  }

  private boolean shouldLog(@SuppressWarnings("rawtypes") final Run r) throws IOException {
    try (BufferedReader br =
        new BufferedReader(
            new InputStreamReader(new FileInputStream(r.getLogFile()), Charsets.UTF_8))) {
      String line;
      while ((line = br.readLine()) != null) {
        if (line.contains(CONTRIBUTING_VARIABLES)) {
          return false;
        }
      }
    }
    return true;
  }
}
