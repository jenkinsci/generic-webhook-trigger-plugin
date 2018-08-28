package org.jenkinsci.plugins.gwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import hudson.model.Queue.Item;
import java.util.Map;
import org.junit.Test;

public class GenericTriggerResultsTest {
  @Test
  public void testNull() {
    final Item item = null;
    final Map<String, String> resolvedVariables = null;
    final String regexpFilterText = null;
    final String regexpFilterExpression = null;

    final GenericTriggerResults sut =
        new GenericTriggerResults(
            item, resolvedVariables, regexpFilterText, regexpFilterExpression);

    assertThat(sut) //
        .isNotNull();
  }

  @Test
  public void testNullApi() {
    final Item item = mock(Item.class);
    when(item.getId()) //
        .thenReturn(2L);
    when(item.getApi()) //
        .thenReturn(null);
    final Map<String, String> resolvedVariables = null;
    final String regexpFilterText = null;
    final String regexpFilterExpression = null;

    final GenericTriggerResults sut =
        new GenericTriggerResults(
            item, resolvedVariables, regexpFilterText, regexpFilterExpression);

    assertThat(sut) //
        .isNotNull();
    assertThat(sut.getId()) //
        .isEqualTo(2L);
  }
}
