// Generated code from Butter Knife. Do not modify!
package com.microoffice.app.ui.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class VisitListActivity$$ViewBinder<T extends com.microoffice.app.ui.activities.VisitListActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296502, "field 'ivCrossWhite'");
    target.ivCrossWhite = finder.castView(view, 2131296502, "field 'ivCrossWhite'");
    view = finder.findRequiredView(source, 2131296465, "field 'frameLayout'");
    target.frameLayout = finder.castView(view, 2131296465, "field 'frameLayout'");
    view = finder.findRequiredView(source, 2131296686, "field 'spEmpNames'");
    target.spEmpNames = finder.castView(view, 2131296686, "field 'spEmpNames'");
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
    view = finder.findRequiredView(source, 2131296652, "field 'rvVisitList'");
    target.rvVisitList = finder.castView(view, 2131296652, "field 'rvVisitList'");
    view = finder.findRequiredView(source, 2131296521, "field 'llFilterData'");
    target.llFilterData = finder.castView(view, 2131296521, "field 'llFilterData'");
  }

  @Override public void unbind(T target) {
    target.ivCrossWhite = null;
    target.frameLayout = null;
    target.spEmpNames = null;
    target.llStartDate = null;
    target.llEndDate = null;
    target.tvStartDate = null;
    target.tvEndDate = null;
    target.tvFilterReset = null;
    target.rvVisitList = null;
    target.llFilterData = null;
  }
}
