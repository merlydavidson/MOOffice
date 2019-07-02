// Generated code from Butter Knife. Do not modify!
package com.microoffice.app.ui.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AddOfficeLocationActivity$$ViewBinder<T extends com.microoffice.app.ui.activities.AddOfficeLocationActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296718, "field 'tbAddOffice'");
    target.tbAddOffice = finder.castView(view, 2131296718, "field 'tbAddOffice'");
    view = finder.findRequiredView(source, 2131296780, "field 'tvDesc'");
    target.tvDesc = finder.castView(view, 2131296780, "field 'tvDesc'");
    view = finder.findRequiredView(source, 2131296322, "field 'btOfficeLoc'");
    target.btOfficeLoc = finder.castView(view, 2131296322, "field 'btOfficeLoc'");
    view = finder.findRequiredView(source, 2131296323, "field 'btSaveLocation'");
    target.btSaveLocation = finder.castView(view, 2131296323, "field 'btSaveLocation'");
    view = finder.findRequiredView(source, 2131296812, "field 'textView_Office_name'");
    target.textView_Office_name = finder.castView(view, 2131296812, "field 'textView_Office_name'");
  }

  @Override public void unbind(T target) {
    target.tbAddOffice = null;
    target.tvDesc = null;
    target.btOfficeLoc = null;
    target.btSaveLocation = null;
    target.textView_Office_name = null;
  }
}
