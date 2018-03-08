package org.jenkinsci.plugins.gwt;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
  public void testThatIsMatchingWorksDevelopCorrectUser() {
    regexpFilterText = "refs/heads/develop tomabje";
    regexpFilterExpression = "^refs/heads/develop ((?!jenkins))";
    final boolean actual = sut.isMatching(regexpFilterText, regexpFilterExpression);

    assertThat(actual) //
        .isTrue();
  }

  @Test
  public void testThatIsMatchingWorksDevelopNotCorrectUser() {
    regexpFilterText = "jenkins refs/heads/develop";
    regexpFilterExpression = "^((?!jenkins)) refs/heads/develop$";
    final boolean actual = sut.isMatching(regexpFilterText, regexpFilterExpression);

    assertThat(actual) //
        .isFalse();
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
  public void testThatVariablesAreResolvedWithLongestVariableFirst() {
    resolvedVariables = new TreeMap<>();
    resolvedVariables.put("key1", "resolved1");
    resolvedVariables.put("key2", "resolved2only");
    resolvedVariables.put("key2andthensome", "resolved2andmore");

    final String text = "$key1 $key2 $key2andthensome $key2";
    final String actual = sut.renderText(text, resolvedVariables);

    assertThat(actual) //
        .isEqualTo("resolved1 resolved2only resolved2andmore resolved2only");
  }

  @Test
  public void testThatVariablesAreResolvedInOrder() {
    final List<String> actual =
        sut.getVariablesInResolveOrder(
            newHashSet( //
                "var1", //
                "var2", //
                "var2andmore", //
                "var31", //
                "var3a", //
                "var3b" //
                ));

    assertThat(actual) //
        .containsExactly( //
            "var2andmore", //
            "var31", //
            "var3a", //
            "var3b", //
            "var1", //
            "var2" //
            );
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
