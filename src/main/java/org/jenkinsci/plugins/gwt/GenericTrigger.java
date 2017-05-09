package org.jenkinsci.plugins.gwt;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.logging.Level.INFO;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.kohsuke.stapler.DataBoundConstructor;

import com.google.common.annotations.VisibleForTesting;

import hudson.Extension;
import hudson.model.CauseAction;
import hudson.model.Item;
import hudson.model.Job;
import hudson.model.ParameterValue;
import hudson.model.ParametersAction;
import hudson.model.StringParameterValue;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;
import jenkins.model.ParameterizedJobMixIn;

public class GenericTrigger extends Trigger<Job<?, ?>> {

  private static final Logger LOGGER = Logger.getLogger(GenericTrigger.class.getName());
  private List<GenericVariable> genericVariables = newArrayList();
  private final String regexpFilterText;
  private final String regexpFilterExpression;
  private List<GenericRequestVariable> genericRequestVariables = newArrayList();

  public static class GenericDescriptor extends TriggerDescriptor {

    @Override
    public boolean isApplicable(Item item) {
      return Job.class.isAssignableFrom(item.getClass());
    }

    @Override
    public String getDisplayName() {
      return "Generic Webhook Trigger";
    }
  }

  @DataBoundConstructor
  public GenericTrigger(
      List<GenericVariable> genericVariables,
      String regexpFilterText,
      String regexpFilterExpression,
      List<GenericRequestVariable> genericRequestVariables) {
    this.genericVariables = genericVariables;
    this.regexpFilterExpression = regexpFilterExpression;
    this.regexpFilterText = regexpFilterText;
    this.genericRequestVariables = genericRequestVariables;
  }

  @Extension public static final GenericDescriptor DESCRIPTOR = new GenericDescriptor();

  public void trigger(Map<String, String[]> parameterMap, String postContent) {
    Map<String, String> resolvedVariables =
        new VariablesResolver(parameterMap, postContent, genericVariables, genericRequestVariables)
            .getVariables();

    boolean isMatching = isMatching(regexpFilterText, regexpFilterExpression, resolvedVariables);

    if (isMatching) {
      GenericCause cause = new GenericCause(postContent, resolvedVariables);

      ParametersAction parameters = createParameters(resolvedVariables);
      retrieveScheduleJob(job).scheduleBuild2(job, 0, new CauseAction(cause), parameters);
    }
  }

  private ParameterizedJobMixIn<?, ?> retrieveScheduleJob(final Job<?, ?> job) {
    return new ParameterizedJobMixIn() {
      @Override
      protected Job<?, ?> asJob() {
        return job;
      }
    };
  }

  private ParametersAction createParameters(Map<String, String> resolvedVariables) {
    List<ParameterValue> parameterList = newArrayList();
    for (Entry<String, String> entry : resolvedVariables.entrySet()) {
      ParameterValue parameter = new StringParameterValue(entry.getKey(), entry.getValue());
      parameterList.add(parameter);
    }
    return new ParametersAction(parameterList);
  }

  @VisibleForTesting
  boolean isMatching(
      String regexpFilterText,
      String regexpFilterExpression,
      Map<String, String> resolvedVariables) {
    if (isNullOrEmpty(regexpFilterText) || isNullOrEmpty(regexpFilterExpression)) {
      return true;
    }
    for (String variable : resolvedVariables.keySet()) {
      String key = "\\$" + variable;
      String resolvedVariable = resolvedVariables.get(variable);
      try {
        regexpFilterText = regexpFilterText.replaceAll(key, resolvedVariable);
      } catch (IllegalArgumentException e) {
        throw new RuntimeException("Tried to replace " + key + " with " + resolvedVariable, e);
      }
    }
    boolean isMatching = Pattern.compile(regexpFilterExpression).matcher(regexpFilterText).find();
    if (!isMatching) {
      LOGGER.log(
          INFO,
          "Not triggering \""
              + regexpFilterExpression
              + "\" not matching \""
              + regexpFilterText
              + "\".");
    }
    return isMatching;
  }

  public List<GenericVariable> getGenericVariables() {
    return genericVariables;
  }

  public String getRegexpFilterExpression() {
    return regexpFilterExpression;
  }

  public List<GenericRequestVariable> getGenericRequestVariables() {
    return genericRequestVariables;
  }

  public String getRegexpFilterText() {
    return regexpFilterText;
  }

  @Override
  public String toString() {
    return "GenericTrigger [genericVariables="
        + genericVariables
        + ", regexpFilterText="
        + regexpFilterText
        + ", regexpFilterExpression="
        + regexpFilterExpression
        + ", genericRequestVariables="
        + genericRequestVariables
        + "]";
  }
}
