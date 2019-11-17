package org.jenkinsci.plugins.gwt.whitelist;

import com.github.jgonian.ipmath.Ipv4Range;
import com.github.jgonian.ipmath.Ipv6Range;

public class HostVerifier {

  static boolean whitelistVerified(
      final WhitelistHost remoteHost, final WhitelistHost whitelistHost) throws WhitelistException {
    if (whitelistHost.getWhitelistHost().equalsIgnoreCase(remoteHost.getWhitelistHost())) {
      return true;
    }

    switch (whitelistHost.getHostType()) {
      case ANY:
        return true;
      case CIDR:
        return verifyCidr(remoteHost, whitelistHost);
      case STATIC:
        return verifyStatic(remoteHost, whitelistHost);
      default:
        throw new WhitelistException(
            "Did not identify type of " + whitelistHost.getWhitelistHost());
    }
  }

  private static boolean verifyStatic(
      final WhitelistHost remoteHost, final WhitelistHost whitelistHost) throws WhitelistException {
    if (whitelistHost.getStaticIpv4() != null && remoteHost.getStaticIpv4() != null) {
      if (whitelistHost.getStaticIpv4().equals(remoteHost.getStaticIpv4())) {
        return true;
      }
      throw new WhitelistException(
          remoteHost.getWhitelistHost() + " did not match statically defined Ipv4.");
    } else if (whitelistHost.getStaticIpv6() != null && remoteHost.getStaticIpv6() != null) {
      if (whitelistHost.getStaticIpv6().equals(remoteHost.getStaticIpv6())) {
        return true;
      }
      throw new WhitelistException(
          remoteHost.getWhitelistHost() + " did not match statically defined Ipv6.");
    }
    throw new WhitelistException(
        remoteHost.getWhitelistHost() + " is not of same IP version as statically defined IP.");
  }

  private static boolean verifyCidr(
      final WhitelistHost remoteHost, final WhitelistHost whitelistHost) throws WhitelistException {
    if (whitelistHost.getRangeIpv4() != null && remoteHost.getStaticIpv4() != null) {
      if (whitelistHost
          .getRangeIpv4()
          .overlaps(Ipv4Range.parse(remoteHost.getWhitelistHost() + "/32"))) {
        return true;
      }
      throw new WhitelistException(remoteHost.getWhitelistHost() + " did not match Ipv4 range.");
    } else if (whitelistHost.getRangeIpv6() != null && remoteHost.getStaticIpv6() != null) {
      if (whitelistHost
          .getRangeIpv6()
          .overlaps(Ipv6Range.parse(remoteHost.getWhitelistHost() + "/128"))) {
        return true;
      }
      throw new WhitelistException(remoteHost.getWhitelistHost() + " did not match Ipv6 range.");
    }
    throw new WhitelistException(
        remoteHost.getWhitelistHost() + " is not of same IP version as statically defined IP.");
  }
}
