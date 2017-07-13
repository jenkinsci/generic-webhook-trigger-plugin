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
  public void testGenericRequestParameters() throws Exception {
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
    headers.put("param_not_mapped", enumeration("do not include"));
    List<GenericHeaderVariable> genericHeaderVariables = new ArrayList<>();
    genericHeaderVariables.add(new GenericHeaderVariable("someparam", ""));
    genericHeaderVariables.add(new GenericHeaderVariable("anotherparam", "[^e]"));
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
        .containsEntry("someparam_0", "some value") //
        .containsEntry("anotherparam_1", "eee") //
        .containsEntry("anotherparam_0", "ee") //
        .hasSize(3);
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
