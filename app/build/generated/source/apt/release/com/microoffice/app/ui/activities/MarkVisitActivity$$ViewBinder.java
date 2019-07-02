// Generated code from Butter Knife. Do not modify!
package com.microoffice.app.ui.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MarkVisitActivity$$ViewBinder<T extends com.microoffice.app.ui.activities.MarkVisitActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296727, "field 'tbMarkVisit'");
    target.tbMarkVisit = finder.castView(view, 2131296727, "field 'tbMarkVisit'");
    view = finder.findRequiredView(source, 2131296326, "field 'btViewVisit'");
    target.btViewVisit = finder.castView(view, 2131296326, "field 'btViewVisit'");
    view = finder.findRequiredView(source, 2131296315, "field 'btAddVisit'");
    target.btAddVisit = finder.castView(view, 2131296315, "field 'btAddVisit'");
    view = finder.findRequiredView(source, 2131296736, "field 'textLocationName'");
    target.textLocationName = finder.castView(view, 2131296736, "field 'textLocationName'");
    view = finder.findRequiredView(source, 2131296739, "field 'textPurposeOfVisit'");
    target.textPurposeOfVisit = finder.castView(view, 2131296739, "field 'textPurposeOfVisit'");
    view = finder.findRequiredView(source, 2131296738, "field 'textPlaceOfVisit'");
    target.textPlaceOfVisit = finder.castView(view, 2131296738, "field 'textPlaceOfVisit'");
    view = finder.findRequiredView(source, 2131296601, "field 'pickerButton'");
    target.pickerButton = finder.castView(view, 2131296601, "field 'pickerButton'");
    view = finder.findRequiredView(source, 2131296627, "field 'rbArrived'");
    target.rbArrived = finder.castView(view, 2131296627, "field 'rbArrived'");
    view = finder.findRequiredView(source, 2131296629, "field 'rbDeparted'");
    target.rbDeparted = finder.castView(view, 2131296629, "field 'rbDeparted'");
  }

  @Override public void unbind(T target) {
    target.tbMarkVisit = null;
    target.btViewVisit = null;
    target.btAddVisit = null;
    target.textLocationName = null;
    target.textPurposeOfVisit = null;
    target.textPlaceOfVisit = null;
    target.pickerButton = null;
    target.rbArrived = null;
    target.rbDeparted = null;
  }
}
