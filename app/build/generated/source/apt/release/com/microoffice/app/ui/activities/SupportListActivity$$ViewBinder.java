// Generated code from Butter Knife. Do not modify!
package com.microoffice.app.ui.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SupportListActivity$$ViewBinder<T extends com.microoffice.app.ui.activities.SupportListActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296500, "field 'ivCrossWhite'");
    target.ivCrossWhite = finder.castView(view, 2131296500, "field 'ivCrossWhite'");
    view = finder.findRequiredView(source, 2131296523, "field 'llStartDate'");
    target.llStartDate = finder.castView(view, 2131296523, "field 'llStartDate'");
    view = finder.findRequiredView(source, 2131296518, "field 'llEndDate'");
    target.llEndDate = finder.castView(view, 2131296518, "field 'llEndDate'");
    view = finder.findRequiredView(source, 2131296797, "field 'tvStartDate'");
    target.tvStartDate = finder.castView(view, 2131296797, "field 'tvStartDate'");
    view = finder.findRequiredView(source, 2131296782, "field 'tvEndDate'");
    target.tvEndDate = finder.castView(view, 2131296782, "field 'tvEndDate'");
    view = finder.findRequiredView(source, 2131296442, "field 'etSearch'");
    target.etSearch = finder.castView(view, 2131296442, "field 'etSearch'");
    view = finder.findRequiredView(source, 2131296443, "field 'etSearchStatus'");
    target.etSearchStatus = finder.castView(view, 2131296443, "field 'etSearchStatus'");
    view = finder.findRequiredView(source, 2131296783, "field 'tvFilterReset'");
    target.tvFilterReset = finder.castView(view, 2131296783, "field 'tvFilterReset'");
    view = finder.findRequiredView(source, 2131296648, "field 'rvSupportList'");
    target.rvSupportList = finder.castView(view, 2131296648, "field 'rvSupportList'");
    view = finder.findRequiredView(source, 2131296519, "field 'llFilterData'");
    target.llFilterData = finder.castView(view, 2131296519, "field 'llFilterData'");
    view = finder.findRequiredView(source, 2131296464, "field 'frmCustomer'");
    target.frmCustomer = finder.castView(view, 2131296464, "field 'frmCustomer'");
    view = finder.findRequiredView(source, 2131296467, "field 'frmStatus'");
    target.frmStatus = finder.castView(view, 2131296467, "field 'frmStatus'");
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
