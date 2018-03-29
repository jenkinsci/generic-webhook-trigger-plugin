package org.jenkinsci.plugins.gwt.bdd;

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.jenkinsci.plugins.gwt.ExpressionType;
import org.jenkinsci.plugins.gwt.GenericTrigger;
import org.jenkinsci.plugins.gwt.GenericVariable;
import org.jenkinsci.plugins.gwt.resolvers.VariablesResolver;

import com.google.gson.GsonBuilder;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class Stepdefs {

  private static Logger LOG = Logger.getLogger(Stepdefs.class.getSimpleName());
  private static FeatureState featureState;

  @Before
  public void before() {
    featureState = new FeatureState();
  }

  @Given("^received post content is:$")
  public void postContentReceived(final String postContent) {
    featureState.setPostContent(postContent);
  }

  @Given("^the following generic variables are configured:$")
  public void givenGenericVariables(final List<GenericVariablePojo> given) {
    for (final GenericVariablePojo from : given) {
      final GenericVariable to = new GenericVariable(from.getVariable(), from.getExpression());
      to.setDefaultValue(from.getDefaultValue());
      to.setRegexpFilter(from.getRegexpFilter());
      if (!isNullOrEmpty(from.getExpressionType())) {
        to.setExpressionType(ExpressionType.valueOf(from.getExpressionType()));
      }
      featureState.getGenericVariables().add(to);
    }
  }

  @Given("^filter is configured with:$")
  public void givenFilter(final List<GenericFilterPojo> given) {
    featureState.setRegexpFilterText(given.get(0).getText());
    featureState.setRegexpFilterExpression(given.get(0).getExpression());
  }

  @Given("^filter is configured with text: (.*)$")
  public void givenFilterFluentText(final String given) {
    featureState.setRegexpFilterText(given.trim());
  }

  @Given("^filter is configured with expression: (.*)$")
  public void givenFilterFluentExpression(final String given) {
    featureState.setRegexpFilterExpression(given.trim());
  }

  @Then("^variables are resolved to:$")
  public void variablesAreResolved(final List<GenericVariablesResolvedPojo> given) {
    final Map<String, String> resolvedVariables = getResolvedVariables();
    for (final GenericVariablesResolvedPojo expected : given) {
      String actual = "";
      if (resolvedVariables.containsKey(expected.getVariable())) {
        actual = resolvedVariables.get(expected.getVariable());
      }
      assertThat(actual) //
          .as(expected.getVariable()) //
          .isEqualTo(expected.getValue());
    }
  }

  @Then("^the job is triggered$")
  public void jobShouldBeTriggered() {
    assertThat(isMatching()) //
        .isTrue();
  }

  @Then("^the job is not triggered$")
  public void jobShouldNotBeTriggered() {
    assertThat(isMatching()) //
        .isFalse();
  }

  private boolean isMatching() {
    final Map<String, String> resolvedVariables = getResolvedVariables();

    final String renderedRegexpFilterText =
        GenericTrigger.renderText(featureState.getRegexpFilterText(), resolvedVariables);
    final boolean isMatching =
        GenericTrigger.isMatching(
            renderedRegexpFilterText, featureState.getRegexpFilterExpression());
    return isMatching;
  }

  private Map<String, String> getResolvedVariables() {
    final Map<String, String> resolvedVariables =
        new VariablesResolver(
                featureState.getHeaders(),
                featureState.getParameterMap(),
                featureState.getPostContent(),
                featureState.getGenericVariables(),
                featureState.getGenericRequestVariables(),
                featureState.getGenericHeaderVariables()) //
            .getVariables();
    LOG.info(
        "Resolved variables:\n "
            + new GsonBuilder().setPrettyPrinting().create().toJson(resolvedVariables));
    return resolvedVariables;
  }
}
