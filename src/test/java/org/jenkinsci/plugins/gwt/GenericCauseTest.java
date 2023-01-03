package org.jenkinsci.plugins.gwt;

import static org.assertj.core.api.Assertions.assertThat;

import hudson.model.Cause;
import org.junit.Test;

public class GenericCauseTest {

  @Test
  public void sanitizeCauseStringTest() {

    Cause cause =
        new GenericCause(
            null,
            null,
            false,
            false,
            "<b>Triggered by:</b> <a href=\"https://test.org/pr/1\">PR 1</a>");

    String expected =
        "<b>Triggered by:</b> <a href=\"https://test.org/pr/1\" rel=\"nofollow\">PR 1</a>";

    assertThat(expected).isEqualTo(cause.getShortDescription());

    cause =
        new GenericCause(
            null,
            null,
            false,
            false,
            "<b>Triggered by:</b> PR 1<div><script>somethingBad()</script></div>");
    expected = "<b>Triggered by:</b> PR 1";

    assertThat(expected).isEqualTo(cause.getShortDescription());
  }
}
