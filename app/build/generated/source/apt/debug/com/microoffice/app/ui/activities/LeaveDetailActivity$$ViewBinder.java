// Generated code from Butter Knife. Do not modify!
package com.microoffice.app.ui.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LeaveDetailActivity$$ViewBinder<T extends com.microoffice.app.ui.activities.LeaveDetailActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296760, "field 'toolbar'");
    target.toolbar = finder.castView(view, 2131296760, "field 'toolbar'");
    view = finder.findRequiredView(source, 2131296644, "field 'rvAttendanceListDetail'");
    target.rvAttendanceListDetail = finder.castView(view, 2131296644, "field 'rvAttendanceListDetail'");
    view = finder.findRequiredView(source, 2131296413, "field 'emptyView'");
    target.emptyView = finder.castView(view, 2131296413, "field 'emptyView'");
    view = finder.findRequiredView(source, 2131296822, "field 'userName'");
    target.userName = finder.castView(view, 2131296822, "field 'userName'");
  }

  @Override public void unbind(T target) {
    target.toolbar = null;
    target.rvAttendanceListDetail = null;
    target.emptyView = null;
    target.userName = null;
  }
}
