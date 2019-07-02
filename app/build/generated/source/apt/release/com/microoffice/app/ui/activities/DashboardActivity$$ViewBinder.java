// Generated code from Butter Knife. Do not modify!
package com.microoffice.app.ui.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class DashboardActivity$$ViewBinder<T extends com.microoffice.app.ui.activities.DashboardActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296723, "field 'tbDashboard'");
    target.tbDashboard = finder.castView(view, 2131296723, "field 'tbDashboard'");
  }

  @Override public void unbind(T target) {
    target.tbDashboard = null;
  }
}
