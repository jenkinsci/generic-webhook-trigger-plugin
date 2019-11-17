package org.jenkinsci.plugins.gwt.whitelist;

import com.github.jgonian.ipmath.Ipv4;
import com.github.jgonian.ipmath.Ipv4Range;
import com.github.jgonian.ipmath.Ipv6;
import com.github.jgonian.ipmath.Ipv6Range;
import com.google.common.net.InetAddresses;

public class WhitelistHost {

  public static enum HOST_TYPE {
    CIDR,
    STATIC,
    ANY;
  }

  private HOST_TYPE hostType;
  private String whitelistHost;
  private String rangeLeftValue;
  private String rangeRightValue;
  private Ipv4 staticIpv4;
  private Ipv6 staticIpv6;
  private Ipv6Range rangeIpv6;
  private Ipv4Range rangeIpv4;

  public WhitelistHost(final String string) throws WhitelistException {
    if (string == null || string.trim().isEmpty()) {
      this.hostType = HOST_TYPE.ANY;
      this.whitelistHost = "";
      return;
    }
    this.hostType = HOST_TYPE.STATIC;
    this.whitelistHost = string.trim();

    String[] hostParts = whitelistHost.split("/");

    boolean isCidrRange = false;
    if (hostParts.length == 2) {
      hostType = HOST_TYPE.CIDR;
    } else {
      hostParts = whitelistHost.split("-");
      if (hostParts.length == 2) {
        isCidrRange = true;
        hostType = HOST_TYPE.CIDR;
      }
    }

    if (hostType == HOST_TYPE.CIDR) {
      int leftValueLength;
      try {
        leftValueLength = InetAddresses.forString(hostParts[0]).getAddress().length;
      } catch (final IllegalArgumentException e) {
        throw new WhitelistException(whitelistHost + " is not an Ipv4 string literal.");
      }
      if (leftValueLength == 4) {
        try {
          this.rangeIpv4 = Ipv4Range.parse(whitelistHost);
        } catch (final IllegalArgumentException e) {
          throw new WhitelistException(whitelistHost + " cannot be parsed as Ipv4 string literal.");
        }
      } else if (leftValueLength == 16) {
        try {
          this.rangeIpv6 = Ipv6Range.parse(whitelistHost);
        } catch (final IllegalArgumentException e) {
          throw new WhitelistException(whitelistHost + " cannot be parsed as Ipv6 string literal.");
        }
      }

      if (isCidrRange) {
        final String leftValue = hostParts[0];
        final String rightValue = hostParts[1];

        try {
          InetAddresses.isInetAddress(leftValue);
          this.rangeLeftValue = leftValue;
        } catch (final IllegalArgumentException e) {
          throw new WhitelistException(
              "Left part of range, " + leftValue + ", is not an IP string literal.");
        }
        try {
          InetAddresses.isInetAddress(rightValue);
          this.rangeRightValue = rightValue;
        } catch (final IllegalArgumentException e) {
          throw new WhitelistException(
              "Right part of range, " + rightValue + ", is not an IP string literal.");
        }
      }
    } else if (hostType == HOST_TYPE.STATIC) {
      int valueLength = -1;
      try {
        valueLength = InetAddresses.forString(hostParts[0]).getAddress().length;
      } catch (final IllegalArgumentException e) {
        throw new WhitelistException(whitelistHost + " is not a valid IP string literal.");
      }
      if (valueLength == 4) {
        try {
          this.staticIpv4 = Ipv4.parse(whitelistHost);
        } catch (final IllegalArgumentException e) {
          throw new WhitelistException(
              whitelistHost + " is not a valid Ipv4 string literal. " + e.getMessage());
        }
      } else if (valueLength == 16) {
        try {
          this.staticIpv6 = Ipv6.parse(whitelistHost);
        } catch (final IllegalArgumentException e) {
          throw new WhitelistException(
              whitelistHost + " is not a valid Ipv6 string literal. " + e.getMessage());
        }
      }

      if (!InetAddresses.isInetAddress(whitelistHost)) {
        throw new WhitelistException(whitelistHost + " is not a valid IP string literal.");
      }
    }
  }

  public HOST_TYPE getHostType() {
    return hostType;
  }

  public String getWhitelistHost() {
    return whitelistHost;
  }

  public Ipv4Range getRangeIpv4() {
    return rangeIpv4;
  }

  public Ipv6Range getRangeIpv6() {
    return rangeIpv6;
  }

  public String getRangeLeftValue() {
    return rangeLeftValue;
  }

  public String getRangeRightValue() {
    return rangeRightValue;
  }

  public Ipv4 getStaticIpv4() {
    return staticIpv4;
  }

  public Ipv6 getStaticIpv6() {
    return staticIpv6;
  }
}
