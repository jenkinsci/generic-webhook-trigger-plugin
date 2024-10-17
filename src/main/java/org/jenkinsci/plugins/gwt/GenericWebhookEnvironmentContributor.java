package org.jenkinsci.plugins.gwt;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.EnvVars;
import hudson.Extension;
import hudson.model.EnvironmentContributor;
import hudson.model.Run;
import hudson.model.TaskListener;
import java.io.IOException;
import java.util.Map;

@Extension
public class GenericWebhookEnvironmentContributor extends EnvironmentContributor {

    @SuppressWarnings("unchecked")
    @Override
    public void buildEnvironmentFor(
            @SuppressWarnings("rawtypes") @NonNull final Run r,
            @NonNull final EnvVars envs,
            @NonNull final TaskListener listener)
            throws IOException, InterruptedException {
        final GenericCause cause = (GenericCause) r.getCause(GenericCause.class);
        if (cause != null) {
            final boolean shouldLog = (cause.isPrintContributedVariables() || cause.isPrintPostContent())
                    && !cause.isPostContentPrinted();
            if (shouldLog) {
                cause.setPostContentPrinted(true);
            }
            if (shouldLog && cause.isPrintPostContent()) {
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
}
