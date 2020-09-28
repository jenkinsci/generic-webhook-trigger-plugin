package org.jenkinsci.plugins.gwt;

import static com.google.common.collect.Lists.newArrayList;
import static org.jenkinsci.plugins.gwt.ParameterActionUtil.createParameterAction;
import static org.jenkinsci.plugins.gwt.Renderer.isMatching;
import static org.jenkinsci.plugins.gwt.Renderer.renderText;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.Extension;
import hudson.model.CauseAction;
import hudson.model.Item;
import hudson.model.Job;
import hudson.model.ParametersAction;
import hudson.model.ParametersDefinitionProperty;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import java.util.List;
import java.util.Map;
import jenkins.model.ParameterizedJobMixIn;
import org.jenkinsci.Symbol;
import org.jenkinsci.plugins.gwt.global.CredentialsHelper;
import org.jenkinsci.plugins.gwt.resolvers.VariablesResolver;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

public class GenericTrigger extends Trigger<Job<?, ?>> {

  private List<GenericVariable> genericVariables = newArrayList();
  private final String regexpFilterText;
  private final String regexpFilterExpression;
  private List<GenericRequestVariable> genericRequestVariables = newArrayList();
  private List<GenericHeaderVariable> genericHeaderVariables = newArrayList();
  private boolean printPostContent;
  private boolean printContributedVariables;
  private String causeString;
  private String token;
  private String tokenCredentialId;
  private boolean silentResponse;
  private boolean overrideQuietPeriod;

  @Symbol("GenericTrigger")
  public static class GenericDescriptor extends TriggerDescriptor {

    @Override
    public boolean isApplicable(final Item item) {
      return Job.class.isAssignableFrom(item.getClass());
    }

    @Override
    public String getDisplayName() {
      return "Generic Webhook Trigger";
    }

    public ListBoxModel doFillTokenCredentialIdItems(
        @AncestorInPath final Item item, @QueryParameter final String credentialsId) {
      return CredentialsHelper.doFillCredentialsIdItems(item, credentialsId);
    }

    public FormValidation doCheckTokenCredentialIdItems(@QueryParameter final String value) {
      return CredentialsHelper.doCheckFillCredentialsId(value);
    }
  }

  @DataBoundConstructor
  public GenericTrigger(
      final List<GenericVariable> genericVariables,
      final String regexpFilterText,
      final String regexpFilterExpression,
      final List<GenericRequestVariable> genericRequestVariables,
      final List<GenericHeaderVariable> genericHeaderVariables) {
    this.genericVariables = genericVariables;
    this.regexpFilterExpression = regexpFilterExpression;
    this.regexpFilterText = regexpFilterText;
    this.genericRequestVariables = genericRequestVariables;
    this.genericHeaderVariables = genericHeaderVariables;
  }

  @DataBoundSetter
  public void setCauseString(final String causeString) {
    this.causeString = causeString;
  }

  public String getCauseString() {
    return this.causeString;
  }

  @DataBoundSetter
  public void setPrintContributedVariables(final boolean printContributedVariables) {
    this.printContributedVariables = printContributedVariables;
  }

  @DataBoundSetter
  public void setPrintPostContent(final boolean printPostContent) {
    this.printPostContent = printPostContent;
  }

  @DataBoundSetter
  public void setSilentResponse(final boolean silentResponse) {
    this.silentResponse = silentResponse;
  }

  @DataBoundSetter
  public void setOverrideQuietPeriod(final boolean overrideQuietPeriod) {
    this.overrideQuietPeriod = overrideQuietPeriod;
  }

  public boolean getOverrideQuietPeriod() {
    return this.overrideQuietPeriod;
  }

  public boolean isSilentResponse() {
    return this.silentResponse;
  }

  public boolean isPrintContributedVariables() {
    return this.printContributedVariables;
  }

  public boolean isPrintPostContent() {
    return this.printPostContent;
  }

  @DataBoundSetter
  public void setToken(final String token) {
    this.token = token;
  }

  public String getToken() {
    return this.token;
  }

  @DataBoundSetter
  public void setTokenCredentialId(final String tokenCredentialId) {
    this.tokenCredentialId = tokenCredentialId;
  }

  public String getTokenCredentialId() {
    return this.tokenCredentialId;
  }

  @Extension public static final GenericDescriptor DESCRIPTOR = new GenericDescriptor();

  @SuppressWarnings("static-access")
  @SuppressFBWarnings({"RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT", "NP_NULL_ON_SOME_PATH"})
  public GenericTriggerResults trigger(
      final Map<String, List<String>> headers,
      final Map<String, String[]> parameterMap,
      final String postContent,
      final int quietPeriod) {
    final Map<String, String> resolvedVariables =
        new VariablesResolver(
                headers,
                parameterMap,
                postContent,
                this.genericVariables,
                this.genericRequestVariables,
                this.genericHeaderVariables)
            .getVariables();

    final String renderedRegexpFilterText = renderText(this.regexpFilterText, resolvedVariables);
    final boolean isMatching = isMatching(renderedRegexpFilterText, this.regexpFilterExpression);

    hudson.model.Queue.Item item = null;
    if (isMatching) {
      final String cause = renderText(this.causeString, resolvedVariables);
      final GenericCause genericCause =
          new GenericCause(
              postContent,
              resolvedVariables,
              this.printContributedVariables,
              this.printPostContent,
              cause);
      final ParametersDefinitionProperty parametersDefinitionProperty =
          this.job.getProperty(ParametersDefinitionProperty.class);
      final ParametersAction parameters =
          createParameterAction(parametersDefinitionProperty, resolvedVariables);
      item =
          this.retrieveScheduleJob(this.job) //
              .scheduleBuild2(this.job, quietPeriod, new CauseAction(genericCause), parameters);
    }
    return new GenericTriggerResults(
        item, resolvedVariables, renderedRegexpFilterText, this.regexpFilterExpression);
  }

  @SuppressWarnings("rawtypes")
  private ParameterizedJobMixIn<?, ?> retrieveScheduleJob(final Job<?, ?> job) {
    return new ParameterizedJobMixIn() {
      @Override
      protected Job<?, ?> asJob() {
        return job;
      }
    };
  }

  public List<GenericVariable> getGenericVariables() {
    return this.genericVariables;
  }

  public String getRegexpFilterExpression() {
    return this.regexpFilterExpression;
  }

  public List<GenericRequestVariable> getGenericRequestVariables() {
    return this.genericRequestVariables;
  }

  public List<GenericHeaderVariable> getGenericHeaderVariables() {
    return this.genericHeaderVariables;
  }

  public String getRegexpFilterText() {
    return this.regexpFilterText;
  }

  @Override
  public String toString() {
    return "GenericTrigger [genericVariables="
        + this.genericVariables
        + ", regexpFilterText="
        + this.regexpFilterText
        + ", regexpFilterExpression="
        + this.regexpFilterExpression
        + ", genericRequestVariables="
        + this.genericRequestVariables
        + ", genericHeaderVariables="
        + this.genericHeaderVariables
        + "]";
  }
}
