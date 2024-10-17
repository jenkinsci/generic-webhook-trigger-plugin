package org.jenkinsci.plugins.gwt;

import static com.google.common.base.Strings.isNullOrEmpty;

import hudson.model.Cause;
import java.util.Map;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

public class GenericCause extends Cause {

    private final Map<String, String> resolvedVariables;
    private final String postContent;
    private final boolean printContributedVariables;
    private final boolean printPostContent;
    /** Used at runtime to only print once. */
    private boolean postContentPrinted;

    private final String cause;

    public GenericCause(
            final String postContent,
            final Map<String, String> resolvedVariables,
            final boolean printContributedVariables,
            final boolean printPostContent,
            final String cause) {
        this.postContent = postContent;
        this.resolvedVariables = resolvedVariables;
        this.printContributedVariables = printContributedVariables;
        this.printPostContent = printPostContent;
        this.postContentPrinted = false;
        if (!isNullOrEmpty(cause)) {
            this.cause = cause;
        } else {
            this.cause = "Generic Cause";
        }
    }

    public boolean isPrintContributedVariables() {
        return printContributedVariables;
    }

    public boolean isPrintPostContent() {
        return printPostContent;
    }

    public void setPostContentPrinted(boolean postContentPrinted) {
        this.postContentPrinted = postContentPrinted;
    }

    public boolean isPostContentPrinted() {
        return postContentPrinted;
    }

    public Map<String, String> getResolvedVariables() {
        return resolvedVariables;
    }

    public String getPostContent() {
        return postContent;
    }

    @Override
    public String getShortDescription() {
        PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
        return policy.sanitize(cause);
    }
}
