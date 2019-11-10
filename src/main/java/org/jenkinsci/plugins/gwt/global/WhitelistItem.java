package org.jenkinsci.plugins.gwt.global;

import com.github.jgonian.ipmath.Ipv4;
import com.github.jgonian.ipmath.Ipv4Range;
import com.github.jgonian.ipmath.Ipv6;
import com.github.jgonian.ipmath.Ipv6Range;
import com.google.common.net.InetAddresses;
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
    return host;
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
  }

  private Boolean validateIpValue(String ipValue) {
    Boolean isValid = false;

    Boolean isCIDR = false;
    Boolean isRange = false;

    String[] hostParts = ipValue.split("/");

    if (hostParts.length == 2) {
      isCIDR = true;
    } else {
      hostParts = ipValue.split("-");
      if (hostParts.length == 2) {
        isRange = true;
      }
    }

    if (isCIDR || isRange) {
      int leftValueLength = InetAddresses.forString(hostParts[0]).getAddress().length;
      if (leftValueLength == 4) {
        if (Ipv4Range.parse(ipValue) != null) {
          isValid = true;
        }
      } else if (leftValueLength == 16) {
        if (Ipv6Range.parse(ipValue) != null) {
          isValid = true;
        }
      }
    } else {
      int ipValueLength = InetAddresses.forString(hostParts[0]).getAddress().length;
      if (ipValueLength == 4) {
        if (Ipv4.parse(ipValue) != null) {
          isValid = true;
        }
      } else if (ipValueLength == 16) {
        if (Ipv6.parse(ipValue) != null) {
          isValid = true;
        }
      }
    }

    return isValid;
  }

  public FormValidation doCheckHost(@QueryParameter String value) throws Exception {
    try {
      if (validateIpValue(value)) {
        return FormValidation.ok();
      } else {
        return FormValidation.error(
            "IP address must be in IPV4 or IPV6 CIDR or IP range notation.");
      }
    } catch (Exception e) {
      return FormValidation.error("Invalid IP address.");
    }
  }
}
