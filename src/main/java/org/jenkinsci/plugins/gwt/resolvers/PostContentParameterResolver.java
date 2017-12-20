package org.jenkinsci.plugins.gwt.resolvers;

import static com.google.common.collect.Maps.newHashMap;
import static java.util.logging.Level.INFO;
import static org.jenkinsci.plugins.gwt.ExpressionType.JSONPath;
import static org.jenkinsci.plugins.gwt.ExpressionType.XPath;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.jenkinsci.plugins.gwt.GenericVariable;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.jayway.jsonpath.JsonPath;

public class PostContentParameterResolver {
  private static final Logger LOGGER = Logger.getLogger(VariablesResolver.class.getName());

  private final XmlFlattener xmlFlattener = new XmlFlattener();
  private final JsonFlattener jsonFlattener = new JsonFlattener();

  public PostContentParameterResolver() {}

  public Map<String, String> getPostContentParameters(
      List<GenericVariable> configuredGenericVariables, String incomingPostContent) {
    final Map<String, String> resolvedVariables = newHashMap();
    if (configuredGenericVariables != null) {
      for (final GenericVariable gv : configuredGenericVariables) {
        resolvedVariables.putAll(resolve(incomingPostContent, gv));
      }
    }
    return resolvedVariables;
  }

  private Map<String, String> resolve(String incomingPostContent, GenericVariable gv) {
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
    } catch (final Exception e) {
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

  private Map<String, String> resolveJsonPath(String incomingPostContent, GenericVariable gv) {
    final Object resolved = JsonPath.read(incomingPostContent, gv.getExpression());
    return jsonFlattener.flattenJson(gv.getVariableName(), gv.getRegexpFilter(), resolved);
  }

  private Map<String, String> resolveXPath(String incomingPostContent, GenericVariable gv)
      throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    final DocumentBuilder builder = factory.newDocumentBuilder();
    final InputSource inputSource =
        new InputSource(new ByteArrayInputStream(incomingPostContent.getBytes()));
    final Document doc = builder.parse(inputSource);
    final XPathFactory xPathfactory = XPathFactory.newInstance();
    final XPath xpath = xPathfactory.newXPath();
    final XPathExpression expr = xpath.compile(gv.getExpression());
    final Object resolved = expr.evaluate(doc, XPathConstants.NODESET);
    return xmlFlattener.flatternXmlNode(gv, (NodeList) resolved);
  }
}
