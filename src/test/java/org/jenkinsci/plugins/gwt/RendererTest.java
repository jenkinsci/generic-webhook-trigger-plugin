package org.jenkinsci.plugins.gwt;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.jenkinsci.plugins.gwt.Renderer.getVariablesInResolveOrder;
import static org.jenkinsci.plugins.gwt.Renderer.isMatching;
import static org.jenkinsci.plugins.gwt.Renderer.renderText;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RendererTest {
    private String regexpFilterText = null;
    private String regexpFilterExpression = null;
    private Map<String, String> resolvedVariables;

    @BeforeEach
    void before() {
        resolvedVariables = newHashMap();
        resolvedVariables.put("key1", "resolved1");
        resolvedVariables.put("key2", "resolved2");
    }

    @Test
    void testThatIsMatchingAcceptsEverythingIfNoFilter() {
        final boolean actual = isMatching(regexpFilterText, regexpFilterExpression);

        assertThat(actual) //
                .isTrue();
    }

    @Test
    void testThatEmptyTextIsNotMatched() {
        final boolean actual = isMatching("", "^feature");

        assertThat(actual) //
                .isFalse();
    }

    @Test
    void testThatEmptyExprButNotEmptyTextIsNotMatched() {
        assertThat(isMatching(null, "^feature")) //
                .isFalse();
        assertThat(isMatching("", "^feature")) //
                .isFalse();
    }

    @Test
    void testThatEmptyExprAndEmptyTextIsMatched() {
        assertThat(isMatching(null, "")) //
                .isTrue();
        assertThat(isMatching("", null)) //
                .isTrue();
        assertThat(isMatching(null, null)) //
                .isTrue();
        assertThat(isMatching("", "")) //
                .isTrue();
    }

    @Test
    void testThatIsMatchingWorksWithNewlines() {
        regexpFilterText = "firstsecondthird";
        regexpFilterExpression = "^(?!.*(second)).*";
        final boolean actual = isMatching(regexpFilterText, regexpFilterExpression);

        assertThat(actual) //
                .isFalse();
    }

    @Test
    void testThatIsMatchingWorksDevelopCorrectUser() {
        regexpFilterText = "refs/heads/develop tomabje";
        regexpFilterExpression = "^refs/heads/develop ((?!jenkins))";
        final boolean actual = isMatching(regexpFilterText, regexpFilterExpression);

        assertThat(actual) //
                .isTrue();
    }

    @Test
    void testThatIsMatchingWorksDevelopNotCorrectUser() {
        regexpFilterText = "refs/heads/develop jenkins";
        regexpFilterExpression = "^refs/heads/develop ((?!jenkins))";
        final boolean actual = isMatching(regexpFilterText, regexpFilterExpression);

        assertThat(actual) //
                .isFalse();
    }

    @Test
    void testThatIsMatchingAcceptsIfMatching() {
        regexpFilterText = "$key1";
        regexpFilterExpression = "resolved1";

        final boolean actual = isMatching(renderText(regexpFilterText, resolvedVariables), regexpFilterExpression);

        assertThat(actual) //
                .isTrue();
    }

    @Test
    void testThatExactValueOfSingleVariableCanBeMatched() {
        resolvedVariables = new TreeMap<>();
        resolvedVariables.put("PR_TO_BRANCH", "master");

        final String text = "$PR_TO_BRANCH";

        final String actualRendered = renderText(text, resolvedVariables);
        assertThat(actualRendered) //
                .isEqualTo("master");

        regexpFilterExpression = "^master$";
        final boolean actual = isMatching(actualRendered, regexpFilterExpression);
        assertThat(actual) //
                .isTrue();
    }

    @Test
    void testThatVariablesAreResolvedWithLongestVariableFirst() {
        resolvedVariables = new TreeMap<>();
        resolvedVariables.put("key1", "resolved1");
        resolvedVariables.put("key2", "resolved2only");
        resolvedVariables.put("key2andthensome", "resolved2andmore");

        final String text = "$key1 $key2 $key2andthensome $key2";
        final String actual = renderText(text, resolvedVariables);

        assertThat(actual) //
                .isEqualTo("resolved1 resolved2only resolved2andmore resolved2only");
    }

    @Test
    void testThatExceptionNotThrownWithBadResolvedVariable() {
        resolvedVariables = new TreeMap<>();
        resolvedVariables.put("key1", "resolved1");
        resolvedVariables.put("key2", "resolved2($this)");

        final String text = "$key1 $key2";
        final String actual = renderText(text, resolvedVariables);

        assertThat(actual) //
                .isEqualTo("resolved1 resolved2($this)");
    }

    @Test
    void testThatVariablesCanUseBrackets() {
        resolvedVariables = new TreeMap<>();
        resolvedVariables.put("key1", "resolved1");
        resolvedVariables.put("key2", "resolved2only");
        resolvedVariables.put("key2andthensome", "resolved2andmore");

        final String text = "${key1} $key2 ${key2andthensome} $key2";
        final String actual = renderText(text, resolvedVariables);

        assertThat(actual) //
                .isEqualTo("resolved1 resolved2only resolved2andmore resolved2only");
    }

    @Test
    void testThatVariablesAreResolvedInOrder() {
        final List<String> actual = getVariablesInResolveOrder(
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
    void testThatIsMatchingRejectsIfNotMatching() {
        regexpFilterText = "resolved2";
        regexpFilterExpression = "$key1";
        final boolean actual = isMatching(regexpFilterText, regexpFilterExpression);

        assertThat(actual) //
                .isFalse();
    }
}
