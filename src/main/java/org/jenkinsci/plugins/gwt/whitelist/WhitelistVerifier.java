package org.jenkinsci.plugins.gwt.whitelist;

import static org.jenkinsci.plugins.gwt.whitelist.HMACVerifier.hmacVerify;

import com.google.common.base.Optional;
import java.util.List;
import java.util.Map;
import org.jenkinsci.plugins.gwt.global.CredentialsHelper;
import org.jenkinsci.plugins.gwt.global.Whitelist;
import org.jenkinsci.plugins.gwt.global.WhitelistItem;
import org.jenkinsci.plugins.plaincredentials.StringCredentials;

public class WhitelistVerifier {

  public static boolean verifyWhitelist(
      final String remoteHost, final Map<String, List<String>> headers, final String postContent) {
    final Whitelist whitelist = Whitelist.get();
    return doVerifyWhitelist(remoteHost, headers, postContent, whitelist);
  }

  static boolean doVerifyWhitelist(
      final String remoteHost,
      final Map<String, List<String>> headers,
      final String postContent,
      final Whitelist whitelist) {
    for (final WhitelistItem whitelistItem : whitelist.getWhitelistItems()) {
      if (!whitelistVerify(remoteHost, whitelistItem, headers, postContent)) {
        return false;
      }
    }
    return true;
  }

  static boolean whitelistVerify(
      final String remoteHost,
      final WhitelistItem whitelistItem,
      final Map<String, List<String>> headers,
      final String postContent) {
    if (whitelistItem.getHost().equalsIgnoreCase(remoteHost)) {
      if (whitelistItem.isHmacEnabled()) {
        final Optional<StringCredentials> hmacKeyOpt =
            CredentialsHelper.findCredentials(whitelistItem.getHmacCredentialId());
        if (!hmacKeyOpt.isPresent()) {
          throw new WhitelistException(
              "Was unable to find secret text credential " + whitelistItem.getHmacCredentialId());
        }
        final String hmacHeader = whitelistItem.getHmacHeader();
        final String hmacKey = hmacKeyOpt.get().getSecret().getPlainText();
        final String hmacAlgorithm = whitelistItem.getHmacAlgorithm();
        if (hmacVerify(headers, postContent, hmacHeader, hmacKey, hmacAlgorithm)) {
          return true;
        }
        return false;
      }
      return true;
    }
    return false;
  }
}
