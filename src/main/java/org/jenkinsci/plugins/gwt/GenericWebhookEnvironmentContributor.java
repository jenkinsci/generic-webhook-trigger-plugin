package org.jenkinsci.plugins.gwt;

import com.google.common.base.Charsets;
import hudson.EnvVars;
import hudson.Extension;
import hudson.model.EnvironmentContributor;
import hudson.model.Run;
import hudson.model.TaskListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import javax.annotation.Nonnull;

@Extension
public class GenericWebhookEnvironmentContributor extends EnvironmentContributor {
  private static final String CONTRIBUTING_VARIABLES =
      GenericWebhookEnvironmentContributor.class.getSimpleName();

  @SuppressWarnings("unchecked")
  @Override
  public void buildEnvironmentFor(
      @SuppressWarnings("rawtypes") @Nonnull final Run r,
      @Nonnull final EnvVars envs,
      @Nonnull final TaskListener listener)
      throws IOException, InterruptedException {
    final GenericCause cause = (GenericCause) r.getCause(GenericCause.class);
    if (cause != null) {
      final boolean shouldLog =
          (cause.isPrintContributedVariables() || cause.isPrintPostContent()) && notLogged(r);
      if (shouldLog && cause.isPrintPostContent()) {
        listener.getLogger().println(CONTRIBUTING_VARIABLES);
        listener.getLogger().println(" Received:\n\n" + cause.getPostContent() + "\n\n");
      }
      final Map<String, String> resolvedVariables = cause.getResolvedVariables();
      if (shouldLog && cause.isPrintContributedVariables()) {
        listener.getLogger().println("Contributing variables:\n");
      }
      for (final String variable : resolvedVariables.keySet()) {
        final String resolved = cause.getResolvedVariables().get(variable);
        if (shouldLog && cause.isPrintContributedVariables()) {
          listener.getLogger().println("    " + variable + " = " + resolved);
        }
        envs.override(variable, resolved);
      }
      if (shouldLog && cause.isPrintContributedVariables()) {
        listener.getLogger().println("\n");
      }
    }
  }

  private boolean notLogged(@SuppressWarnings("rawtypes") final Run r) throws IOException {
    try (BufferedReader br =
        new BufferedReader(new InputStreamReader(r.getLogInputStream(), Charsets.UTF_8))) {
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
