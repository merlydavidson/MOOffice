// Generated code from Butter Knife. Do not modify!
package com.microoffice.app.ui.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MarkAttendanceActivity$$ViewBinder<T extends com.microoffice.app.ui.activities.MarkAttendanceActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296726, "field 'tbMarkAttendance'");
    target.tbMarkAttendance = finder.castView(view, 2131296726, "field 'tbMarkAttendance'");
    view = finder.findRequiredView(source, 2131296617, "field 'todayPlanEdt'");
    target.todayPlanEdt = finder.castView(view, 2131296617, "field 'todayPlanEdt'");
    view = finder.findRequiredView(source, 2131296615, "field 'placeEdt'");
    target.placeEdt = finder.castView(view, 2131296615, "field 'placeEdt'");
    view = finder.findRequiredView(source, 2131296316, "field 'btAddendanceView'");
    target.btAddendanceView = finder.castView(view, 2131296316, "field 'btAddendanceView'");
    view = finder.findRequiredView(source, 2131296320, "field 'btMarkAttendance'");
    target.btMarkAttendance = finder.castView(view, 2131296320, "field 'btMarkAttendance'");
    view = finder.findRequiredView(source, 2131296737, "field 'txtOfficeName'");
    target.txtOfficeName = finder.castView(view, 2131296737, "field 'txtOfficeName'");
    view = finder.findRequiredView(source, 2131296634, "field 'radioGroup'");
    target.radioGroup = finder.castView(view, 2131296634, "field 'radioGroup'");
    view = finder.findRequiredView(source, 2131296630, "field 'rbIN'");
    target.rbIN = finder.castView(view, 2131296630, "field 'rbIN'");
    view = finder.findRequiredView(source, 2131296631, "field 'rbOUT'");
    target.rbOUT = finder.castView(view, 2131296631, "field 'rbOUT'");
  }

  @Override public void unbind(T target) {
    target.tbMarkAttendance = null;
    target.todayPlanEdt = null;
    target.placeEdt = null;
    target.btAddendanceView = null;
    target.btMarkAttendance = null;
    target.txtOfficeName = null;
    target.radioGroup = null;
    target.rbIN = null;
    target.rbOUT = null;
  }
}
