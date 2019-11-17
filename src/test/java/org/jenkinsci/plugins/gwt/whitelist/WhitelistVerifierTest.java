package org.jenkinsci.plugins.gwt.whitelist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.jenkinsci.plugins.gwt.whitelist.WhitelistVerifier.doVerifyWhitelist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.jenkinsci.plugins.gwt.global.Whitelist;
import org.jenkinsci.plugins.gwt.global.WhitelistItem;
import org.junit.Test;

public class WhitelistVerifierTest {

  @Test
  public void testThatRequestIsValidWhenNoWhitelist() {
    final Map<String, List<String>> headers = new HashMap<>();
    final String postContent = "";

    final String remoteHost = "192.168.0.1";
    final boolean enabled = false;
    final Whitelist whitelist = new Whitelist(enabled, new ArrayList<WhitelistItem>());

    assertThat(testDoVerifyWhitelist(remoteHost, headers, postContent, whitelist)).isTrue();
  }

  @Test
  public void testThatHostCanBeVerifiedAndValid() {
    final WhitelistItem whitelistItem = new WhitelistItem("192.168.0.1");
    whitelistItem.setHmacEnabled(false);
    final Map<String, List<String>> headers = new HashMap<>();
    final String postContent = "";

    final String remoteHost = "192.168.0.1";
    final boolean enabled = true;
    final Whitelist whitelist = new Whitelist(enabled, Arrays.asList(whitelistItem));

    assertThat(testDoVerifyWhitelist(remoteHost, headers, postContent, whitelist)).isTrue();
  }

  @Test
  public void testThatHostCanBeVerifiedAndInvalid() {
    final WhitelistItem whitelistItem = new WhitelistItem("192.168.0.1");
    whitelistItem.setHmacEnabled(false);
    final Map<String, List<String>> headers = new HashMap<>();
    final String postContent = "";

    final String remoteHost = "anotherhost";
    final boolean enabled = true;
    final Whitelist whitelist = new Whitelist(enabled, Arrays.asList(whitelistItem));

    assertThat(testDoVerifyWhitelist(remoteHost, headers, postContent, whitelist)).isFalse();
  }

  @Test
  public void testThatHostIsAcceptedWhenWhitelistDisabled() {
    final WhitelistItem whitelistItem = new WhitelistItem("192.168.0.1");
    whitelistItem.setHmacEnabled(false);
    final Map<String, List<String>> headers = new HashMap<>();
    final String postContent = "";

    final String remoteHost = "anotherhost";
    final boolean enabled = false;
    final Whitelist whitelist = new Whitelist(enabled, Arrays.asList(whitelistItem));

    assertThat(testDoVerifyWhitelist(remoteHost, headers, postContent, whitelist)).isTrue();
  }

  @Test
  public void testThatHostCanBeVerifiedAndInvalidAndValidByAnotherItem() throws Exception {
    final WhitelistItem whitelistItem1 = new WhitelistItem("192.168.0.1");
    whitelistItem1.setHmacEnabled(false);

    final WhitelistItem whitelistItem2 = new WhitelistItem("192.168.0.2");
    whitelistItem2.setHmacEnabled(false);

    final WhitelistItem whitelistItem3 = new WhitelistItem("192.168.0.3");
    whitelistItem3.setHmacEnabled(false);

    final Map<String, List<String>> headers = new HashMap<>();
    final String postContent = "";

    final boolean enabled = true;
    final Whitelist whitelist =
        new Whitelist(enabled, Arrays.asList(whitelistItem1, whitelistItem2, whitelistItem3));

    assertThat(testDoVerifyWhitelist("192.168.0.0", headers, postContent, whitelist)).isFalse();
    assertThat(testDoVerifyWhitelist("192.168.0.1", headers, postContent, whitelist)).isTrue();
    assertThat(testDoVerifyWhitelist("192.168.0.2", headers, postContent, whitelist)).isTrue();
    assertThat(testDoVerifyWhitelist("192.168.0.3", headers, postContent, whitelist)).isTrue();
    assertThat(testDoVerifyWhitelist("192.168.0.4", headers, postContent, whitelist)).isFalse();
  }

  @Test
  public void testThatHostCanBeVerifiedWithSupportedNotations() {
    final WhitelistItem whitelistItem1 = new WhitelistItem("1.2.3.4");
    whitelistItem1.setHmacEnabled(false);

    final WhitelistItem whitelistItem2 = new WhitelistItem("2.2.3.0/24");
    whitelistItem2.setHmacEnabled(false);

    final WhitelistItem whitelistItem3 = new WhitelistItem("3.2.1.1-3.2.1.10");
    whitelistItem3.setHmacEnabled(false);

    final WhitelistItem whitelistItem4 =
        new WhitelistItem("2001:0db8:85a3:0000:0000:8a2e:0370:7334");
    whitelistItem4.setHmacEnabled(false);

    final WhitelistItem whitelistItem5 =
        new WhitelistItem("2002:0db8:85a3:0000:0000:8a2e:0370:7334/127");
    whitelistItem5.setHmacEnabled(false);

    final Map<String, List<String>> headers = new HashMap<>();
    final String postContent = "";

    final boolean enabled = true;
    final Whitelist whitelist =
        new Whitelist(
            enabled,
            Arrays.asList(
                whitelistItem1, whitelistItem2, whitelistItem3, whitelistItem4, whitelistItem5));

    assertThat(testDoVerifyWhitelist("1.2.3.4", headers, postContent, whitelist)).isTrue();
    assertThat(testDoVerifyWhitelist("3.2.1.0", headers, postContent, whitelist)).isFalse();
    assertThat(testDoVerifyWhitelist("3.2.1.1", headers, postContent, whitelist)).isTrue();
    assertThat(testDoVerifyWhitelist("3.2.1.10", headers, postContent, whitelist)).isTrue();
    assertThat(testDoVerifyWhitelist("3.2.1.11", headers, postContent, whitelist)).isFalse();
    assertThat(testDoVerifyWhitelist("1.1.1.2", headers, postContent, whitelist)).isFalse();
    assertThat(testDoVerifyWhitelist("2.2.3.50", headers, postContent, whitelist)).isTrue();
    assertThat(
            testDoVerifyWhitelist(
                "2001:0db8:85a3:0000:0000:8a2e:0370:7334", headers, postContent, whitelist))
        .isTrue();
    assertThat(
            testDoVerifyWhitelist(
                "2002:0db8:85a3:0000:0000:8a2e:0370:7335", headers, postContent, whitelist))
        .isTrue();
  }

  @Test
  public void testThatHostCanBeVerifiedWithCidr() {
    final WhitelistItem whitelistItem = new WhitelistItem("2.2.3.0/24");
    whitelistItem.setHmacEnabled(false);

    final Map<String, List<String>> headers = new HashMap<>();
    final String postContent = "";

    final boolean enabled = true;
    final Whitelist whitelist = new Whitelist(enabled, Arrays.asList(whitelistItem));

    assertThat(testDoVerifyWhitelist("2.2.2.255", headers, postContent, whitelist)).isFalse();
    assertThat(testDoVerifyWhitelist("2.2.3.0", headers, postContent, whitelist)).isTrue();
    assertThat(testDoVerifyWhitelist("2.2.3.255", headers, postContent, whitelist)).isTrue();
    assertThat(testDoVerifyWhitelist("2.2.4.0", headers, postContent, whitelist)).isFalse();
  }

  @Test
  public void testThatHostCanBeVerifiedWithRanges() {
    final WhitelistItem whitelistItem = new WhitelistItem("3.2.3.5-3.2.3.10");
    whitelistItem.setHmacEnabled(false);

    final Map<String, List<String>> headers = new HashMap<>();
    final String postContent = "";

    final boolean enabled = true;
    final Whitelist whitelist = new Whitelist(enabled, Arrays.asList(whitelistItem));

    assertThat(testDoVerifyWhitelist("3.2.3.4", headers, postContent, whitelist)).isFalse();
    assertThat(testDoVerifyWhitelist("3.2.3.5", headers, postContent, whitelist)).isTrue();
    assertThat(testDoVerifyWhitelist("3.2.3.10", headers, postContent, whitelist)).isTrue();
    assertThat(testDoVerifyWhitelist("3.2.3.11", headers, postContent, whitelist)).isFalse();
  }

  @Test
  public void testThatHostCanBeVerifiedWithAny() {
    final WhitelistItem whitelistItem = new WhitelistItem("");
    whitelistItem.setHmacEnabled(false);

    final Map<String, List<String>> headers = new HashMap<>();
    final String postContent = "";

    final boolean enabled = true;
    final Whitelist whitelist = new Whitelist(enabled, Arrays.asList(whitelistItem));

    assertThat(testDoVerifyWhitelist("6.2.3.10", headers, postContent, whitelist)).isTrue();
  }

  @Test
  public void testThatHostCanBeVerifiedWithAnyAndDeniedByHmac() {
    final WhitelistItem whitelistItem = new WhitelistItem("");
    whitelistItem.setHmacEnabled(true);

    final Map<String, List<String>> headers = new HashMap<>();
    final String postContent = "";

    final boolean enabled = true;
    final Whitelist whitelist = new Whitelist(enabled, Arrays.asList(whitelistItem));

    assertThat(testDoVerifyWhitelist("6.2.3.10", headers, postContent, whitelist)).isFalse();
  }

  @Test
  public void testThatInvalidRangeThrowsException() {
    final WhitelistItem whitelistItem = new WhitelistItem("3.2.3.a-3.2.3.10");
    whitelistItem.setHmacEnabled(false);

    final Map<String, List<String>> headers = new HashMap<>();
    final String postContent = "";

    final boolean enabled = true;
    final Whitelist whitelist = new Whitelist(enabled, Arrays.asList(whitelistItem));

    try {
      doVerifyWhitelist("1.1.1.1", headers, postContent, whitelist);
      fail("No exception");
    } catch (final WhitelistException e) {
      assertThat(e.getMessage()).contains("3.2.3.a-3.2.3.10 is not an Ipv4 string literal.");
    }
  }

  @Test
  public void testThatInvalidCidrThrowsException() {
    final WhitelistItem whitelistItem = new WhitelistItem("3.2.3.1/a");
    whitelistItem.setHmacEnabled(false);

    final Map<String, List<String>> headers = new HashMap<>();
    final String postContent = "";

    final boolean enabled = true;
    final Whitelist whitelist = new Whitelist(enabled, Arrays.asList(whitelistItem));

    try {
      doVerifyWhitelist("1.1.1.1", headers, postContent, whitelist);
      fail("No exception");
    } catch (final WhitelistException e) {
      assertThat(e.getMessage()).contains("3.2.3.1/a cannot be parsed as Ipv4 string literal");
    }
  }

  @Test
  public void testThatInvalidStaticThrowsException() {
    final WhitelistItem whitelistItem = new WhitelistItem("3.2.3.a");
    whitelistItem.setHmacEnabled(false);

    final Map<String, List<String>> headers = new HashMap<>();
    final String postContent = "";

    final boolean enabled = true;
    final Whitelist whitelist = new Whitelist(enabled, Arrays.asList(whitelistItem));

    try {
      doVerifyWhitelist("1.1.1.1", headers, postContent, whitelist);
      fail("No exception");
    } catch (final WhitelistException e) {
      assertThat(e.getMessage()).contains("3.2.3.a is not a valid IP string literal");
    }
  }

  @Test
  public void testThatHostCanBeVerifiedWithStaticIp() {
    final WhitelistItem whitelistItem = new WhitelistItem("4.2.3.5");
    whitelistItem.setHmacEnabled(false);

    final Map<String, List<String>> headers = new HashMap<>();
    final String postContent = "";

    final boolean enabled = true;
    final Whitelist whitelist = new Whitelist(enabled, Arrays.asList(whitelistItem));

    assertThat(testDoVerifyWhitelist("4.2.3.4", headers, postContent, whitelist)).isFalse();
    assertThat(testDoVerifyWhitelist("4.2.3.5", headers, postContent, whitelist)).isTrue();
    assertThat(testDoVerifyWhitelist("4.2.3.6", headers, postContent, whitelist)).isFalse();
  }

  private boolean testDoVerifyWhitelist(
      final String remoteHost,
      final Map<String, List<String>> headers,
      final String postContent,
      final Whitelist whitelist) {
    try {
      doVerifyWhitelist(remoteHost, headers, postContent, whitelist);
      return true;
    } catch (final WhitelistException e) {
      Logger.getLogger(WhitelistVerifierTest.class.getSimpleName()).info(e.getMessage());
      return false;
    }
  }
}
