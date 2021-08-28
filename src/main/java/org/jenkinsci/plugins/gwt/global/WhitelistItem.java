package org.jenkinsci.plugins.gwt.global;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import java.io.Serializable;
import org.jenkinsci.plugins.gwt.whitelist.WhitelistException;
import org.jenkinsci.plugins.gwt.whitelist.WhitelistHost;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

public class WhitelistItem extends AbstractDescribableImpl<WhitelistItem> implements Serializable {

  private static final long serialVersionUID = 1176246137502450635L;
  private String host;
  private boolean hmacEnabled;
  private String hmacHeader;
  private String hmacCredentialId;
  private String hmacAlgorithm;

  public WhitelistItem() {}

  @DataBoundConstructor
  public WhitelistItem(final String host) {
    this.host = host;
  }

  public String getHost() {
    if (this.host == null) {
      return null;
    }
    return this.host.trim();
  }

  public String getHmacAlgorithm() {
    return this.hmacAlgorithm;
  }

  @DataBoundSetter
  public void setHmacAlgorithm(final String hmacAlgorithm) {
    this.hmacAlgorithm = hmacAlgorithm;
  }

  public String getHmacCredentialId() {
    return this.hmacCredentialId;
  }

  @DataBoundSetter
  public void setHmacCredentialId(final String hmacCredentialId) {
    this.hmacCredentialId = hmacCredentialId;
  }

  public boolean isHmacEnabled() {
    return this.hmacEnabled;
  }

  @DataBoundSetter
  public void setHmacEnabled(final boolean hmacEnabled) {
    this.hmacEnabled = hmacEnabled;
  }

  public String getHmacHeader() {
    return this.hmacHeader;
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
      for (final WhitelistAlgorithm a : WhitelistAlgorithm.values()) {
        listBoxModel.add(a.getFullName());
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
     * See: https://wiki.jenkins.io/display/JENKINS/Form+Validation
     *
     * @param value
     * @return FormValidation
     */
    public FormValidation doCheckHost(@QueryParameter final String value) {
      try {
        new WhitelistHost(value);
        return FormValidation.ok();
      } catch (final WhitelistException e) {
        return FormValidation.error(e.getMessage());
      }
    }
  }
}
