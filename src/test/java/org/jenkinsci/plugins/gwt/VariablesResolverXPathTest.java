package org.jenkinsci.plugins.gwt;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.jenkinsci.plugins.gwt.ExpressionType.XPath;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jenkinsci.plugins.gwt.resolvers.VariablesResolver;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class VariablesResolverXPathTest {
  private final Map<String, Enumeration<String>> headers = new HashMap<>();
  private final List<GenericHeaderVariable> genericHeaderVariables = new ArrayList<>();

  @Test
  public void testXPathGetOneVariable() throws Exception {
    String resourceName = "two-list-items.xml";
    String postContent = getContent(resourceName);

    String regexpFilter = "";
    List<GenericVariable> genericVariables =
        newArrayList( //
            new GenericVariable("book", "/bookstore/book[1]/title", XPath, regexpFilter));
    Map<String, String[]> parameterMap = new HashMap<>();
    List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
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
        .containsEntry("book", "Harry Potter");
  }

  @Test
  public void testXPathGetOneNode() throws Exception {
    String resourceName = "two-list-items.xml";
    String postContent = getContent(resourceName);

    String regexpFilter = "";
    List<GenericVariable> genericVariables =
        newArrayList( //
            new GenericVariable("book", "/bookstore/book[1]", XPath, regexpFilter));
    Map<String, String[]> parameterMap = new HashMap<>();
    List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
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
        .containsEntry("book_title", "Harry Potter");
  }

  @Test
  public void testXPathGetNodes() throws Exception {
    String resourceName = "two-list-items.xml";
    String postContent = getContent(resourceName);

    String regexpFilter = "";
    List<GenericVariable> genericVariables =
        newArrayList( //
            new GenericVariable("book", "/bookstore/book[*]", XPath, regexpFilter));
    Map<String, String[]> parameterMap = new HashMap<>();
    List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
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
        .containsEntry("book_0_title", "Harry Potter")
        .containsEntry("book_1_title", "Learning XML");
  }

  @Test
  public void testXPathTwoListItems() throws Exception {
    String resourceName = "two-list-items.xml";
    String postContent = getContent(resourceName);

    String regexpFilter = "";
    List<GenericVariable> genericVariables =
        newArrayList( //
            new GenericVariable("payload", "/*", XPath, regexpFilter));
    Map<String, String[]> parameterMap = new HashMap<>();
    List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
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
        .containsEntry("payload_book_0_price", "29.99")
        .containsEntry("payload_book_0_title", "Harry Potter")
        .containsEntry("payload_book_1_price", "39.95")
        .containsEntry("payload_book_1_title", "Learning XML")
        .hasSize(4);
  }

  @Test
  public void testXPathOneListItem() throws Exception {
    String resourceName = "one-list-item.xml";
    String postContent = getContent(resourceName);

    String regexpFilter = "";
    List<GenericVariable> genericVariables =
        newArrayList( //
            new GenericVariable("payload", "/*", XPath, regexpFilter));
    Map<String, String[]> parameterMap = new HashMap<>();
    List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
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
        .containsEntry("payload_book_0_price", "29.99")
        .containsEntry("payload_book_0_title", "Harry Potter")
        .hasSize(2);
  }

  @Test
  public void testXPathTwoListListItems() throws Exception {
    String resourceName = "two-list-list-items.xml";
    String postContent = getContent(resourceName);

    String regexpFilter = "";
    List<GenericVariable> genericVariables =
        newArrayList( //
            new GenericVariable("book", "/bookstore", XPath, regexpFilter));
    Map<String, String[]> parameterMap = new HashMap<>();
    List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
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
        .containsEntry("book_book_0_page_0_content", "content 1")
        .containsEntry("book_book_0_page_0_number", "1")
        .containsEntry("book_book_0_page_1_content", "content 2")
        .containsEntry("book_book_0_page_1_number", "2")
        .containsEntry("book_book_1_page_0_content", "content 21")
        .containsEntry("book_book_1_page_0_number", "21")
        .hasSize(6);
  }

  @Test
  public void testXPathTwoListListItemsFirstBook() throws Exception {
    String resourceName = "two-list-list-items.xml";
    String postContent = getContent(resourceName);

    String regexpFilter = "";
    List<GenericVariable> genericVariables =
        newArrayList( //
            new GenericVariable("book", "/bookstore/book[1]", XPath, regexpFilter));
    Map<String, String[]> parameterMap = new HashMap<>();
    List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
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
        .containsEntry("book_page_0_content", "content 1")
        .containsEntry("book_page_0_number", "1")
        .containsEntry("book_page_1_content", "content 2")
        .containsEntry("book_page_1_number", "2")
        .hasSize(4);
  }

  @Test
  public void testXPathTwoListListItemsSecondBook() throws Exception {
    String resourceName = "two-list-list-items.xml";
    String postContent = getContent(resourceName);

    String regexpFilter = "";
    List<GenericVariable> genericVariables =
        newArrayList( //
            new GenericVariable("book", "/bookstore/book[2]", XPath, regexpFilter));
    Map<String, String[]> parameterMap = new HashMap<>();
    List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
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
        .containsEntry("book_page_0_content", "content 21")
        .containsEntry("book_page_0_number", "21")
        .hasSize(2);
  }

  @Test
  public void testXPathTwoListItemsFirstBook() throws Exception {
    String resourceName = "two-list-items.xml";
    String postContent = getContent(resourceName);

    String regexpFilter = "";
    List<GenericVariable> genericVariables =
        newArrayList( //
            new GenericVariable("book", "/bookstore/book[1]", XPath, regexpFilter));
    Map<String, String[]> parameterMap = new HashMap<>();
    List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
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
        .containsEntry("book_price", "29.99")
        .containsEntry("book_title", "Harry Potter")
        .hasSize(2);
  }

  @Test
  public void testXPathGetTwoVariable() throws Exception {
    String resourceName = "two-list-items.xml";
    String postContent = getContent(resourceName);

    String regexpFilter = "";
    List<GenericVariable> genericVariables =
        newArrayList( //
            new GenericVariable("book1", "/bookstore/book[1]/title", XPath, "\\s"), //
            new GenericVariable("book2", "/bookstore/book[2]/title", XPath, regexpFilter));
    Map<String, String[]> parameterMap = new HashMap<>();
    List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
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
        .containsEntry("book1", "HarryPotter") //
        .containsEntry("book2", "Learning XML");
  }

  @Test
  public void testXPathGetZeroMatchingVariables() throws Exception {
    String resourceName = "two-list-items.xml";
    String postContent = getContent(resourceName);

    List<GenericVariable> genericVariables =
        newArrayList( //
            new GenericVariable("book1", "/bookstore/book[1]/title123", XPath, "[a-z]"));
    Map<String, String[]> parameterMap = new HashMap<>();
    List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
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
