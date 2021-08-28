package org.jenkinsci.plugins.gwt.global;

public enum WhitelistAlgorithm {
  HMAC_MD5("HmacMD5", "md5"),
  HMAC_SHA1("HmacSHA1", "sha1"),
  HMAC_SHA256("HmacSHA256", "sha256");

  private String fullName;
  private String algorithm;

  WhitelistAlgorithm(final String fullName, final String algorithm) {
    this.fullName = fullName;
    this.algorithm = algorithm;
  }

  public String getFullName() {
    return this.fullName;
  }

  public String getAlgorithm() {
    return this.algorithm;
  }
}
