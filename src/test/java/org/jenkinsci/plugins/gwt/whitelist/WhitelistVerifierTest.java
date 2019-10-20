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
}
