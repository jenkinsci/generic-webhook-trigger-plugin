package org.jenkinsci.plugins.gwt;

import static com.google.common.collect.Maps.newHashMap;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class GenericTriggerTest {
  private String regexpFilterText = null;
  private String regexpFilterExpression = null;
  private Map<String, String> resolvedVariables;
  private GenericTrigger sut;

  @Before
  public void before() {
    resolvedVariables = newHashMap();
    resolvedVariables.put("key1", "resolved1");
    resolvedVariables.put("key2", "resolved2");
    this.sut = new GenericTrigger(null, null, null, null, null);
  }

  @Test
  public void testThatIsMatchingAcceptsEverythingIfNoFilter() {
    final boolean actual = sut.isMatching(regexpFilterText, regexpFilterExpression);

    assertThat(actual) //
        .isTrue();
  }

  @Test
  public void testThatIsMatchingAcceptsIfMatching() {
    regexpFilterText = "$key1";
    regexpFilterExpression = "resolved1";

    final boolean actual =
        sut.isMatching(sut.renderText(regexpFilterText, resolvedVariables), regexpFilterExpression);

    assertThat(actual) //
        .isTrue();
  }

  @Test
  public void testThatIsMatchingRejectsIfNotMatching() {

    regexpFilterText = "resolved2";
    regexpFilterExpression = "$key1";
    final boolean actual = sut.isMatching(regexpFilterText, regexpFilterExpression);

    assertThat(actual) //
        .isFalse();
  }
}
