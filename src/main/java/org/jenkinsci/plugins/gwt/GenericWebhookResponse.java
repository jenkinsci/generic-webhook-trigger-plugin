package org.jenkinsci.plugins.gwt;

import java.util.Map;

public class GenericWebhookResponse {

  private final String message;
  private Map<String, Object> jobs;

  public GenericWebhookResponse(final String message) {
    this.message = message;
  }

  public GenericWebhookResponse(final String message, final Map<String, Object> jobs) {
    this.message = message;
    this.jobs = jobs;
  }

  public String getMessage() {
    return message;
  }

  public Map<String, Object> getJobs() {
    return jobs;
  }
}
