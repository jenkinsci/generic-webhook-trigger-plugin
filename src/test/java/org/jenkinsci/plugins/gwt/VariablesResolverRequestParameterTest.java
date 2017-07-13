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

public class VariablesResolverRequestParameterTest {
  private final Map<String, Enumeration<String>> headers = new HashMap<>();
  private final List<GenericHeaderVariable> genericHeaderVariables = new ArrayList<>();

  @Test
  public void testGenericRequestParameters() throws Exception {
    String postContent = null;

    List<GenericVariable> genericVariables = newArrayList();

    Map<String, String[]> parameterMap = new HashMap<>();
    String[] values1 = new String[] {"abc123456cdef", "ABCdef"};
    parameterMap.put("reqp1", values1);

    String[] values2 = new String[] {"this one will be ignored"};
    parameterMap.put("reqp2", values2);

    String[] values3 = new String[] {"just one"};
    parameterMap.put("reqp3", values3);

    String[] values4 = new String[] {"just one", "just one again"};
    parameterMap.put("reqp4", values4);

    List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
    genericRequestVariables.add(new GenericRequestVariable("reqp1", "[^0-9]"));
    genericRequestVariables.add(new GenericRequestVariable("reqp3", "[^a-z]"));
    genericRequestVariables.add(new GenericRequestVariable("reqp4", ""));

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
        .containsEntry("reqp1_0", "123456") //
        .containsEntry("reqp1_1", "") //
        .containsEntry("reqp3", "justone") //
        .containsEntry("reqp3_0", "justone") //
        .containsEntry("reqp4_0", "just one") //
        .containsEntry("reqp4_1", "just one again") //
        .hasSize(6);
  }
}
