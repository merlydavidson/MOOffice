// Generated code from Butter Knife. Do not modify!
package com.microoffice.app.ui.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SupportListActivity$$ViewBinder<T extends com.microoffice.app.ui.activities.SupportListActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296502, "field 'ivCrossWhite'");
    target.ivCrossWhite = finder.castView(view, 2131296502, "field 'ivCrossWhite'");
    view = finder.findRequiredView(source, 2131296525, "field 'llStartDate'");
    target.llStartDate = finder.castView(view, 2131296525, "field 'llStartDate'");
    view = finder.findRequiredView(source, 2131296520, "field 'llEndDate'");
    target.llEndDate = finder.castView(view, 2131296520, "field 'llEndDate'");
    view = finder.findRequiredView(source, 2131296799, "field 'tvStartDate'");
    target.tvStartDate = finder.castView(view, 2131296799, "field 'tvStartDate'");
    view = finder.findRequiredView(source, 2131296784, "field 'tvEndDate'");
    target.tvEndDate = finder.castView(view, 2131296784, "field 'tvEndDate'");
    view = finder.findRequiredView(source, 2131296444, "field 'etSearch'");
    target.etSearch = finder.castView(view, 2131296444, "field 'etSearch'");
    view = finder.findRequiredView(source, 2131296445, "field 'etSearchStatus'");
    target.etSearchStatus = finder.castView(view, 2131296445, "field 'etSearchStatus'");
    view = finder.findRequiredView(source, 2131296785, "field 'tvFilterReset'");
    target.tvFilterReset = finder.castView(view, 2131296785, "field 'tvFilterReset'");
    view = finder.findRequiredView(source, 2131296650, "field 'rvSupportList'");
    target.rvSupportList = finder.castView(view, 2131296650, "field 'rvSupportList'");
    view = finder.findRequiredView(source, 2131296521, "field 'llFilterData'");
    target.llFilterData = finder.castView(view, 2131296521, "field 'llFilterData'");
    view = finder.findRequiredView(source, 2131296466, "field 'frmCustomer'");
    target.frmCustomer = finder.castView(view, 2131296466, "field 'frmCustomer'");
    view = finder.findRequiredView(source, 2131296469, "field 'frmStatus'");
    target.frmStatus = finder.castView(view, 2131296469, "field 'frmStatus'");
  }

  @Override public void unbind(T target) {
    target.ivCrossWhite = null;
    target.llStartDate = null;
    target.llEndDate = null;
    target.tvStartDate = null;
    target.tvEndDate = null;
    target.etSearch = null;
    target.etSearchStatus = null;
    target.tvFilterReset = null;
    target.rvSupportList = null;
    target.llFilterData = null;
    target.frmCustomer = null;
    target.frmStatus = null;
  }
}
