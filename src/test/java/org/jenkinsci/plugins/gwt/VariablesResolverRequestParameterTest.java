package org.jenkinsci.plugins.gwt;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jenkinsci.plugins.gwt.resolvers.VariablesResolver;
import org.junit.Test;

public class VariablesResolverRequestParameterTest {
  private final Map<String, List<String>> headers = new HashMap<>();
  private final List<GenericHeaderVariable> genericHeaderVariables = new ArrayList<>();

  @Test
  public void testGenericRequestParametersWithFilters() throws Exception {
    final String postContent = null;

    final List<GenericVariable> genericVariables = newArrayList();

    final Map<String, String[]> parameterMap = new HashMap<>();
    final String[] values1 = new String[] {"abc123456cdef", "ABCdef"};
    parameterMap.put("reqp1", values1);

    final String[] values2 = new String[] {"this one will be ignored"};
    parameterMap.put("reqp2", values2);

    final String[] values3 = new String[] {"just one"};
    parameterMap.put("reqp3", values3);

    final String[] values4 = new String[] {"just one", "just one again"};
    parameterMap.put("reqp4", values4);

    final List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
    genericRequestVariables.add(new GenericRequestVariable("reqp1", "[^0-9]"));
    genericRequestVariables.add(new GenericRequestVariable("reqp3", "[^a-z]"));
    genericRequestVariables.add(new GenericRequestVariable("reqp4", ""));

    final Map<String, String> variables =
        new VariablesResolver(
                headers,
                parameterMap,
                postContent,
                genericVariables,
                genericRequestVariables,
                genericHeaderVariables)
            .getVariables();

    assertThat(variables) //
        .containsEntry("reqp1_0", "123456") //
        .containsEntry("reqp1_1", "") //
        .containsEntry("reqp3", "justone") //
        .containsEntry("reqp3_0", "justone") //
        .containsEntry("reqp4_0", "just one") //
        .containsEntry("reqp4_1", "just one again") //
        .hasSize(6);
  }

  @Test
  public void testGenericRequestParameters() throws Exception {
    final String postContent = null;

    final List<GenericVariable> genericVariables = newArrayList();

    final Map<String, String[]> parameterMap = new HashMap<>();
    final String[] values1 = new String[] {"p1value"};
    parameterMap.put("param1", values1);

    final String[] values2 = new String[] {"p2value"};
    parameterMap.put("param2", values2);

    final List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
    genericRequestVariables.add(new GenericRequestVariable("param1", ""));
    genericRequestVariables.add(new GenericRequestVariable("param2", ""));

    final Map<String, String> variables =
        new VariablesResolver(
                headers,
                parameterMap,
                postContent,
                genericVariables,
                genericRequestVariables,
                genericHeaderVariables)
            .getVariables();

    assertThat(variables) //
        .hasSize(4) //
        .containsEntry("param1", "p1value") //
        .containsEntry("param1_0", "p1value") //
        .containsEntry("param2", "p2value") //
        .containsEntry("param2_0", "p2value");
  }
}
