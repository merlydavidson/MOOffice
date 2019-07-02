// Generated code from Butter Knife. Do not modify!
package com.microoffice.app.ui.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MainActivity$$ViewBinder<T extends com.microoffice.app.ui.activities.MainActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296725, "field 'tbLoginType'");
    target.tbLoginType = finder.castView(view, 2131296725, "field 'tbLoginType'");
    view = finder.findRequiredView(source, 2131296635, "field 'rgInstallType'");
    target.rgInstallType = finder.castView(view, 2131296635, "field 'rgInstallType'");
    view = finder.findRequiredView(source, 2131296626, "field 'rbAdministrator'");
    target.rbAdministrator = finder.castView(view, 2131296626, "field 'rbAdministrator'");
    view = finder.findRequiredView(source, 2131296628, "field 'rbAssociate'");
    target.rbAssociate = finder.castView(view, 2131296628, "field 'rbAssociate'");
  }

  @Override public void unbind(T target) {
    target.tbLoginType = null;
    target.rgInstallType = null;
    target.rbAdministrator = null;
    target.rbAssociate = null;
  }
}
