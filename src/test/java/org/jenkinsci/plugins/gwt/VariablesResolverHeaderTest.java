package org.jenkinsci.plugins.gwt;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jenkinsci.plugins.gwt.resolvers.VariablesResolver;
import org.junit.Test;

public class VariablesResolverHeaderTest {

  @Test
  public void testHeadersAndRequestParameters() throws Exception {
    String postContent = null;

    List<GenericVariable> genericVariables = newArrayList();

    Map<String, String[]> parameterMap = new HashMap<>();
    List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
    genericRequestVariables.add(new GenericRequestVariable("reqp1", "[^0-9]"));
    genericRequestVariables.add(new GenericRequestVariable("reqp3", "[^a-z]"));
    genericRequestVariables.add(new GenericRequestVariable("reqp4", ""));

    Map<String, Enumeration<String>> headers = new HashMap<>();
    headers.put("someparam", enumeration("some value"));
    headers.put("anotherparam", enumeration("another value", "even more"));
    headers.put("content-type", enumeration("application/json"));
    headers.put("param_not_mapped", enumeration("do not include"));
    List<GenericHeaderVariable> genericHeaderVariables = new ArrayList<>();
    genericHeaderVariables.add(new GenericHeaderVariable("someparam", ""));
    genericHeaderVariables.add(new GenericHeaderVariable("anotherparam", "[^e]"));
    genericHeaderVariables.add(new GenericHeaderVariable("content-type", ""));
    Map<String, String> variables =
        new VariablesResolver(
                headers,
                parameterMap,
                postContent,
                genericVariables,
                genericRequestVariables,
                genericHeaderVariables)
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
  public void testHeaders() throws Exception {
    String postContent = null;

    List<GenericVariable> genericVariables = newArrayList();

    Map<String, String[]> parameterMap = new HashMap<>();
    List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();

    Map<String, Enumeration<String>> headers = new HashMap<>();
    headers.put("someparam", enumeration("some value"));
    headers.put("anotherparam", enumeration("another value", "even more"));
    headers.put("content-type", enumeration("application/json"));
    headers.put("param_not_mapped", enumeration("do not include"));

    List<GenericHeaderVariable> genericHeaderVariables = new ArrayList<>();
    genericHeaderVariables.add(new GenericHeaderVariable("someparam", ""));
    genericHeaderVariables.add(new GenericHeaderVariable("anotherparam", "[^e]"));
    genericHeaderVariables.add(new GenericHeaderVariable("content-type", ""));
    Map<String, String> variables =
        new VariablesResolver(
                headers,
                parameterMap,
                postContent,
                genericVariables,
                genericRequestVariables,
                genericHeaderVariables)
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
  public void testHeaderResolvesToNull() throws Exception {
    String postContent = null;

    List<GenericVariable> genericVariables = newArrayList();

    Map<String, String[]> parameterMap = new HashMap<>();
    List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();

    Map<String, Enumeration<String>> headers = new HashMap<>();
    headers.put("someparam", enumeration("some value"));

    List<GenericHeaderVariable> genericHeaderVariables = new ArrayList<>();
    genericHeaderVariables.add(new GenericHeaderVariable("someparam", null));
    Map<String, String> variables =
        new VariablesResolver(
                headers,
                parameterMap,
                postContent,
                genericVariables,
                genericRequestVariables,
                genericHeaderVariables)
            .getVariables();

    assertThat(variables) //
        .containsEntry("someparam", "some value") //
        .hasSize(2);
  }

  @Test
  public void testHeaderResolvesCanBeReused() throws Exception {
    String postContent = null;

    List<GenericVariable> genericVariables = newArrayList();

    Map<String, String[]> parameterMap = new HashMap<>();
    List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();

    Map<String, Enumeration<String>> headers = new HashMap<>();
    headers.put("someparam", enumeration("some value"));

    List<GenericHeaderVariable> genericHeaderVariables = new ArrayList<>();
    genericHeaderVariables.add(new GenericHeaderVariable("someparam", null));
    Map<String, String> variables =
        new VariablesResolver(
                headers,
                parameterMap,
                postContent,
                genericVariables,
                genericRequestVariables,
                genericHeaderVariables)
            .getVariables();

    assertThat(variables) //
        .containsEntry("someparam", "some value") //
        .hasSize(2);

    variables =
        new VariablesResolver(
                headers,
                parameterMap,
                postContent,
                genericVariables,
                genericRequestVariables,
                genericHeaderVariables)
            .getVariables();

    assertThat(variables) //
        .containsEntry("someparam", "some value") //
        .hasSize(2);
  }

  @Test
  public void testHeadersButNoneConfigured() throws Exception {
    String postContent = null;

    List<GenericVariable> genericVariables = newArrayList();

    Map<String, String[]> parameterMap = new HashMap<>();
    List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();

    Map<String, Enumeration<String>> headers = new HashMap<>();
    headers.put("someparam", enumeration("some value"));
    headers.put("anotherparam", enumeration("another value", "even more"));
    headers.put("content-type", enumeration("application/json"));
    headers.put("param_not_mapped", enumeration("do not include"));

    List<GenericHeaderVariable> genericHeaderVariables = new ArrayList<>();
    Map<String, String> variables =
        new VariablesResolver(
                headers,
                parameterMap,
                postContent,
                genericVariables,
                genericRequestVariables,
                genericHeaderVariables)
            .getVariables();

    assertThat(variables) //
        .hasSize(0);
  }

  private Enumeration<String> enumeration(final String... string) {
    return new Enumeration<String>() {
      private int i = 0;

      @Override
      public String nextElement() {
        return string[i++];
      }

      @Override
      public boolean hasMoreElements() {
        return i < string.length;
      }
    };
  }
}
