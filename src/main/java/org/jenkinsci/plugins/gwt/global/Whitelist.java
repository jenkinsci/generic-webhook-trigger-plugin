package org.jenkinsci.plugins.gwt.global;

import com.google.common.annotations.VisibleForTesting;
import hudson.Extension;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

@Extension
public class Whitelist extends GlobalConfiguration implements Serializable {

  private static final long serialVersionUID = -2832851253933848205L;

  public static Whitelist get() {
    return GlobalConfiguration.all().get(Whitelist.class);
  }

  private boolean enabled;
  private List<WhitelistItem> whitelistItems = new ArrayList<>();

  @VisibleForTesting
  public Whitelist(final boolean enabled, final List<WhitelistItem> whitelistItems) {
    this.enabled = enabled;
    this.whitelistItems = whitelistItems;
  }

  public Whitelist() {
    load();
  }

  @Override
  public boolean configure(final StaplerRequest req, final JSONObject json) throws FormException {
    req.bindJSON(this, json);
    save();
    return true;
  }

  @DataBoundSetter
  public void setEnabled(final boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isEnabled() {
    return enabled;
  }

  @DataBoundSetter
  public void setWhitelistItems(final List<WhitelistItem> whitelistItems) {
    this.whitelistItems = whitelistItems;
  }

  public List<WhitelistItem> getWhitelistItems() {
    return whitelistItems;
  }
}
