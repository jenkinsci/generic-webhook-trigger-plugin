package org.jenkinsci.plugins.gwt;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.kohsuke.stapler.DataBoundConstructor;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Item;
import hudson.model.ParameterValue;
import hudson.model.ParametersAction;
import hudson.model.StringParameterValue;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;

public class GenericTrigger extends Trigger<AbstractProject<?, ?>> {

  private List<GenericVariable> genericVariables = newArrayList();

  public static class GenericDescriptor extends TriggerDescriptor {

    @Override
    public boolean isApplicable(Item item) {
      return true;
    }

    @Override
    public String getDisplayName() {
      return "Generic Webhook Trigger";
    }
  }

  @DataBoundConstructor
  public GenericTrigger(List<GenericVariable> genericVariables) {
    this.genericVariables = genericVariables;
  }

  @Extension public static final GenericDescriptor DESCRIPTOR = new GenericDescriptor();

  public void trigger(String postContent) {
    Map<String, String> resolvedVariables =
        new VariablesResolver(postContent, genericVariables).getVariables();

    List<ParameterValue> parameterList = newArrayList();
    for (Entry<String, String> entry : resolvedVariables.entrySet()) {
      ParameterValue parameter = new StringParameterValue(entry.getKey(), entry.getValue());
      parameterList.add(parameter);
    }
    ParametersAction parameters = new ParametersAction(parameterList);

    GenericCause cause = new GenericCause(postContent, resolvedVariables);
    job.scheduleBuild2(0, cause, parameters);
  }

  public List<GenericVariable> getGenericVariables() {
    return genericVariables;
  }

  @Override
  public String toString() {
    String fullName = "";
    if (job != null) {
      fullName = job.getFullName();
    }
    return "GenericTrigger " + fullName;
  }
}
