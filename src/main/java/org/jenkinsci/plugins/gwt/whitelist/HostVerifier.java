package org.jenkinsci.plugins.gwt.whitelist;

import com.github.jgonian.ipmath.Ipv4;
import com.github.jgonian.ipmath.Ipv4Range;
import com.github.jgonian.ipmath.Ipv6;
import com.github.jgonian.ipmath.Ipv6Range;
import com.google.common.net.InetAddresses;

public class HostVerifier {

  static boolean whitelistContains(final String remoteHost, final String whitelistHost)
      throws WhitelistException {
    if (whitelistHost.equalsIgnoreCase(remoteHost)) {
      return true;
    }

    String[] hostParts = whitelistHost.split("/");

    String whitelistHostIP;
    boolean isCIDR = false;
    boolean isRange = false;
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
      return verifyOverlaps(remoteHost, whitelistHost, whitelistHostIP);
    } else {
      return verifyIP(remoteHost, whitelistHost);
    }
  }

  private static boolean verifyOverlaps(
      final String remoteHost, final String whitelistHostCIDR, final String whitelistHostIP)
      throws WhitelistException {

    int whitelistHostLength = InetAddresses.forString(whitelistHostIP).getAddress().length;
    int remoteHostLength = InetAddresses.forString(remoteHost).getAddress().length;
    if (whitelistHostLength == 4) {
      if (remoteHostLength == 4) {
        try {
          Ipv4Range whitelistHostRange = Ipv4Range.parse(whitelistHostCIDR);
          if (whitelistHostRange.overlaps(Ipv4Range.parse(remoteHost + "/32"))) {
            return true;
          }
        } catch (IllegalArgumentException e) {
          throw new WhitelistException("Configured whitelist host is not an IP string literal.");
        }
        throw new WhitelistException(remoteHost + " did not match Ipv4 range.");
      } else {
        throw new WhitelistException(remoteHost + " is not Ipv4 but configured host is.");
      }
    } else if (whitelistHostLength == 16) {
      if (remoteHostLength == 16) {
        try {
          Ipv6Range whitelistHostRange = Ipv6Range.parse(whitelistHostCIDR);
          if (whitelistHostRange.overlaps(Ipv6Range.parse(remoteHost + "/128"))) {
            return true;
          }
        } catch (IllegalArgumentException e) {
          throw new WhitelistException("Configured whitelist host is not an IP string literal.");
        }
        throw new WhitelistException(remoteHost + " did not match Ipv6 range.");
      } else {
        throw new WhitelistException(remoteHost + " is not Ipv6 but configured host is.");
      }
    } else {
      throw new WhitelistException("Was unable to identify configured host as Ipv4 or Ipv6.");
    }
  }

  private static boolean verifyIpv4(final String remoteHost, final String whitelistHost)
      throws WhitelistException {
    Ipv4 whitelistIP = Ipv4.parse(whitelistHost);
    if (whitelistIP.equals(Ipv4.parse(remoteHost))) {
      return true;
    }
    throw new WhitelistException(remoteHost + " did not match statically defined Ipv4.");
  }

  private static boolean verifyIpv6(final String remoteHost, final String whitelistHost)
      throws WhitelistException {
    if (Ipv6.parse(whitelistHost).equals(Ipv6.parse(remoteHost))) {
      return true;
    }
    throw new WhitelistException(remoteHost + " did not match statically defined Ipv6.");
  }

  private static boolean verifyIP(final String remoteHost, final String whitelistHost)
      throws WhitelistException {
    int whitelistHostLength = -1;
    try {
      whitelistHostLength = InetAddresses.forString(whitelistHost).getAddress().length;
    } catch (IllegalArgumentException e) {
      throw new WhitelistException("Configured whitelist host is not an IP string literal.");
    }

    int remoteHostLength = -1;
    try {
      remoteHostLength = InetAddresses.forString(remoteHost).getAddress().length;
    } catch (IllegalArgumentException e) {
      throw new WhitelistException(remoteHost + " is not an IP string literal.");
    }

    if (whitelistHostLength == 4 && remoteHostLength == 4) {
      return verifyIpv4(remoteHost, whitelistHost);
    } else if (whitelistHostLength == 16 && remoteHostLength == 16) {
      return verifyIpv6(remoteHost, whitelistHost);
    }
    throw new WhitelistException(remoteHost + " did not match statically defined IP.");
  }
}
