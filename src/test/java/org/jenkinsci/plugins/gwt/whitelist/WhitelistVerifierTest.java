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

    assertThat(doVerifyWhitelist(remoteHost, headers, postContent, whitelist)).isTrue();
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

    assertThat(doVerifyWhitelist(remoteHost, headers, postContent, whitelist)).isTrue();
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

    assertThat(doVerifyWhitelist(remoteHost, headers, postContent, whitelist)).isFalse();
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

    assertThat(doVerifyWhitelist("whateverhost0", headers, postContent, whitelist)).isFalse();
    assertThat(doVerifyWhitelist("whateverhost1", headers, postContent, whitelist)).isTrue();
    assertThat(doVerifyWhitelist("whateverhost2", headers, postContent, whitelist)).isTrue();
    assertThat(doVerifyWhitelist("whateverhost3", headers, postContent, whitelist)).isTrue();
    assertThat(doVerifyWhitelist("whateverhost4", headers, postContent, whitelist)).isFalse();
  }
}
