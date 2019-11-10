package org.jenkinsci.plugins.gwt.global;

import com.github.jgonian.ipmath.Ipv4;
import com.github.jgonian.ipmath.Ipv4Range;
import com.github.jgonian.ipmath.Ipv6;
import com.github.jgonian.ipmath.Ipv6Range;
import com.google.common.net.InetAddresses;
import com.google.common.net.InternetDomainName;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

public class WhitelistItem extends AbstractDescribableImpl<WhitelistItem> implements Serializable {

  public static final String HMAC_MD5 = "HmacMD5";
  public static final String HMAC_SHA1 = "HmacSHA1";
  public static final String HMAC_SHA256 = "HmacSHA256";
  private static final long serialVersionUID = 1176246137502450635L;
  private String host;
  private boolean hmacEnabled;
  private String hmacHeader;
  private String hmacCredentialId;
  private String hmacAlgorithm;
  private static final List<String> MAC_ALGORITHMS =
      Arrays.asList(HMAC_MD5, HMAC_SHA1, HMAC_SHA256);

  public WhitelistItem() {}

  @DataBoundConstructor
  public WhitelistItem(final String host) {
    this.host = host;
  }

  public String getHost() {
    if (host == null) return null;
    return host.trim();
  }

  public String getHmacAlgorithm() {
    return hmacAlgorithm;
  }

  @DataBoundSetter
  public void setHmacAlgorithm(final String hmacAlgorithm) {
    this.hmacAlgorithm = hmacAlgorithm;
  }

  public String getHmacCredentialId() {
    return hmacCredentialId;
  }

  @DataBoundSetter
  public void setHmacCredentialId(final String hmacCredentialId) {
    this.hmacCredentialId = hmacCredentialId;
  }

  public boolean isHmacEnabled() {
    return hmacEnabled;
  }

  @DataBoundSetter
  public void setHmacEnabled(final boolean hmacEnabled) {
    this.hmacEnabled = hmacEnabled;
  }

  public String getHmacHeader() {
    return hmacHeader;
  }

  @DataBoundSetter
  public void setHmacHeader(final String hmacHeader) {
    this.hmacHeader = hmacHeader;
  }

  @Extension
  public static class DescriptorImpl extends Descriptor<WhitelistItem> {
    @NonNull
    @Override
    public String getDisplayName() {
      return "Whitelist item";
    }

    public ListBoxModel doFillHmacAlgorithmItems() {
      final ListBoxModel listBoxModel = new ListBoxModel();
      for (final String a : MAC_ALGORITHMS) {
        listBoxModel.add(a);
      }
      return listBoxModel;
    }

    public ListBoxModel doFillHmacCredentialIdItems(
        @AncestorInPath final Item item, @QueryParameter final String credentialsId) {
      return CredentialsHelper.doFillCredentialsIdItems(item, credentialsId);
    }

    public FormValidation doCheckHmacCredentialIdItems(@QueryParameter final String value) {
      return CredentialsHelper.doCheckFillCredentialsId(value);
    }

    /**
     * Returns true if the provided value is a valid ipv4/ipv6 string.
     *
     * @param value
     * @return boolean
     */
    private boolean validateIpValue(final String value) {
      boolean isValid = false;

      boolean isCIDR = false;
      boolean isRange = false;

      String[] hostParts = value.split("/");

      if (hostParts.length == 2) {
        isCIDR = true;
      } else {
        hostParts = value.split("-");
        if (hostParts.length == 2) {
          isRange = true;
        }
      }

      if (isCIDR || isRange) {
        int leftValueLength = InetAddresses.forString(hostParts[0]).getAddress().length;
        if (leftValueLength == 4) {
          isValid = Ipv4Range.parse(value) != null;
        } else if (leftValueLength == 16) {
          isValid = Ipv6Range.parse(value) != null;
        }
      } else {
        int valueLength = InetAddresses.forString(hostParts[0]).getAddress().length;
        if (valueLength == 4) {
          isValid = Ipv4.parse(value) != null;
        } else if (valueLength == 16) {
          isValid = Ipv6.parse(value) != null;
        }
      }

      if (isValid && !value.contains("/") && !value.contains("-")) {
        isValid = InetAddresses.isInetAddress(value);
      }

      return isValid;
    }

    /**
     * See: https://wiki.jenkins.io/display/JENKINS/Form+Validation
     *
     * @param value
     * @return FormValidation
     */
    public FormValidation doCheckHost(@QueryParameter final String value) {
      try {
        boolean isValidDomain = InternetDomainName.isValid(value);

        boolean isValidIp = false;
        if (!isValidDomain) {
          isValidIp = validateIpValue(value);
        }

        if (isValidDomain || isValidIp) {
          return FormValidation.ok();
        }
      } catch (IllegalArgumentException e) {
        // NOTE: Generic error message avoids assuming domain/ip value type.
      }

      return FormValidation.error("Invalid domain, IP address, CIDR block or IP range.");
    }
  }
}
