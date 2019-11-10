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

  public static void verifyWhitelist(
      final String remoteHost, final Map<String, List<String>> headers, final String postContent)
      throws WhitelistException {
    final Whitelist whitelist = Whitelist.get();
    doVerifyWhitelist(remoteHost, headers, postContent, whitelist);
  }

  static void doVerifyWhitelist(
      final String remoteHost,
      final Map<String, List<String>> headers,
      final String postContent,
      final Whitelist whitelist)
      throws WhitelistException {
    if (whitelist.getWhitelistItems().isEmpty() || !whitelist.isEnabled()) {
      return;
    }
    final StringBuilder messages = new StringBuilder();
    int i = 0;
    for (final WhitelistItem whitelistItem : whitelist.getWhitelistItems()) {
      i++;
      try {
        whitelistVerify(remoteHost, whitelistItem, headers, postContent);
        return;
      } catch (final WhitelistException e) {
        messages.append(i + ") " + e.getMessage() + "\n");
      }
    }
    final String messagesString = messages.toString();
    throw new WhitelistException("Did not find a matching whitelisted host:\n" + messagesString);
  }

  static void whitelistVerify(
      final String remoteHost,
      final WhitelistItem whitelistItem,
      final Map<String, List<String>> headers,
      final String postContent)
      throws WhitelistException {

    String whitelistHost = whitelistItem.getHost();

    if (HostVerifier.whitelistContains(remoteHost, whitelistHost)) {
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
        hmacVerify(headers, postContent, hmacHeader, hmacKey, hmacAlgorithm);
        return;
      }
      return;
    }
    throw new WhitelistException(
        "Sending host \"" + remoteHost + "\" was not matched by whitelist.");
  }
}
