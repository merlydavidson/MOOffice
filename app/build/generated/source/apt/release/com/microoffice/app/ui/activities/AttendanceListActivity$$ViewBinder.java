// Generated code from Butter Knife. Do not modify!
package com.microoffice.app.ui.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AttendanceListActivity$$ViewBinder<T extends com.microoffice.app.ui.activities.AttendanceListActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296500, "field 'ivCrossWhite'");
    target.ivCrossWhite = finder.castView(view, 2131296500, "field 'ivCrossWhite'");
    view = finder.findRequiredView(source, 2131296686, "field 'spOfficeNames'");
    target.spOfficeNames = finder.castView(view, 2131296686, "field 'spOfficeNames'");
    view = finder.findRequiredView(source, 2131296684, "field 'spEmpNames'");
    target.spEmpNames = finder.castView(view, 2131296684, "field 'spEmpNames'");
    view = finder.findRequiredView(source, 2131296304, "field 'att_frame'");
    target.att_frame = finder.castView(view, 2131296304, "field 'att_frame'");
    view = finder.findRequiredView(source, 2131296462, "field 'frame_emp_llt'");
    target.frame_emp_llt = finder.castView(view, 2131296462, "field 'frame_emp_llt'");
    view = finder.findRequiredView(source, 2131296523, "field 'llStartDate'");
    target.llStartDate = finder.castView(view, 2131296523, "field 'llStartDate'");
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
  }

  @Override public void unbind(T target) {
    target.ivCrossWhite = null;
    target.spOfficeNames = null;
    target.spEmpNames = null;
    target.att_frame = null;
    target.frame_emp_llt = null;
    target.llStartDate = null;
    target.llEndDate = null;
    target.tvStartDate = null;
    target.tvEndDate = null;
    target.tvFilterReset = null;
    target.rvAttendanceList = null;
    target.llFilterData = null;
  }
}
