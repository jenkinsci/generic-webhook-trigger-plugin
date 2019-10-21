package org.jenkinsci.plugins.gwt.whitelist;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.jenkinsci.plugins.gwt.whitelist.HMACVerifier.hmacVerify;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jenkinsci.plugins.gwt.global.WhitelistItem;
import org.junit.Test;

public class HMACVerifierTest {

  @Test
  public void testThatHmacCanBeVerifiedAndValid() throws Exception {
    final Map<String, List<String>> headers;
    final String postContent =
        new String(
            Files.readAllBytes(
                Paths.get(
                    getClass().getResource("/hmac/hmac-bitbucket-server-payload.json").toURI())),
            UTF_8);
    final String hmacHeader = "X-Hub-Signature";
    final String hmacSecret = "this is secret";
    final String algorithm = WhitelistItem.HMAC_SHA256;
    headers = new HashMap<String, List<String>>();
    headers.put(
        "X-Hub-Signature",
        Arrays.asList("sha256=87e3e7b7e4567f528342a75b6d88c619f272c68a4d0d565c68d596a830213164"));

    final boolean actual = testHmacVerify(headers, postContent, hmacHeader, hmacSecret, algorithm);

    assertThat(actual).isTrue();
  }

  @Test
  public void testThatHmacCanBeVerifiedAndValidWIthoutAlgorithmInHeader() throws Exception {
    final Map<String, List<String>> headers;
    final String postContent =
        new String(
            Files.readAllBytes(
                Paths.get(
                    getClass().getResource("/hmac/hmac-bitbucket-server-payload.json").toURI())),
            UTF_8);
    final String hmacHeader = "X-Hub-Signature";
    final String hmacSecret = "this is secret";
    final String algorithm = WhitelistItem.HMAC_SHA256;
    headers = new HashMap<String, List<String>>();
    headers.put(
        "X-Hub-Signature",
        Arrays.asList("87e3e7b7e4567f528342a75b6d88c619f272c68a4d0d565c68d596a830213164"));

    final boolean actual = testHmacVerify(headers, postContent, hmacHeader, hmacSecret, algorithm);

    assertThat(actual).isTrue();
  }

  @Test
  public void testThatHmacCanBeVerifiedAndInvalid() throws Exception {
    final Map<String, List<String>> headers;
    final String postContent =
        new String(
            Files.readAllBytes(
                Paths.get(
                    getClass().getResource("/hmac/hmac-bitbucket-server-payload.json").toURI())),
            UTF_8);
    final String hmacHeader = "X-Hub-Signature";
    final String hmacSecret = "this is secret";
    final String algorithm = WhitelistItem.HMAC_SHA256;
    headers = new HashMap<String, List<String>>();
    headers.put(
        "X-Hub-Signature",
        Arrays.asList("sha256=97e3e7b7e4567f528342a75b6d88c619f272c68a4d0d565c68d596a830213164"));

    final boolean actual = testHmacVerify(headers, postContent, hmacHeader, hmacSecret, algorithm);

    assertThat(actual).isFalse();
  }

  private boolean testHmacVerify(
      final Map<String, List<String>> headers,
      final String postContent,
      final String hmacHeader,
      final String hmacSecret,
      final String algorithm) {
    try {
      hmacVerify(headers, postContent, hmacHeader, hmacSecret, algorithm);
      return true;
    } catch (final WhitelistException e) {
      return false;
    }
  }
}
