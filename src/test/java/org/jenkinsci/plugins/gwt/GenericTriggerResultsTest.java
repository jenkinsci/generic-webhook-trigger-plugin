package org.jenkinsci.plugins.gwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.Test;

public class GenericTriggerResultsTest {
  @Test
  public void testNull() {
    final String url = null;
    final long id = 0;
    final boolean triggered = false;
    final Map<String, String> resolvedVariables = null;
    final String regexpFilterText = null;
    final String regexpFilterExpression = null;

    final GenericTriggerResults sut =
        new GenericTriggerResults(
            url, id, triggered, resolvedVariables, regexpFilterText, regexpFilterExpression);

    assertThat(sut) //
        .isNotNull();
  }

  @Test
  public void testNullApi() {
    final String url = null;
    final long id = 2L;
    final boolean triggered = false;
    final Map<String, String> resolvedVariables = null;
    final String regexpFilterText = null;
    final String regexpFilterExpression = null;

    final GenericTriggerResults sut =
        new GenericTriggerResults(
            url, id, triggered, resolvedVariables, regexpFilterText, regexpFilterExpression);

    assertThat(sut) //
        .isNotNull();
    assertThat(sut.getId()) //
        .isEqualTo(2L);
  }
}
