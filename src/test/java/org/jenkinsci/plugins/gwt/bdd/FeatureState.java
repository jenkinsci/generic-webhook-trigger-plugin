package org.jenkinsci.plugins.gwt.bdd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jenkinsci.plugins.gwt.GenericHeaderVariable;
import org.jenkinsci.plugins.gwt.GenericRequestVariable;
import org.jenkinsci.plugins.gwt.GenericVariable;

public class FeatureState {
    private Map<String, List<String>> headers = new HashMap<>();
    private Map<String, String[]> parameterMap = new HashMap<>();
    private String postContent;
    private List<GenericVariable> genericVariables = new ArrayList<>();
    private List<GenericRequestVariable> genericRequestVariables = new ArrayList<>();
    private List<GenericHeaderVariable> genericHeaderVariables = new ArrayList<>();
    private String regexpFilterText;
    private String regexpFilterExpression;
    private boolean shouldNotFlatten;

    public Map<String, List<String>> getHeaders() {
        return this.headers;
    }

    public void setHeaders(final Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public Map<String, String[]> getParameterMap() {
        return this.parameterMap;
    }

    public void setParameterMap(final Map<String, String[]> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public String getPostContent() {
        return this.postContent;
    }

    public void setPostContent(final String postContent) {
        this.postContent = postContent;
    }

    public List<GenericVariable> getGenericVariables() {
        return this.genericVariables;
    }

    public void setGenericVariables(final List<GenericVariable> genericVariables) {
        this.genericVariables = genericVariables;
    }

    public List<GenericRequestVariable> getGenericRequestVariables() {
        return this.genericRequestVariables;
    }

    public void setGenericRequestVariables(final List<GenericRequestVariable> genericRequestVariables) {
        this.genericRequestVariables = genericRequestVariables;
    }

    public List<GenericHeaderVariable> getGenericHeaderVariables() {
        return this.genericHeaderVariables;
    }

    public void setGenericHeaderVariables(final List<GenericHeaderVariable> genericHeaderVariables) {
        this.genericHeaderVariables = genericHeaderVariables;
    }

    public String getRegexpFilterText() {
        return this.regexpFilterText;
    }

    public void setRegexpFilterText(final String regexpFilterText) {
        this.regexpFilterText = regexpFilterText;
    }

    public String getRegexpFilterExpression() {
        return this.regexpFilterExpression;
    }

    public void setRegexpFilterExpression(final String regexpFilterExpression) {
        this.regexpFilterExpression = regexpFilterExpression;
    }

    public boolean getShouldNotFlatten() {
        return this.shouldNotFlatten;
    }

    public void setShouldNotFlatten(final boolean shouldNotFlatten) {
        this.shouldNotFlatten = shouldNotFlatten;
    }

    @Override
    public String toString() {
        return "FeatureState [headers="
                + this.headers
                + ", parameterMap="
                + this.parameterMap
                + ", postContent="
                + this.postContent
                + ", genericVariables="
                + this.genericVariables
                + ", genericRequestVariables="
                + this.genericRequestVariables
                + ", genericHeaderVariables="
                + this.genericHeaderVariables
                + ", regexpFilterText="
                + this.regexpFilterText
                + ", regexpFilterExpression="
                + this.regexpFilterExpression
                + ", shouldNotFlatten="
                + this.shouldNotFlatten
                + "]";
    }
}
