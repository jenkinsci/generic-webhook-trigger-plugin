package org.jenkinsci.plugins.gwt;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.jenkinsci.plugins.gwt.ExpressionType.JSONPath;
import static org.jenkinsci.plugins.gwt.ExpressionType.XPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class VariablesResolverTest {

  @Test
  public void testJSONPathGetAllVariable() throws Exception {
    String resourceName = "gital-mergerequest-comment.json";
    String postContent = getContent(resourceName);

    String regexpFilter = "";
    List<GenericVariable> genericVariables =
        newArrayList( //
            new GenericVariable("ids", "$..id", JSONPath, regexpFilter));
    Map<String, String[]> parameterMap = new HashMap<>();
    String[] values1 = new String[] {"a", "b"};
    parameterMap.put("reqp1", values1);
    String[] values2 = new String[] {"just one"};
    parameterMap.put("reqp2", values2);
    List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
    genericRequestVariables.add(new GenericRequestVariable("reqp1", ""));
    genericRequestVariables.add(new GenericRequestVariable("reqp2", ""));
    Map<String, String> variables =
        new VariablesResolver(parameterMap, postContent, genericVariables, genericRequestVariables)
            .getVariables();

    assertThat(variables) //
        .containsEntry("ids_0", "28") //
        .containsEntry("ids_1", "1") //
        .containsEntry("ids_2", "1c3e5deb451353c34264b98c77836012a2106515") //
        .containsEntry("reqp1_0", "a") //
        .containsEntry("reqp1_1", "b") //
        .containsEntry("reqp2", "just one") //
        .hasSize(6);
  }

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
        new VariablesResolver(parameterMap, postContent, genericVariables, genericRequestVariables)
            .getVariables();

    assertThat(variables) //
        .containsEntry("reqp1", "123456") //
        .containsEntry("reqp3", "justone") //
        .containsEntry("reqp4_0", "just one") //
        .containsEntry("reqp4_1", "just one again") //
        .hasSize(4);
  }

  @Test
  public void testJSONPathGetZeroMatchingVariables() throws Exception {
    String resourceName = "gital-mergerequest-comment.json";
    String postContent = getContent(resourceName);

    String regexpFilter = "";
    List<GenericVariable> genericVariables =
        newArrayList( //
            new GenericVariable("ids", "$..abc", JSONPath, regexpFilter));
    Map<String, String[]> parameterMap = new HashMap<>();
    List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
    Map<String, String> variables =
        new VariablesResolver(parameterMap, postContent, genericVariables, genericRequestVariables)
            .getVariables();

    assertThat(variables) //
        .isEmpty();
  }

  @Test
  public void testJSONPathGetOneVariable() throws Exception {
    String resourceName = "gital-mergerequest-comment.json";
    String postContent = getContent(resourceName);

    String regexpFilter = "";
    List<GenericVariable> genericVariables =
        newArrayList( //
            new GenericVariable("user_name", "$.user.name", JSONPath, regexpFilter));
    Map<String, String[]> parameterMap = new HashMap<>();
    List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
    Map<String, String> variables =
        new VariablesResolver(parameterMap, postContent, genericVariables, genericRequestVariables)
            .getVariables();

    assertThat(variables) //
        .containsEntry("user_name", "Administrator");
  }

  @Test
  public void testJSONPathGetTwoVariables() throws Exception {
    String resourceName = "gital-mergerequest-comment.json";
    String postContent = getContent(resourceName);

    String regexpFilter = "";
    List<GenericVariable> genericVariables =
        newArrayList( //
            new GenericVariable("user_name", "$.user.name", JSONPath, "[aA]"), //
            new GenericVariable("project_id", "$.project_id", JSONPath, regexpFilter));
    Map<String, String[]> parameterMap = new HashMap<>();
    List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
    Map<String, String> variables =
        new VariablesResolver(parameterMap, postContent, genericVariables, genericRequestVariables)
            .getVariables();

    assertThat(variables) //
        .containsEntry("user_name", "dministrtor") //
        .containsEntry("project_id", "1");
  }

  @Test
  public void testXPathGetOneVariable() throws Exception {
    String resourceName = "example.xml";
    String postContent = getContent(resourceName);

    String regexpFilter = "";
    List<GenericVariable> genericVariables =
        newArrayList( //
            new GenericVariable("book", "/bookstore/book[1]/title", XPath, regexpFilter));
    Map<String, String[]> parameterMap = new HashMap<>();
    List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
    Map<String, String> variables =
        new VariablesResolver(parameterMap, postContent, genericVariables, genericRequestVariables)
            .getVariables();

    assertThat(variables) //
        .containsEntry("book", "Harry Potter");
  }

  @Test
  public void testHPathGetTwoVariable() throws Exception {
    String resourceName = "example.xml";
    String postContent = getContent(resourceName);

    String regexpFilter = "";
    List<GenericVariable> genericVariables =
        newArrayList( //
            new GenericVariable("book1", "/bookstore/book[1]/title", XPath, "\\s"), //
            new GenericVariable("book2", "/bookstore/book[2]/title", XPath, regexpFilter));
    Map<String, String[]> parameterMap = new HashMap<>();
    List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
    Map<String, String> variables =
        new VariablesResolver(parameterMap, postContent, genericVariables, genericRequestVariables)
            .getVariables();

    assertThat(variables) //
        .containsEntry("book1", "HarryPotter") //
        .containsEntry("book2", "Learning XML");
  }

  @Test
  public void testXPathGetZeroMatchingVariables() throws Exception {
    String resourceName = "example.xml";
    String postContent = getContent(resourceName);

    List<GenericVariable> genericVariables =
        newArrayList( //
            new GenericVariable("book1", "/bookstore/book[1]/title123", XPath, "[a-z]"));
    Map<String, String[]> parameterMap = new HashMap<>();
    List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
    Map<String, String> variables =
        new VariablesResolver(parameterMap, postContent, genericVariables, genericRequestVariables)
            .getVariables();

    assertThat(variables) //
        .containsEntry("book1", "");
  }

  private String getContent(String resourceName) {
    try {
      return Resources.toString(
          Resources.getResource(resourceName).toURI().toURL(), Charsets.UTF_8);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
