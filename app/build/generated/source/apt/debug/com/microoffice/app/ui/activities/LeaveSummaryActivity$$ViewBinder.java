// Generated code from Butter Knife. Do not modify!
package com.microoffice.app.ui.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LeaveSummaryActivity$$ViewBinder<T extends com.microoffice.app.ui.activities.LeaveSummaryActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296685, "field 'spEmpNames'");
    target.spEmpNames = finder.castView(view, 2131296685, "field 'spEmpNames'");
    view = finder.findRequiredView(source, 2131296414, "field 'emptyText'");
    target.emptyText = finder.castView(view, 2131296414, "field 'emptyText'");
    view = finder.findRequiredView(source, 2131296411, "field 'employeelayout'");
    target.employeelayout = finder.castView(view, 2131296411, "field 'employeelayout'");
    view = finder.findRequiredView(source, 2131296761, "field 'toolbarSummary'");
    target.toolbarSummary = finder.castView(view, 2131296761, "field 'toolbarSummary'");
    view = finder.findRequiredView(source, 2131296645, "field 'rvAttenedanceSummary'");
    target.rvAttenedanceSummary = finder.castView(view, 2131296645, "field 'rvAttenedanceSummary'");
    view = finder.findRequiredView(source, 2131296786, "field 'tvFilterReset'");
    target.tvFilterReset = finder.castView(view, 2131296786, "field 'tvFilterReset'");
    view = finder.findRequiredView(source, 2131296360, "field 'casualLeavetv'");
    target.casualLeavetv = finder.castView(view, 2131296360, "field 'casualLeavetv'");
    view = finder.findRequiredView(source, 2131296405, "field 'earnedLeavetv'");
    target.earnedLeavetv = finder.castView(view, 2131296405, "field 'earnedLeavetv'");
    view = finder.findRequiredView(source, 2131296536, "field 'lopLeavetv'");
    target.lopLeavetv = finder.castView(view, 2131296536, "field 'lopLeavetv'");
    view = finder.findRequiredView(source, 2131296764, "field 'totalLeavetv'");
    target.totalLeavetv = finder.castView(view, 2131296764, "field 'totalLeavetv'");
    view = finder.findRequiredView(source, 2131296410, "field 'emplyllt'");
    target.emplyllt = finder.castView(view, 2131296410, "field 'emplyllt'");
    view = finder.findRequiredView(source, 2131296328, "field 'fabRequestLeave'");
    target.fabRequestLeave = finder.castView(view, 2131296328, "field 'fabRequestLeave'");
    view = finder.findRequiredView(source, 2131296329, "field 'fabSummary'");
    target.fabSummary = finder.castView(view, 2131296329, "field 'fabSummary'");
  }

  @Override public void unbind(T target) {
    target.spEmpNames = null;
    target.emptyText = null;
    target.employeelayout = null;
    target.toolbarSummary = null;
    target.rvAttenedanceSummary = null;
    target.tvFilterReset = null;
    target.casualLeavetv = null;
    target.earnedLeavetv = null;
    target.lopLeavetv = null;
    target.totalLeavetv = null;
    target.emplyllt = null;
    target.fabRequestLeave = null;
    target.fabSummary = null;
  }
}
