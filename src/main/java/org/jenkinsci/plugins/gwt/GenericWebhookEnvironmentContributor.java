package org.jenkinsci.plugins.gwt;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.EnvironmentContributor;
import hudson.model.Run;
import hudson.model.TaskListener;
import java.io.IOException;
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
          (cause.isPrintContributedVariables() || cause.isPrintPostContent())
              && !cause.isPostContentPrinted();
      if (shouldLog) {
        cause.setPostContentPrinted(true);
        if (cause.isPrintPostContent()) {
          listener.getLogger().println(CONTRIBUTING_VARIABLES);
          listener.getLogger().println(" Received:\n\n" + cause.getPostContent() + "\n\n");
        }
        final Map<String, String> resolvedVariables = cause.getResolvedVariables();
        if (cause.isPrintContributedVariables()) {
          listener.getLogger().println("Contributing variables:\n");
        }
        for (final String variable : resolvedVariables.keySet()) {
          final String resolved = cause.getResolvedVariables().get(variable);
          if (cause.isPrintContributedVariables()) {
            listener.getLogger().println("    " + variable + " = " + resolved);
          }
          envs.override(variable, resolved);
        }
        if (cause.isPrintContributedVariables()) {
          listener.getLogger().println("\n");
        }
      }
    }
  }
}
