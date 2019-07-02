// Generated code from Butter Knife. Do not modify!
package com.microoffice.app.ui.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AdministratorActivity$$ViewBinder<T extends com.microoffice.app.ui.activities.AdministratorActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296720, "field 'tbAdministrator'");
    target.tbAdministrator = finder.castView(view, 2131296720, "field 'tbAdministrator'");
    view = finder.findRequiredView(source, 2131296324, "field 'btSave'");
    target.btSave = finder.castView(view, 2131296324, "field 'btSave'");
    view = finder.findRequiredView(source, 2131296317, "field 'btClose'");
    target.btClose = finder.castView(view, 2131296317, "field 'btClose'");
    view = finder.findRequiredView(source, 2131296318, "field 'btInvite'");
    target.btInvite = finder.castView(view, 2131296318, "field 'btInvite'");
    view = finder.findRequiredView(source, 2131296423, "field 'edAdminName'");
    target.edAdminName = finder.castView(view, 2131296423, "field 'edAdminName'");
    view = finder.findRequiredView(source, 2131296435, "field 'edAdminPhone'");
    target.edAdminPhone = finder.castView(view, 2131296435, "field 'edAdminPhone'");
    view = finder.findRequiredView(source, 2131296427, "field 'edCompnayName'");
    target.edCompnayName = finder.castView(view, 2131296427, "field 'edCompnayName'");
    view = finder.findRequiredView(source, 2131296426, "field 'etCompanyMail'");
    target.etCompanyMail = finder.castView(view, 2131296426, "field 'etCompanyMail'");
    view = finder.findRequiredView(source, 2131296444, "field 'edSharedSecret'");
    target.edSharedSecret = finder.castView(view, 2131296444, "field 'edSharedSecret'");
    view = finder.findRequiredView(source, 2131296445, "field 'edSharedSecretRetry'");
    target.edSharedSecretRetry = finder.castView(view, 2131296445, "field 'edSharedSecretRetry'");
  }

  @Override public void unbind(T target) {
    target.tbAdministrator = null;
    target.btSave = null;
    target.btClose = null;
    target.btInvite = null;
    target.edAdminName = null;
    target.edAdminPhone = null;
    target.edCompnayName = null;
    target.etCompanyMail = null;
    target.edSharedSecret = null;
    target.edSharedSecretRetry = null;
  }
}
