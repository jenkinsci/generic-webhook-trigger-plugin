package org.jenkinsci.plugins.gwt.bdd;

import static com.google.common.base.Strings.isNullOrEmpty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.jenkinsci.plugins.gwt.Renderer.renderText;

import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.jenkinsci.plugins.gwt.ExpressionType;
import org.jenkinsci.plugins.gwt.GenericVariable;
import org.jenkinsci.plugins.gwt.Renderer;
import org.jenkinsci.plugins.gwt.resolvers.VariablesResolver;

public class Stepdefs {

    private static Logger LOG = Logger.getLogger(Stepdefs.class.getSimpleName());
    private static FeatureState featureState;

    @Before
    public void before() {
        featureState = new FeatureState();
    }

    @When("^received post content is:$")
    public void postContentReceived(final String postContent) {
        featureState.setPostContent(postContent);
    }

    @DataTableType
    public GenericVariablePojo genericVariablePojo(final Map<String, String> entry) {
        return new GenericVariablePojo(
                Strings.nullToEmpty(entry.get("variable")),
                Strings.nullToEmpty(entry.get("expression")),
                Strings.nullToEmpty(entry.get("expressionType")),
                Strings.nullToEmpty(entry.get("defaultValue")),
                Strings.nullToEmpty(entry.get("regexpFilter")));
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
            featureState //
                    .getGenericVariables() //
                    .add(to);
        }
    }

    @Given("^variable ([a-z]+?) has regexpFilter: (.*)$")
    public void givenGenericVariables(final String variable, final String regexpFilter) {
        for (final GenericVariable gv : featureState.getGenericVariables()) {
            if (gv.getVariableName().equals(variable)) {
                gv.setRegexpFilter(regexpFilter);
            }
        }
    }

    @DataTableType
    public GenericFilterPojo genericFilterPojo(final Map<String, String> entry) {
        final GenericFilterPojo it = new GenericFilterPojo();
        it.setExpression(Strings.nullToEmpty(entry.get("expression")));
        it.setText(Strings.nullToEmpty(entry.get("text")));
        return it;
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

    @Given("^should not flatten is checked$")
    public void givenShouldNotFlatten() {
        featureState.setShouldNotFlatten(true);
    }

    @DataTableType
    public GenericVariablesResolvedPojo gnericVariablesResolvedPojo(final Map<String, String> entry) {
        final GenericVariablesResolvedPojo it = new GenericVariablesResolvedPojo();
        it.setVariable(Strings.nullToEmpty(entry.get("variable")));
        it.setValue(Strings.nullToEmpty(entry.get("value")));
        return it;
    }

    @Then("^variables are resolved to:$")
    public void variablesAreResolved(final List<GenericVariablesResolvedPojo> given) {
        final Map<String, String> resolvedVariables = this.getResolvedVariables();
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

    @Then("^variable ([a-z]+?) is resolved to:$")
    public void variableIsResolvedTo(final String variable, final String expected) {
        assertThat(this.getResolvedVariables().get(variable)) //
                .as(variable) //
                .isEqualTo(expected);
    }

    @Then("^the job is triggered$")
    public void jobShouldBeTriggered() {
        this.isMatching(true);
    }

    @Then("^the job is not triggered$")
    public void jobShouldNotBeTriggered() {
        this.isMatching(false);
    }

    @Then("^filter text is rendered to: (.*)$")
    public void filterTextIsRenderedTo(final String given) {
        assertThat(this.renderedText(this.getResolvedVariables())).isEqualTo(given);
    }

    private boolean isMatching(final boolean expected) {
        final Map<String, String> resolvedVariables = this.getResolvedVariables();

        final String renderedRegexpFilterText = this.renderedText(resolvedVariables);
        final boolean isMatching =
                Renderer.isMatching(renderedRegexpFilterText, featureState.getRegexpFilterExpression());
        if (!isMatching && expected) {
            fail("Text: \""
                    + renderedRegexpFilterText
                    + "\" does not match \""
                    + featureState.getRegexpFilterExpression()
                    + "\"");
        }
        if (isMatching && !expected) {
            fail("Text: \""
                    + renderedRegexpFilterText
                    + "\" does match \""
                    + featureState.getRegexpFilterExpression()
                    + "\"");
        }
        return isMatching;
    }

    private String renderedText(final Map<String, String> resolvedVariables) {
        return renderText(featureState.getRegexpFilterText(), resolvedVariables);
    }

    private Map<String, String> getResolvedVariables() {
        final Map<String, String> resolvedVariables = new VariablesResolver(
                        featureState.getHeaders(),
                        featureState.getParameterMap(),
                        featureState.getPostContent(),
                        featureState.getGenericVariables(),
                        featureState.getGenericRequestVariables(),
                        featureState.getGenericHeaderVariables(),
                        featureState.getShouldNotFlatten()) //
                .getVariables();
        LOG.info("Resolved variables:\n "
                + new GsonBuilder().setPrettyPrinting().create().toJson(resolvedVariables));
        return resolvedVariables;
    }
}
