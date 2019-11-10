package org.jenkinsci.plugins.gwt.whitelist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jenkinsci.plugins.gwt.whitelist.WhitelistVerifier.doVerifyWhitelist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jenkinsci.plugins.gwt.global.Whitelist;
import org.jenkinsci.plugins.gwt.global.WhitelistItem;
import org.junit.Test;

public class WhitelistVerifierTest {

  @Test
  public void testThatRequestIsValidWhenNoWhitelist() {
    final Map<String, List<String>> headers = new HashMap<String, List<String>>();
    final String postContent = "";

    final String remoteHost = "whateverhost";
    final boolean enabled = false;
    final Whitelist whitelist = new Whitelist(enabled, new ArrayList<WhitelistItem>());

    assertThat(testDoVerifyWhitelist(remoteHost, headers, postContent, whitelist)).isTrue();
  }

  @Test
  public void testThatHostCanBeVerifiedAndValid() {
    final WhitelistItem whitelistItem = new WhitelistItem("whateverhost");
    whitelistItem.setHmacEnabled(false);
    final Map<String, List<String>> headers = new HashMap<String, List<String>>();
    final String postContent = "";

    final String remoteHost = "whateverhost";
    final boolean enabled = true;
    final Whitelist whitelist = new Whitelist(enabled, Arrays.asList(whitelistItem));

    assertThat(testDoVerifyWhitelist(remoteHost, headers, postContent, whitelist)).isTrue();
  }

  @Test
  public void testThatHostCanBeVerifiedAndInvalid() {
    final WhitelistItem whitelistItem = new WhitelistItem("whateverhost");
    whitelistItem.setHmacEnabled(false);
    final Map<String, List<String>> headers = new HashMap<String, List<String>>();
    final String postContent = "";

    final String remoteHost = "anotherhost";
    final boolean enabled = true;
    final Whitelist whitelist = new Whitelist(enabled, Arrays.asList(whitelistItem));

    assertThat(testDoVerifyWhitelist(remoteHost, headers, postContent, whitelist)).isFalse();
  }

  @Test
  public void testThatHostIsAcceptedWhenWhitelistDisabled() {
    final WhitelistItem whitelistItem = new WhitelistItem("whateverhost");
    whitelistItem.setHmacEnabled(false);
    final Map<String, List<String>> headers = new HashMap<String, List<String>>();
    final String postContent = "";

    final String remoteHost = "anotherhost";
    final boolean enabled = false;
    final Whitelist whitelist = new Whitelist(enabled, Arrays.asList(whitelistItem));

    assertThat(testDoVerifyWhitelist(remoteHost, headers, postContent, whitelist)).isTrue();
  }

  @Test
  public void testThatHostCanBeVerifiedAndInvalidAndValidByAnotherItem() {
    final WhitelistItem whitelistItem1 = new WhitelistItem("whateverhost1");
    whitelistItem1.setHmacEnabled(false);

    final WhitelistItem whitelistItem2 = new WhitelistItem("whateverhost2");
    whitelistItem2.setHmacEnabled(false);

    final WhitelistItem whitelistItem3 = new WhitelistItem("whateverhost3");
    whitelistItem3.setHmacEnabled(false);

    final Map<String, List<String>> headers = new HashMap<String, List<String>>();
    final String postContent = "";

    final boolean enabled = true;
    final Whitelist whitelist =
        new Whitelist(enabled, Arrays.asList(whitelistItem1, whitelistItem2, whitelistItem3));

    assertThat(testDoVerifyWhitelist("whateverhost0", headers, postContent, whitelist)).isFalse();
    assertThat(testDoVerifyWhitelist("whateverhost1", headers, postContent, whitelist)).isTrue();
    assertThat(testDoVerifyWhitelist("whateverhost2", headers, postContent, whitelist)).isTrue();
    assertThat(testDoVerifyWhitelist("whateverhost3", headers, postContent, whitelist)).isTrue();
    assertThat(testDoVerifyWhitelist("whateverhost4", headers, postContent, whitelist)).isFalse();
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

    final Map<String, List<String>> headers = new HashMap<String, List<String>>();
    final String postContent = "";

    final boolean enabled = true;
    final Whitelist whitelist =
        new Whitelist(
            enabled,
            Arrays.asList(
                whitelistItem1, whitelistItem2, whitelistItem3, whitelistItem4, whitelistItem5));

    assertThat(testDoVerifyWhitelist("1.2.3.4", headers, postContent, whitelist)).isTrue();
    assertThat(testDoVerifyWhitelist("2.2.3.50", headers, postContent, whitelist)).isTrue();
    assertThat(testDoVerifyWhitelist("3.2.1.5", headers, postContent, whitelist)).isTrue();
    assertThat(testDoVerifyWhitelist("1.1.1.2", headers, postContent, whitelist)).isFalse();
    assertThat(
            testDoVerifyWhitelist(
                "2001:0db8:85a3:0000:0000:8a2e:0370:7334", headers, postContent, whitelist))
        .isTrue();
    assertThat(
            testDoVerifyWhitelist(
                "2002:0db8:85a3:0000:0000:8a2e:0370:7335", headers, postContent, whitelist))
        .isTrue();
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
      return false;
    }
  }
}
