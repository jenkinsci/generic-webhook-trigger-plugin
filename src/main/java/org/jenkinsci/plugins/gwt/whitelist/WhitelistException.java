package org.jenkinsci.plugins.gwt.whitelist;

public class WhitelistException extends RuntimeException {

  private static final long serialVersionUID = -3821871257758501700L;

  public WhitelistException(final String string) {
    super(string);
  }
}
