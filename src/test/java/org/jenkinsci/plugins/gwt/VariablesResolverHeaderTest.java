package org.jenkinsci.plugins.gwt;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jenkinsci.plugins.gwt.resolvers.VariablesResolver;
import org.junit.jupiter.api.Test;

class VariablesResolverHeaderTest {

    @Test
    void testHeadersAndRequestParameters() {
        final String postContent = null;

        final List<GenericVariable> genericVariables = newArrayList();

        final Map<String, String[]> parameterMap = new HashMap<>();
        final List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
        genericRequestVariables.add(new GenericRequestVariable("reqp1", "[^0-9]"));
        genericRequestVariables.add(new GenericRequestVariable("reqp3", "[^a-z]"));
        genericRequestVariables.add(new GenericRequestVariable("reqp4", ""));

        final Map<String, List<String>> headers = new HashMap<>();
        headers.put("someparam", newArrayList("some value"));
        headers.put("anotherparam", newArrayList("another value", "even more"));
        headers.put("content-type", newArrayList("application/json"));
        headers.put("param_not_mapped", newArrayList("do not include"));
        final List<GenericHeaderVariable> genericHeaderVariables = new ArrayList<>();
        genericHeaderVariables.add(new GenericHeaderVariable("someparam", ""));
        genericHeaderVariables.add(new GenericHeaderVariable("anotherparam", "[^e]"));
        genericHeaderVariables.add(new GenericHeaderVariable("content-type", ""));
        final boolean shouldNotFlatten = false;
        final Map<String, String> variables = new VariablesResolver(
                        headers,
                        parameterMap,
                        postContent,
                        genericVariables,
                        genericRequestVariables,
                        genericHeaderVariables,
                        shouldNotFlatten)
                .getVariables();

        assertThat(variables) //
                .containsEntry("someparam", "some value") //
                .containsEntry("someparam_0", "some value") //
                .containsEntry("anotherparam", "ee") //
                .containsEntry("anotherparam_0", "ee") //
                .containsEntry("anotherparam_1", "eee") //
                .containsEntry("content_type", "application/json") //
                .hasSize(7);
    }

    @Test
    void testHeaders() {
        final String postContent = null;

        final List<GenericVariable> genericVariables = newArrayList();

        final Map<String, String[]> parameterMap = new HashMap<>();
        final List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();

        final Map<String, List<String>> headers = new HashMap<>();
        headers.put("Someparam", newArrayList("some value"));
        headers.put("anotherparam", newArrayList("another value", "even more"));
        headers.put("content-Type", newArrayList("application/json"));
        headers.put("param_not_mapped", newArrayList("do not include"));

        final List<GenericHeaderVariable> genericHeaderVariables = new ArrayList<>();
        genericHeaderVariables.add(new GenericHeaderVariable("someparam", ""));
        genericHeaderVariables.add(new GenericHeaderVariable("anotherparam", "[^e]"));
        genericHeaderVariables.add(new GenericHeaderVariable("content-type", ""));
        final boolean shouldNotFlatten = false;
        final Map<String, String> variables = new VariablesResolver(
                        headers,
                        parameterMap,
                        postContent,
                        genericVariables,
                        genericRequestVariables,
                        genericHeaderVariables,
                        shouldNotFlatten)
                .getVariables();

        assertThat(variables) //
                .containsEntry("someparam", "some value") //
                .containsEntry("someparam_0", "some value") //
                .containsEntry("anotherparam", "ee") //
                .containsEntry("anotherparam_0", "ee") //
                .containsEntry("anotherparam_1", "eee") //
                .containsEntry("content_type", "application/json") //
                .hasSize(7);
    }

    @Test
    void testHeaderResolvesToNull() {
        final String postContent = null;

        final List<GenericVariable> genericVariables = newArrayList();

        final Map<String, String[]> parameterMap = new HashMap<>();
        final List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();

        final Map<String, List<String>> headers = new HashMap<>();
        headers.put("someparam", newArrayList("some value"));

        final List<GenericHeaderVariable> genericHeaderVariables = new ArrayList<>();
        genericHeaderVariables.add(new GenericHeaderVariable("someparam", null));
        final boolean shouldNotFlatten = false;
        final Map<String, String> variables = new VariablesResolver(
                        headers,
                        parameterMap,
                        postContent,
                        genericVariables,
                        genericRequestVariables,
                        genericHeaderVariables,
                        shouldNotFlatten)
                .getVariables();

        assertThat(variables) //
                .containsEntry("someparam", "some value") //
                .hasSize(2);
    }

    @Test
    void testHeaderResolvesCanBeReused() {
        final String postContent = null;

        final List<GenericVariable> genericVariables = newArrayList();

        final Map<String, String[]> parameterMap = new HashMap<>();
        final List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();

        final Map<String, List<String>> headers = new HashMap<>();
        headers.put("someparam", newArrayList("some value"));

        final List<GenericHeaderVariable> genericHeaderVariables = new ArrayList<>();
        genericHeaderVariables.add(new GenericHeaderVariable("someparam", null));
        final boolean shouldNotFlatten = false;
        Map<String, String> variables = new VariablesResolver(
                        headers,
                        parameterMap,
                        postContent,
                        genericVariables,
                        genericRequestVariables,
                        genericHeaderVariables,
                        shouldNotFlatten)
                .getVariables();

        assertThat(variables) //
                .containsEntry("someparam", "some value") //
                .hasSize(2);

        variables = new VariablesResolver(
                        headers,
                        parameterMap,
                        postContent,
                        genericVariables,
                        genericRequestVariables,
                        genericHeaderVariables,
                        shouldNotFlatten)
                .getVariables();

        assertThat(variables) //
                .containsEntry("someparam", "some value") //
                .hasSize(2);
    }

    @Test
    void testHeadersButNoneConfigured() {
        final String postContent = null;

        final List<GenericVariable> genericVariables = newArrayList();

        final Map<String, String[]> parameterMap = new HashMap<>();
        final List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();

        final Map<String, List<String>> headers = new HashMap<>();
        headers.put("someparam", newArrayList("some value"));
        headers.put("anotherparam", newArrayList("another value", "even more"));
        headers.put("content-type", newArrayList("application/json"));
        headers.put("param_not_mapped", newArrayList("do not include"));

        final List<GenericHeaderVariable> genericHeaderVariables = new ArrayList<>();
        final boolean shouldNotFlatten = false;
        final Map<String, String> variables = new VariablesResolver(
                        headers,
                        parameterMap,
                        postContent,
                        genericVariables,
                        genericRequestVariables,
                        genericHeaderVariables,
                        shouldNotFlatten)
                .getVariables();

        assertThat(variables) //
                .isEmpty();
    }
}
