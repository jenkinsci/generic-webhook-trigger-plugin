package org.jenkinsci.plugins.gwt.resolvers;

import static com.google.common.collect.Maps.newHashMap;
import static org.jenkinsci.plugins.gwt.resolvers.FlattenerUtils.filter;
import static org.jenkinsci.plugins.gwt.resolvers.FlattenerUtils.toVariableName;
import static org.w3c.dom.Node.ATTRIBUTE_NODE;
import static org.w3c.dom.Node.ELEMENT_NODE;
import static org.w3c.dom.Node.TEXT_NODE;

import java.util.Map;

import org.jenkinsci.plugins.gwt.GenericVariable;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlFlattener {
  public XmlFlattener() {}

  public Map<String, String> flatternXmlNode(GenericVariable gv, NodeList nodeList) {
    final Map<String, String> resolvedVariables = newHashMap();
    if (nodeList.getLength() > 0) {
      for (int i = 0; i < nodeList.getLength(); i++) {
        final boolean fromRootLevel = nodeList.getLength() == 1 ? true : false;
        resolvedVariables.putAll(
            flattenXmlNode(
                gv.getVariableName(), gv.getRegexpFilter(), nodeList.item(i), i, fromRootLevel));
      }
    } else {
      resolvedVariables.put(gv.getVariableName(), "");
    }
    return resolvedVariables;
  }

  private Map<String, String> flattenXmlNode(
      String parentKey, String regexFilter, Node node, int level, boolean fromRootLevel) {
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

  private boolean isXmlLeafNode(Node node) {
    return node != null
        && (node.getNodeType() == ELEMENT_NODE || node.getNodeType() == ATTRIBUTE_NODE)
        && node.getChildNodes().getLength() == 1
        && node.getFirstChild().getNodeType() == TEXT_NODE;
  }

  private String expandKey(String key, int level, boolean fromRootLevel) {
    if (fromRootLevel) {
      return key;
    } else {
      return key + "_" + level;
    }
  }
}
