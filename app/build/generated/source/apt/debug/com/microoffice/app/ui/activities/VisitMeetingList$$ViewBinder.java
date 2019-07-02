// Generated code from Butter Knife. Do not modify!
package com.microoffice.app.ui.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class VisitMeetingList$$ViewBinder<T extends com.microoffice.app.ui.activities.VisitMeetingList> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296649, "field 'rvMeetingList'");
    target.rvMeetingList = finder.castView(view, 2131296649, "field 'rvMeetingList'");
    view = finder.findRequiredView(source, 2131296321, "field 'btAddToMeetingList'");
    target.btAddToMeetingList = finder.castView(view, 2131296321, "field 'btAddToMeetingList'");
    view = finder.findRequiredView(source, 2131296829, "field 'txtPersonMet'");
    target.txtPersonMet = finder.castView(view, 2131296829, "field 'txtPersonMet'");
    view = finder.findRequiredView(source, 2131296830, "field 'txtPersonTitle'");
    target.txtPersonTitle = finder.castView(view, 2131296830, "field 'txtPersonTitle'");
    view = finder.findRequiredView(source, 2131296531, "field 'loDataEntry'");
    target.loDataEntry = finder.castView(view, 2131296531, "field 'loDataEntry'");
    view = finder.findRequiredView(source, 2131296551, "field 'mlFilterData'");
    target.mlFilterData = finder.castView(view, 2131296551, "field 'mlFilterData'");
    view = finder.findRequiredView(source, 2131296435, "field 'txtNote'");
    target.txtNote = finder.castView(view, 2131296435, "field 'txtNote'");
    view = finder.findRequiredView(source, 2131296732, "field 'tbMeetingList'");
    target.tbMeetingList = finder.castView(view, 2131296732, "field 'tbMeetingList'");
    view = finder.findRequiredView(source, 2131296828, "field 'txtNextDate'");
    target.txtNextDate = finder.castView(view, 2131296828, "field 'txtNextDate'");
  }

  @Override public void unbind(T target) {
    target.rvMeetingList = null;
    target.btAddToMeetingList = null;
    target.txtPersonMet = null;
    target.txtPersonTitle = null;
    target.loDataEntry = null;
    target.mlFilterData = null;
    target.txtNote = null;
    target.tbMeetingList = null;
    target.txtNextDate = null;
  }
}
