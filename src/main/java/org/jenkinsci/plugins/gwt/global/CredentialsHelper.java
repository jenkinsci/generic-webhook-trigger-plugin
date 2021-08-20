package org.jenkinsci.plugins.gwt.global;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Strings.isNullOrEmpty;

import com.cloudbees.plugins.credentials.CredentialsMatcher;
import com.cloudbees.plugins.credentials.CredentialsMatchers;
import com.cloudbees.plugins.credentials.CredentialsProvider;
import com.cloudbees.plugins.credentials.common.StandardListBoxModel;
import com.cloudbees.plugins.credentials.domains.DomainRequirement;
import com.google.common.base.Optional;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.model.Item;
import hudson.model.Queue;
import hudson.model.queue.Tasks;
import hudson.security.ACL;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import java.util.ArrayList;
import java.util.List;
import jenkins.model.Jenkins;
import org.acegisecurity.Authentication;
import org.jenkinsci.plugins.plaincredentials.StringCredentials;

public class CredentialsHelper {

  @SuppressFBWarnings("NP_NULL_PARAM_DEREF")
  public static ListBoxModel doFillCredentialsIdItems(final Item item, final String credentialsId) {
    final StandardListBoxModel result = new StandardListBoxModel();
    if (item == null) {
      if (!Jenkins.getInstance().hasPermission(Jenkins.ADMINISTER)) {
        return result.includeCurrentValue(credentialsId);
      }
    } else {
      if (!item.hasPermission(Item.EXTENDED_READ)
          && !item.hasPermission(CredentialsProvider.USE_ITEM)) {
        return result.includeCurrentValue(credentialsId);
      }
    }
    return result //
        .includeEmptyValue() //
        .includeMatchingAs(
            item instanceof Queue.Task ? Tasks.getAuthenticationOf((Queue.Task) item) : ACL.SYSTEM,
            item,
            StringCredentials.class,
            new ArrayList<DomainRequirement>(),
            CredentialsMatchers.anyOf(CredentialsMatchers.instanceOf(StringCredentials.class)))
        .includeCurrentValue(credentialsId);
  }

  public static FormValidation doCheckFillCredentialsId(final String credentialsId) {
    if (isNullOrEmpty(credentialsId)) {
      return FormValidation.ok();
    }
    if (!findCredentials(credentialsId).isPresent()) {
      return FormValidation.error("Cannot find currently selected credentials");
    }
    return FormValidation.ok();
  }

  public static Optional<StringCredentials> findCredentials(final String credentialsId) {
    return findCredentials(credentialsId, null);
  }

  public static Optional<StringCredentials> findCredentials(
      final String credentialsId, final Item item) {
    if (isNullOrEmpty(credentialsId)) {
      return absent();
    }
    final Authentication authentication = null;
    final ArrayList<DomainRequirement> domainRequirements = new ArrayList<>();
    final List<StringCredentials> lookupCredentials =
        CredentialsProvider.lookupCredentials(
            StringCredentials.class, item, authentication, domainRequirements);
    final CredentialsMatcher allOf =
        CredentialsMatchers.allOf(
            CredentialsMatchers.withId(credentialsId),
            CredentialsMatchers.anyOf(CredentialsMatchers.instanceOf(StringCredentials.class)));
    return fromNullable(CredentialsMatchers.firstOrNull(lookupCredentials, allOf));
  }
}
