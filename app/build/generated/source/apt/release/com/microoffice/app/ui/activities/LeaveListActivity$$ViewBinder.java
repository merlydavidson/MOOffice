// Generated code from Butter Knife. Do not modify!
package com.microoffice.app.ui.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LeaveListActivity$$ViewBinder<T extends com.microoffice.app.ui.activities.LeaveListActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296684, "field 'spEmpNames'");
    target.spEmpNames = finder.castView(view, 2131296684, "field 'spEmpNames'");
    view = finder.findRequiredView(source, 2131296685, "field 'spLeaveStatus'");
    target.spLeaveStatus = finder.castView(view, 2131296685, "field 'spLeaveStatus'");
    view = finder.findRequiredView(source, 2131296523, "field 'llStartDate'");
    target.llStartDate = finder.castView(view, 2131296523, "field 'llStartDate'");
    view = finder.findRequiredView(source, 2131296410, "field 'emptyText'");
    target.emptyText = finder.castView(view, 2131296410, "field 'emptyText'");
    view = finder.findRequiredView(source, 2131296518, "field 'llEndDate'");
    target.llEndDate = finder.castView(view, 2131296518, "field 'llEndDate'");
    view = finder.findRequiredView(source, 2131296797, "field 'tvStartDate'");
    target.tvStartDate = finder.castView(view, 2131296797, "field 'tvStartDate'");
    view = finder.findRequiredView(source, 2131296782, "field 'tvEndDate'");
    target.tvEndDate = finder.castView(view, 2131296782, "field 'tvEndDate'");
    view = finder.findRequiredView(source, 2131296783, "field 'tvFilterReset'");
    target.tvFilterReset = finder.castView(view, 2131296783, "field 'tvFilterReset'");
    view = finder.findRequiredView(source, 2131296641, "field 'rvAttendanceList'");
    target.rvAttendanceList = finder.castView(view, 2131296641, "field 'rvAttendanceList'");
    view = finder.findRequiredView(source, 2131296519, "field 'llFilterData'");
    target.llFilterData = finder.castView(view, 2131296519, "field 'llFilterData'");
    view = finder.findRequiredView(source, 2131296757, "field 'toolbar'");
    target.toolbar = finder.castView(view, 2131296757, "field 'toolbar'");
    view = finder.findRequiredView(source, 2131296407, "field 'employeelayout'");
    target.employeelayout = finder.castView(view, 2131296407, "field 'employeelayout'");
  }

  @Override public void unbind(T target) {
    target.spEmpNames = null;
    target.spLeaveStatus = null;
    target.llStartDate = null;
    target.emptyText = null;
    target.llEndDate = null;
    target.tvStartDate = null;
    target.tvEndDate = null;
    target.tvFilterReset = null;
    target.rvAttendanceList = null;
    target.llFilterData = null;
    target.toolbar = null;
    target.employeelayout = null;
  }
}
