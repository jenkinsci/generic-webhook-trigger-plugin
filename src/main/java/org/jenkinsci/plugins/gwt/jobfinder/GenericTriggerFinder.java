package org.jenkinsci.plugins.gwt.jobfinder;

import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;
import java.util.Map;
import org.jenkinsci.plugins.gwt.GenericTrigger;

public class GenericTriggerFinder {
  public static GenericTrigger findGenericTrigger(
      final Map<TriggerDescriptor, Trigger<?>> triggers) {
    if (triggers == null) {
      return null;
    }
    for (final Trigger<?> candidate : triggers.values()) {
      if (candidate instanceof GenericTrigger) {
        return (GenericTrigger) candidate;
      }
    }
    return null;
  }
}
