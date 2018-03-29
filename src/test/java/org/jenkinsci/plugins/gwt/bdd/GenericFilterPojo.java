package org.jenkinsci.plugins.gwt.bdd;

public class GenericFilterPojo {
  private String expression;
  private String text;

  public String getExpression() {
    return expression;
  }

  public void setExpression(final String expression) {
    this.expression = expression;
  }

  public String getText() {
    return text;
  }

  public void setText(final String text) {
    this.text = text;
  }
}
