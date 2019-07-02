// Generated code from Butter Knife. Do not modify!
package com.microoffice.app.ui.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AttendanceListActivity$$ViewBinder<T extends com.microoffice.app.ui.activities.AttendanceListActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296502, "field 'ivCrossWhite'");
    target.ivCrossWhite = finder.castView(view, 2131296502, "field 'ivCrossWhite'");
    view = finder.findRequiredView(source, 2131296688, "field 'spOfficeNames'");
    target.spOfficeNames = finder.castView(view, 2131296688, "field 'spOfficeNames'");
    view = finder.findRequiredView(source, 2131296686, "field 'spEmpNames'");
    target.spEmpNames = finder.castView(view, 2131296686, "field 'spEmpNames'");
    view = finder.findRequiredView(source, 2131296304, "field 'att_frame'");
    target.att_frame = finder.castView(view, 2131296304, "field 'att_frame'");
    view = finder.findRequiredView(source, 2131296464, "field 'frame_emp_llt'");
    target.frame_emp_llt = finder.castView(view, 2131296464, "field 'frame_emp_llt'");
    view = finder.findRequiredView(source, 2131296525, "field 'llStartDate'");
    target.llStartDate = finder.castView(view, 2131296525, "field 'llStartDate'");
    view = finder.findRequiredView(source, 2131296520, "field 'llEndDate'");
    target.llEndDate = finder.castView(view, 2131296520, "field 'llEndDate'");
    view = finder.findRequiredView(source, 2131296799, "field 'tvStartDate'");
    target.tvStartDate = finder.castView(view, 2131296799, "field 'tvStartDate'");
    view = finder.findRequiredView(source, 2131296784, "field 'tvEndDate'");
    target.tvEndDate = finder.castView(view, 2131296784, "field 'tvEndDate'");
    view = finder.findRequiredView(source, 2131296785, "field 'tvFilterReset'");
    target.tvFilterReset = finder.castView(view, 2131296785, "field 'tvFilterReset'");
    view = finder.findRequiredView(source, 2131296643, "field 'rvAttendanceList'");
    target.rvAttendanceList = finder.castView(view, 2131296643, "field 'rvAttendanceList'");
    view = finder.findRequiredView(source, 2131296521, "field 'llFilterData'");
    target.llFilterData = finder.castView(view, 2131296521, "field 'llFilterData'");
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
