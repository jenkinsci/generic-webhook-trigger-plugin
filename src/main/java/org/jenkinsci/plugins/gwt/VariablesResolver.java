package org.jenkinsci.plugins.gwt;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.fromNullable;
import static com.google.common.collect.Maps.newHashMap;
import static org.jenkinsci.plugins.gwt.ExpressionType.JSONPath;
import static org.jenkinsci.plugins.gwt.ExpressionType.XPath;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.jayway.jsonpath.JsonPath;

public class VariablesResolver {
  private static final Logger LOGGER = Logger.getLogger(VariablesResolver.class.getName());

  private List<GenericVariable> genericVariables = Lists.newArrayList();
  private final List<GenericRequestVariable> genericRequestVariables;
  private final String postContent;
  private final Map<String, String[]> parameterMap;

  public VariablesResolver(
      Map<String, String[]> parameterMap,
      String postContent,
      List<GenericVariable> genericVariables,
      List<GenericRequestVariable> genericRequestVariables) {
    this.postContent = postContent;
    this.genericVariables = genericVariables;
    this.parameterMap = parameterMap;
    this.genericRequestVariables = genericRequestVariables;
  }

  public Map<String, String> getVariables() {
    Map<String, String> map = newHashMap();
    addRequestParameters(map);
    addPostContentParameters(map);
    return map;
  }

  private void addPostContentParameters(Map<String, String> map) {
    if (genericVariables != null) {
      for (GenericVariable gv : genericVariables) {
        Object resolved = resolve(gv);

        if (resolved instanceof List) {
          int i = 0;
          for (Object o : (List<?>) resolved) {
            map.put(gv.getKey() + "_" + i, filter(o.toString(), gv.getRegexpFilter()));
            i++;
          }
        } else {
          map.put(gv.getKey(), filter(resolved.toString(), gv.getRegexpFilter()));
        }
      }
    }
  }

  private void addRequestParameters(Map<String, String> map) {
    if (parameterMap != null) {
      for (String requestParamName : parameterMap.keySet()) {
        Optional<String> mappedRequestParameterOpt =
            findMappedRequestParameter(genericRequestVariables, requestParamName);
        if (!mappedRequestParameterOpt.isPresent()) {
          continue;
        }
        String regexpFilter = mappedRequestParameterOpt.get();
        String[] values = parameterMap.get(requestParamName);
        if (values.length == 1) {
          map.put(requestParamName, filter(values[0], regexpFilter));
        } else {
          List<String> foundFilteredValues = new ArrayList<>();
          for (String valueCandidate : values) {
            String filteredValue = filter(valueCandidate, regexpFilter);
            if (!filteredValue.isEmpty()) {
              foundFilteredValues.add(filteredValue);
            }
          }
          if (foundFilteredValues.size() == 1) {
            String filteredValue = foundFilteredValues.get(0);
            map.put(requestParamName, filteredValue);
          } else {
            for (int i = 0; i < foundFilteredValues.size(); i++) {
              String filteredValue = foundFilteredValues.get(i);
              map.put(requestParamName + "_" + i, filteredValue);
            }
          }
        }
      }
    }
  }

  private Optional<String> findMappedRequestParameter(
      List<GenericRequestVariable> genericRequestVariables, String requestParamName) {
    if (genericRequestVariables != null) {
      for (GenericRequestVariable v : genericRequestVariables) {
        if (v.getKey().equals(requestParamName)) {
          return fromNullable(v.getRegexpFilter());
        }
      }
    }
    return absent();
  }

  private String filter(String string, String regexpFilter) {
    if (string == null || regexpFilter == null || regexpFilter.isEmpty()) {
      return string;
    }
    return string.replaceAll(regexpFilter, "");
  }

  private Object resolve(GenericVariable gv) {
    try {
      if (gv != null && gv.getValue() != null && !gv.getValue().isEmpty()) {
        if (gv.getExpressionType() == JSONPath) {
          return JsonPath.read(postContent, gv.getValue());
        } else if (gv.getExpressionType() == XPath) {
          DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
          DocumentBuilder builder = factory.newDocumentBuilder();
          InputSource inputSource =
              new InputSource(new ByteArrayInputStream(postContent.getBytes()));
          Document doc = builder.parse(inputSource);
          XPathFactory xPathfactory = XPathFactory.newInstance();
          XPath xpath = xPathfactory.newXPath();
          XPathExpression expr = xpath.compile(gv.getValue());
          return expr.evaluate(doc);
        } else {
          throw new IllegalStateException("Not recognizing " + gv.getExpressionType());
        }
      }
    } catch (Exception e) {
      LOGGER.info("Unable to resolve " + gv.getKey());
    }
    return "";
  }
}
