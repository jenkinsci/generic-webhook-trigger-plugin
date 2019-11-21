package org.jenkinsci.plugins.gwt;

import java.util.Map;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.json.JsonHttpResponse;

public class GenericResponse {

  static HttpResponse jsonResponse(
      final int httpStatusCode, final String message, final Map<String, Object> jobs) {
    final GenericWebhookResponse genericResponse = new GenericWebhookResponse(message, jobs);
    return new JsonHttpResponse(JSONObject.fromObject(genericResponse), httpStatusCode);
  }

  static HttpResponse jsonResponse(final int httpStatusCode, final String message) {
    final GenericWebhookResponse genericResponse = new GenericWebhookResponse(message);
    return new JsonHttpResponse(JSONObject.fromObject(genericResponse), httpStatusCode);
  }
}
