package org.jenkinsci.plugins.gwt.whitelist;

import static org.jenkinsci.plugins.gwt.whitelist.HMACVerifier.hmacVerify;

import com.github.jgonian.ipmath.Ipv4;
import com.github.jgonian.ipmath.Ipv4Range;
import com.github.jgonian.ipmath.Ipv6;
import com.github.jgonian.ipmath.Ipv6Range;
import com.google.common.base.Optional;
import com.google.common.net.InetAddresses;
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

  /** Returns true if whitelistHost CIDR block contains remoteHost; supports ipv4/ipv6. */
  static boolean verifyCIDR(
      final String remoteHost, final String whitelistHostCIDR, final String whitelistHostIP)
      throws WhitelistException {

    int whitelistHostLength = InetAddresses.forString(whitelistHostIP).getAddress().length;
    int remoteHostLength = InetAddresses.forString(remoteHost).getAddress().length;
    if (whitelistHostLength == 4) {
      if (remoteHostLength == 4) {
        Ipv4Range whitelistHostRange = Ipv4Range.parse(whitelistHostCIDR);
        return whitelistHostRange.overlaps(Ipv4Range.parse(remoteHost + "/32"));
      }
    } else if (whitelistHostLength == 16) {
      if (remoteHostLength == 16) {
        Ipv6Range whitelistHostRange = Ipv6Range.parse(whitelistHostCIDR);
        return whitelistHostRange.overlaps(Ipv6Range.parse(remoteHost + "/128"));
      }
    }

    return false;
  }

  static boolean verifyIpv4(final String remoteHost, final String whitelistHost)
      throws WhitelistException {
    Ipv4 whitelistIP = Ipv4.parse(whitelistHost);
    return whitelistIP.equals(Ipv4.parse(remoteHost));
  }

  static boolean verifyIpv6(final String remoteHost, final String whitelistHost)
      throws WhitelistException {
    Ipv6 whitelistIP = Ipv6.parse(whitelistHost);
    return whitelistIP.equals(Ipv6.parse(remoteHost));
  }

  /** Returns true if whitelistHost is equal to remoteHost; supports ipv4/ipv6. */
  static boolean verifyIP(final String remoteHost, final String whitelistHost)
      throws WhitelistException {

    int whitelistHostLength = InetAddresses.forString(whitelistHost).getAddress().length;
    int remoteHostLength = InetAddresses.forString(remoteHost).getAddress().length;
    if (whitelistHostLength == 4) {
      if (remoteHostLength == 4) {
        return verifyIpv4(whitelistHost, remoteHost);
      }
    } else if (whitelistHostLength == 16) {
      if (remoteHostLength == 16) {
        return verifyIpv6(whitelistHost, remoteHost);
      }
    }

    return false;
  }

  /** Returns true if whitelistHost contains remoteHost; supports ip/cidr. */
  static Boolean whitelistContains(final String remoteHost, final String whitelistHost)
      throws WhitelistException {
    Boolean isMatched = false;

    if (whitelistHost.equalsIgnoreCase(remoteHost)) {
      isMatched = true;
    }

    Boolean isCIDR = false;
    Boolean isRange = false;

    String[] hostParts = whitelistHost.split("/");

    String whitelistHostIP;
    if (hostParts.length == 2) {
      whitelistHostIP = hostParts[0];
      isCIDR = true;
    } else {
      hostParts = whitelistHost.split("-");
      if (hostParts.length == 2) {
        isRange = true;
      }
    }

    if (isCIDR || isRange) {
      whitelistHostIP = hostParts[0];
      isMatched = verifyCIDR(remoteHost, whitelistHost, whitelistHostIP);
    } else {
      isMatched = verifyIP(remoteHost, whitelistHost);
    }

    return isMatched;
  }

  static void whitelistVerify(
      final String remoteHost,
      final WhitelistItem whitelistItem,
      final Map<String, List<String>> headers,
      final String postContent)
      throws WhitelistException {

    String whitelistHost = whitelistItem.getHost();

    if (whitelistContains(remoteHost, whitelistHost)) {
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
