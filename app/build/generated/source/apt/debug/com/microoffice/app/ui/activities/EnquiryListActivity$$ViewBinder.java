// Generated code from Butter Knife. Do not modify!
package com.microoffice.app.ui.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class EnquiryListActivity$$ViewBinder<T extends com.microoffice.app.ui.activities.EnquiryListActivity> implements ViewBinder<T> {
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
    view = finder.findRequiredView(source, 2131296648, "field 'rvEnquiryList'");
    target.rvEnquiryList = finder.castView(view, 2131296648, "field 'rvEnquiryList'");
    view = finder.findRequiredView(source, 2131296521, "field 'llFilterData'");
    target.llFilterData = finder.castView(view, 2131296521, "field 'llFilterData'");
    view = finder.findRequiredView(source, 2131296467, "field 'frm_customer'");
    target.frm_customer = finder.castView(view, 2131296467, "field 'frm_customer'");
    view = finder.findRequiredView(source, 2131296468, "field 'frm_status'");
    target.frm_status = finder.castView(view, 2131296468, "field 'frm_status'");
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
    target.rvEnquiryList = null;
    target.llFilterData = null;
    target.frm_customer = null;
    target.frm_status = null;
  }
}
