package org.jenkinsci.plugins.gwt;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.logging.Level.INFO;
import static java.util.regex.Pattern.compile;
import hudson.Extension;
import hudson.model.Item;
import hudson.model.ParameterValue;
import hudson.model.CauseAction;
import hudson.model.Job;
import hudson.model.ParameterDefinition;
import hudson.model.ParametersAction;
import hudson.model.ParametersDefinitionProperty;
import hudson.model.StringParameterValue;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import jenkins.model.ParameterizedJobMixIn;

import org.jenkinsci.Symbol;
import org.jenkinsci.plugins.gwt.resolvers.VariablesResolver;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import com.google.common.annotations.VisibleForTesting;

public class GenericTrigger extends Trigger<Job<?, ?>> {

  private static final Logger LOGGER = Logger.getLogger(GenericTrigger.class.getName());
  private List<GenericVariable> genericVariables = newArrayList();
  private final String regexpFilterText;
  private final String regexpFilterExpression;
  private List<GenericRequestVariable> genericRequestVariables = newArrayList();
  private List<GenericHeaderVariable> genericHeaderVariables = newArrayList();
  private boolean printPostContent;
  private boolean printContributedVariables;
  private String causeString;

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
    return causeString;
  }

  @DataBoundSetter
  public void setPrintContributedVariables(final boolean printContributedVariables) {
    this.printContributedVariables = printContributedVariables;
  }

  @DataBoundSetter
  public void setPrintPostContent(final boolean printPostContent) {
    this.printPostContent = printPostContent;
  }

  public boolean isPrintContributedVariables() {
    return printContributedVariables;
  }

  public boolean isPrintPostContent() {
    return printPostContent;
  }

  @Extension public static final GenericDescriptor DESCRIPTOR = new GenericDescriptor();

  @SuppressWarnings("static-access")
  public GenericTriggerResults trigger(
      final Map<String, List<String>> headers,
      final Map<String, String[]> parameterMap,
      final String postContent) {
    final Map<String, String> resolvedVariables =
        new VariablesResolver(
                headers,
                parameterMap,
                postContent,
                genericVariables,
                genericRequestVariables,
                genericHeaderVariables)
            .getVariables();

    final String renderedRegexpFilterText = renderText(regexpFilterText, resolvedVariables);
    final boolean isMatching = isMatching(renderedRegexpFilterText, regexpFilterExpression);

    hudson.model.Queue.Item item = null;
    if (isMatching) {
      final String cause = renderText(causeString, resolvedVariables);
      final GenericCause genericCause =
          new GenericCause(
              postContent, resolvedVariables, printContributedVariables, printPostContent, cause);

      final ParametersAction parameters = createParameters(job, resolvedVariables);
      item =
          retrieveScheduleJob(job) //
              .scheduleBuild2(job, 0, new CauseAction(genericCause), parameters);
    }
    return new GenericTriggerResults(
        item, resolvedVariables, renderedRegexpFilterText, regexpFilterExpression);
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

  private ParametersAction createParameters(
      final Job<?, ?> job, final Map<String, String> resolvedVariables) {
    final List<StringParameterValue> parameterList =
        getParametersFromParameterizedJob(job, resolvedVariables);
    final List<ParameterValue> parameterValueList = toParameterValueList(parameterList);
    return new ParametersAction(parameterValueList);
  }

  private List<ParameterValue> toParameterValueList(
      final List<StringParameterValue> parameterList) {
    final List<ParameterValue> t = new ArrayList<>();
    for (final StringParameterValue f : parameterList) {
      t.add(f);
    }
    return t;
  }

  /** To keep any default values set there. */
  private List<StringParameterValue> getParametersFromParameterizedJob(
      final Job<?, ?> job, final Map<String, String> resolvedVariables) {
    final List<StringParameterValue> parameterList = newArrayList();
    final ParametersDefinitionProperty parametersDefinitionProperty =
        job.getProperty(ParametersDefinitionProperty.class);
    if (parametersDefinitionProperty != null) {
      for (final ParameterDefinition parameterDefinition :
          parametersDefinitionProperty.getParameterDefinitions()) {
        final String param = parameterDefinition.getName();
        final ParameterValue defaultParameterValue = parameterDefinition.getDefaultParameterValue();
        if (defaultParameterValue != null) {
          String value = null;
          if (!isNullOrEmpty(resolvedVariables.get(param))) {
            value = resolvedVariables.get(param);
          } else {
            value = defaultParameterValue.getValue().toString();
          }
          if (!isNullOrEmpty(value)) {
            final StringParameterValue parameter = new StringParameterValue(param, value);
            parameterList.add(parameter);
          }
        }
      }
    }
    return parameterList;
  }

  @VisibleForTesting
  public static boolean isMatching(
      final String renderedRegexpFilterText, final String regexpFilterExpression) {
    final boolean noFilterConfigured =
        isNullOrEmpty(renderedRegexpFilterText) || isNullOrEmpty(regexpFilterExpression);
    if (noFilterConfigured) {
      return true;
    }
    final boolean isMatching =
        compile(regexpFilterExpression) //
            .matcher(renderedRegexpFilterText) //
            .find();
    if (!isMatching) {
      LOGGER.log(
          INFO,
          "Not triggering \""
              + regexpFilterExpression
              + "\" not matching \""
              + renderedRegexpFilterText
              + "\".");
    }
    return isMatching;
  }

  @VisibleForTesting
  public static String renderText(
      String regexpFilterText, final Map<String, String> resolvedVariables) {
    if (isNullOrEmpty(regexpFilterText)) {
      return "";
    }
    final List<String> variables = getVariablesInResolveOrder(resolvedVariables.keySet());
    for (final String variable : variables) {
      final String key = "\\$" + variable;
      final String resolvedVariable = resolvedVariables.get(variable);
      try {
        regexpFilterText =
            regexpFilterText //
                .replaceAll(key, resolvedVariable);
      } catch (final IllegalArgumentException e) {
        throw new RuntimeException("Tried to replace " + key + " with " + resolvedVariable, e);
      }
    }
    return regexpFilterText;
  }

  @VisibleForTesting
  static List<String> getVariablesInResolveOrder(final Set<String> unsorted) {
    final List<String> variables = new ArrayList<>(unsorted);
    Collections.sort(
        variables,
        new Comparator<String>() {
          @Override
          public int compare(final String o1, final String o2) {
            if (o1.length() == o2.length()) {
              return o1.compareTo(o2);
            } else if (o1.length() > o2.length()) {
              return -1;
            }
            return 1;
          }
        });
    return variables;
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

  public List<GenericHeaderVariable> getGenericHeaderVariables() {
    return genericHeaderVariables;
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
        + ", genericHeaderVariables="
        + genericHeaderVariables
        + "]";
  }
}
