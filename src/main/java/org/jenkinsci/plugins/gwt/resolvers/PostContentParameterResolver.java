package org.jenkinsci.plugins.gwt.resolvers;

import static com.google.common.collect.Maps.newHashMap;
import static java.util.logging.Level.INFO;
import static org.jenkinsci.plugins.gwt.ExpressionType.JSONPath;
import static org.jenkinsci.plugins.gwt.ExpressionType.XPath;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.jenkinsci.plugins.gwt.GenericVariable;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.google.common.base.Charsets;
import com.jayway.jsonpath.JsonPath;

public class PostContentParameterResolver {
  private static final Logger LOGGER = Logger.getLogger(VariablesResolver.class.getName());

  private final XmlFlattener xmlFlattener = new XmlFlattener();
  private final JsonFlattener jsonFlattener = new JsonFlattener();

  public PostContentParameterResolver() {}

  public Map<String, String> getPostContentParameters(
      final List<GenericVariable> configuredGenericVariables, final String incomingPostContent) {
    final Map<String, String> resolvedVariables = newHashMap();
    if (configuredGenericVariables != null) {
      for (final GenericVariable gv : configuredGenericVariables) {
        resolvedVariables.putAll(resolve(incomingPostContent, gv));
      }
    }
    return resolvedVariables;
  }

  private Map<String, String> resolve(final String incomingPostContent, final GenericVariable gv) {
    try {
      if (gv != null && gv.getExpression() != null && !gv.getExpression().isEmpty()) {
        if (gv.getExpressionType() == JSONPath) {
          return resolveJsonPath(incomingPostContent, gv);
        } else if (gv.getExpressionType() == XPath) {
          return resolveXPath(incomingPostContent, gv);
        } else {
          throw new IllegalStateException("Not recognizing " + gv.getExpressionType());
        }
      }
    } catch (final Throwable e) {
      LOGGER.log(
          INFO,
          "Unable to resolve "
              + gv.getVariableName()
              + " with "
              + gv.getExpressionType()
              + " "
              + gv.getExpression()
              + " in\n"
              + incomingPostContent,
          e);
    }
    return new HashMap<>();
  }

  private Map<String, String> resolveJsonPath(
      final String incomingPostContent, final GenericVariable gv) {
    final Object resolved = JsonPath.read(incomingPostContent, gv.getExpression());
    return jsonFlattener.flattenJson(gv.getVariableName(), gv.getRegexpFilter(), resolved);
  }

  private Map<String, String> resolveXPath(
      final String incomingPostContent, final GenericVariable gv) throws Exception {
    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    final DocumentBuilder builder = factory.newDocumentBuilder();
    final InputSource inputSource =
        new InputSource(new ByteArrayInputStream(incomingPostContent.getBytes(Charsets.UTF_8)));
    final Document doc = builder.parse(inputSource);
    final XPathFactory xPathfactory = XPathFactory.newInstance();
    final XPath xpath = xPathfactory.newXPath();
    final XPathExpression expr = xpath.compile(gv.getExpression());
    final Object resolved = expr.evaluate(doc, XPathConstants.NODESET);
    return xmlFlattener.flatternXmlNode(gv, (NodeList) resolved);
  }
}
