package org.jenkinsci.plugins.gwt;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.jenkinsci.plugins.gwt.ExpressionType.JSONPath;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jenkinsci.plugins.gwt.resolvers.VariablesResolver;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class VariablesResolverJsonPathTest {
  private final Map<String, Enumeration<String>> headers = new HashMap<>();
  private final List<GenericHeaderVariable> genericHeaderVariables = new ArrayList<>();

  @Test
  public void testJSONPathGetOneLeaf() throws Exception {
    final String resourceName = "one-leaf.json";
    final Map<String, String> variables = getJsonPathVariables(resourceName, "$.user");

    assertThat(variables) //
        .containsEntry("variableName_name", "Administrator") //
        .hasSize(1);
  }

  @Test
  public void testJSONPathGetTwoLeafs() throws Exception {
    final String resourceName = "two-leafs.json";
    final Map<String, String> variables = getJsonPathVariables(resourceName, "$.user");

    assertThat(variables) //
        .containsEntry("variableName_name", "Administrator") //
        .containsEntry("variableName_username", "root") //
        .hasSize(2);
  }

  @Test
  public void testJSONPathGetOneListItem() throws Exception {
    final String resourceName = "one-list-item.json";
    final Map<String, String> variables = getJsonPathVariables(resourceName, "$.user");

    assertThat(variables) //
        .containsEntry("variableName_0_name", "Administrator") //
        .hasSize(1);
  }

  @Test
  public void testJSONPathGetTwoListItems() throws Exception {
    final String resourceName = "two-list-items.json";
    final Map<String, String> variables = getJsonPathVariables(resourceName, "$.user");

    assertThat(variables) //
        .containsEntry("variableName_0_name", "Administrator") //
        .containsEntry("variableName_1_username", "root") //
        .hasSize(2);
  }

  @Test
  public void testJSONPathGetSeveralMixedListItems() throws Exception {
    final String resourceName = "several-mixed-list-items.json";
    final Map<String, String> variables = getJsonPathVariables(resourceName, "$.user");

    assertThat(variables) //
        .containsEntry("variableName_0_name", "Administrator") //
        .containsEntry("variableName_1_username", "root") //
        .containsEntry("variableName_2", "a simple string") //
        .containsEntry("variableName_3", "33333") //
        .containsEntry("variableName_4_a_number", "a value") //
        .containsEntry("variableName_5", "another simple string") //
        .containsEntry("variableName_6", "66666") //
        .containsEntry("variableName_7_another_number", "another value") //
        .hasSize(8);
  }

  @Test
  public void testJSONPathGetAllVariable() throws Exception {
    final String resourceName = "gitlab-mergerequest-comment.json";
    final String postContent = getContent(resourceName);

    final String regexpFilter = "";
    final List<GenericVariable> genericVariables =
        newArrayList( //
            new GenericVariable("ids", "$..id", JSONPath, regexpFilter));
    final Map<String, String[]> parameterMap = new HashMap<>();
    final String[] values1 = new String[] {"a", "b"};
    parameterMap.put("reqp1", values1);
    final String[] values2 = new String[] {"just one"};
    parameterMap.put("reqp2", values2);
    final List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
    genericRequestVariables.add(new GenericRequestVariable("reqp1", ""));
    genericRequestVariables.add(new GenericRequestVariable("reqp2", ""));
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
        .containsEntry("ids_0", "28") //
        .containsEntry("ids_1", "1") //
        .containsEntry("ids_2", "1c3e5deb451353c34264b98c77836012a2106515") //
        .containsEntry("reqp1_0", "a") //
        .containsEntry("reqp1_1", "b") //
        .containsEntry("reqp2", "just one") //
        .containsEntry("reqp2_0", "just one") //
        .hasSize(7);
  }

  @Test
  public void testGenericRequestParameters() throws Exception {
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
  public void testJSONPathGetZeroMatchingVariables() throws Exception {
    final String resourceName = "gitlab-mergerequest-comment.json";
    final String postContent = getContent(resourceName);

    final String regexpFilter = "";
    final List<GenericVariable> genericVariables =
        newArrayList( //
            new GenericVariable("ids", "$..abc", JSONPath, regexpFilter));
    final Map<String, String[]> parameterMap = new HashMap<>();
    final List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
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
        .isEmpty();
  }

  @Test
  public void testJSONPathGetOneVariable() throws Exception {
    final String resourceName = "gitlab-mergerequest-comment.json";
    final String postContent = getContent(resourceName);

    final String regexpFilter = "";
    final List<GenericVariable> genericVariables =
        newArrayList( //
            new GenericVariable("user_name", "$.user.name", JSONPath, regexpFilter));
    final Map<String, String[]> parameterMap = new HashMap<>();
    final List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
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
        .containsEntry("user_name", "Administrator");
  }

  @Test
  public void testJSONPathGetTwoVariables() throws Exception {
    final String resourceName = "gitlab-mergerequest-comment.json";
    final String postContent = getContent(resourceName);

    final String regexpFilter = "";
    final List<GenericVariable> genericVariables =
        newArrayList( //
            new GenericVariable("user_name", "$.user.name", JSONPath, "[aA]"), //
            new GenericVariable("project_id", "$.project_id", JSONPath, regexpFilter));
    final Map<String, String[]> parameterMap = new HashMap<>();
    final List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
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
        .containsEntry("user_name", "dministrtor") //
        .containsEntry("project_id", "1");
  }

  @Test
  public void testJSONPathGetNodeVariable() throws Exception {
    final String resourceName = "gitlab-mergerequest-comment.json";
    final Map<String, String> variables = getJsonPathVariables(resourceName, "$.user");

    assertThat(variables) //
        .containsEntry("variableName_name", "Administrator");
  }

  @Test
  public void testJSONPathGetPayloadVariable() throws Exception {
    final String resourceName = "gitlab-mergerequest-comment.json";
    final String postContent = getContent(resourceName);

    final String regexpFilter = "";
    final List<GenericVariable> genericVariables =
        newArrayList( //
            new GenericVariable("payload", "$", JSONPath, regexpFilter));
    final Map<String, String[]> parameterMap = new HashMap<>();
    final List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
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
        .containsEntry("payload_user_name", "Administrator");
  }

  @Test
  public void testStarOperator() throws Exception {
    final String resourceName = "github-push-event.json";
    final Map<String, String> variables =
        getJsonPathVariables(resourceName, "$.commits[*].modified[*]");

    assertThat(variables) //
        .containsEntry("variableName_0", "README.md") //
        .hasSize(1);
  }

  @Test
  public void testCommaOperator() throws Exception {
    final String resourceName = "github-push-event.json";
    final Map<String, String> variables =
        getJsonPathVariables(resourceName, "$.commits[*].['modified','added','removed'][*]");

    assertThat(variables) //
        .containsEntry("variableName_0", "README.md") //
        .hasSize(1);
  }

  private Map<String, String> getJsonPathVariables(String resourceName, String jsonPath) {
    final String postContent = getContent(resourceName);

    final String regexpFilter = "";
    final List<GenericVariable> genericVariables =
        newArrayList( //
            new GenericVariable("variableName", jsonPath, JSONPath, regexpFilter));
    final Map<String, String[]> parameterMap = new HashMap<>();
    final List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
    final Map<String, String> variables =
        new VariablesResolver(
                headers,
                parameterMap,
                postContent,
                genericVariables,
                genericRequestVariables,
                genericHeaderVariables)
            .getVariables();
    return variables;
  }

  private String getContent(String resourceName) {
    try {
      return Resources.toString(
          Resources.getResource(resourceName).toURI().toURL(), Charsets.UTF_8);
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }
}
