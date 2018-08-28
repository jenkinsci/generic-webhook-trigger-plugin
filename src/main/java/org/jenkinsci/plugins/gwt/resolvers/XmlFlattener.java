package org.jenkinsci.plugins.gwt.resolvers;

import static com.google.common.collect.Maps.newHashMap;
import static org.jenkinsci.plugins.gwt.resolvers.FlattenerUtils.filter;
import static org.jenkinsci.plugins.gwt.resolvers.FlattenerUtils.toVariableName;
import static org.w3c.dom.Node.ATTRIBUTE_NODE;
import static org.w3c.dom.Node.ELEMENT_NODE;
import static org.w3c.dom.Node.TEXT_NODE;

import java.io.StringWriter;
import java.util.Map;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.jenkinsci.plugins.gwt.GenericVariable;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlFlattener {
  public XmlFlattener() {}

  public Map<String, String> flatternXmlNode(final GenericVariable gv, final NodeList nodeList)
      throws Exception {
    final Map<String, String> resolvedVariables = newHashMap();
    if (nodeList.getLength() > 0) {
      final boolean singleElementInNodeList = nodeList.getLength() == 1 ? true : false;
      resolvedVariables.put(gv.getVariableName(), toXmlString(nodeList.item(0)));
      for (int i = 0; i < nodeList.getLength(); i++) {
        resolvedVariables.putAll(
            flattenXmlNode(
                gv.getVariableName(),
                gv.getRegexpFilter(),
                nodeList.item(i),
                i,
                singleElementInNodeList));
      }
    } else {
      resolvedVariables.put(gv.getVariableName(), "");
    }
    return resolvedVariables;
  }

  private String toXmlString(final Node elem) throws Exception {
    final StringWriter buf = new StringWriter();
    final Transformer xform = TransformerFactory.newInstance().newTransformer();
    xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    xform.setOutputProperty(OutputKeys.INDENT, "yes");
    xform.transform(new DOMSource(elem), new StreamResult(buf));
    return buf.toString();
  }

  private Map<String, String> flattenXmlNode(
      final String parentKey,
      final String regexFilter,
      final Node node,
      final int level,
      final boolean fromRootLevel) {
    final Map<String, String> resolvedVariables = newHashMap();
    if (isXmlLeafNode(node)) {
      final String noWhitespaces = toVariableName(parentKey);
      resolvedVariables.put(noWhitespaces, filter(node.getTextContent(), regexFilter));
    } else {
      for (int i = 0; i < node.getChildNodes().getLength(); i++) {
        final Node childNode = node.getChildNodes().item(i);
        final String childKey =
            expandKey(parentKey, level, fromRootLevel) + "_" + childNode.getNodeName();
        if (isXmlLeafNode(childNode)) {
          final String variableName = toVariableName(childKey);
          resolvedVariables.put(variableName, filter(childNode.getTextContent(), regexFilter));
        } else {
          // leafnode and text inside leafnode are 2 nodes, so /2 to
          // keep counter in line
          final int leafNodeLevel = i / 2;
          resolvedVariables.putAll(
              flattenXmlNode(childKey, regexFilter, childNode, leafNodeLevel, false));
        }
      }
    }
    return resolvedVariables;
  }

  private boolean isXmlLeafNode(final Node node) {
    return node != null
        && (node.getNodeType() == ELEMENT_NODE || node.getNodeType() == ATTRIBUTE_NODE)
        && node.getChildNodes().getLength() == 1
        && node.getFirstChild().getNodeType() == TEXT_NODE;
  }

  private String expandKey(final String key, final int level, final boolean fromRootLevel) {
    if (fromRootLevel) {
      return key;
    } else {
      return key + "_" + level;
    }
  }
}
