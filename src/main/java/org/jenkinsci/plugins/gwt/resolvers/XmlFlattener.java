package org.jenkinsci.plugins.gwt.resolvers;

import static com.google.common.collect.Maps.newHashMap;
import static org.jenkinsci.plugins.gwt.resolvers.FlattenerUtils.filter;
import static org.jenkinsci.plugins.gwt.resolvers.FlattenerUtils.noWhitespace;

import java.util.Map;

import org.jenkinsci.plugins.gwt.GenericVariable;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlFlattener {
  public XmlFlattener() {}

  public Map<String, String> flatternXmlNode(GenericVariable gv, NodeList nodeList) {
    Map<String, String> resolvedVariables = newHashMap();
    if (nodeList.getLength() > 0) {
      for (int i = 0; i < nodeList.getLength(); i++) {
        boolean fromRootLevel = nodeList.getLength() == 1 ? true : false;
        resolvedVariables.putAll(
            flattenXmlNode(gv.getKey(), gv.getRegexpFilter(), nodeList.item(i), i, fromRootLevel));
      }
    } else {
      resolvedVariables.put(gv.getKey(), "");
    }
    return resolvedVariables;
  }

  private Map<String, String> flattenXmlNode(
      String parentKey, String regexFilter, Node node, int level, boolean fromRootLevel) {
    Map<String, String> resolvedVariables = newHashMap();
    if (isXmlLeafNode(node)) {
      String noWhitespaces = noWhitespace(parentKey);
      resolvedVariables.put(noWhitespaces, filter(node.getTextContent(), regexFilter));
    } else {
      for (int i = 0; i < node.getChildNodes().getLength(); i++) {
        Node childNode = node.getChildNodes().item(i);
        String childKey =
            expandKey(parentKey, level, fromRootLevel) + "_" + childNode.getNodeName();
        if (isXmlLeafNode(childNode)) {
          String noWhitespace = noWhitespace(childKey);
          resolvedVariables.put(noWhitespace, filter(childNode.getTextContent(), regexFilter));
        } else {
          //leafnode and text inside leafnode are 2 nodes, so /2 to keep counter in line
          int leafNodeLevel = i / 2;
          resolvedVariables.putAll(
              flattenXmlNode(childKey, regexFilter, childNode, leafNodeLevel, false));
        }
      }
    }
    return resolvedVariables;
  }

  private boolean isXmlLeafNode(Node node) {
    return node != null
        && node.getNodeType() == Node.ELEMENT_NODE
        && node.getChildNodes().getLength() == 1
        && node.getFirstChild().getNodeType() == Node.TEXT_NODE;
  }

  private String expandKey(String key, int level, boolean fromRootLevel) {
    if (fromRootLevel) {
      return key;
    } else {
      return key + "_" + level;
    }
  }
}
