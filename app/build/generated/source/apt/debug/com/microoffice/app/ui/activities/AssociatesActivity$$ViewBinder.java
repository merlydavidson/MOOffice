// Generated code from Butter Knife. Do not modify!
package com.microoffice.app.ui.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AssociatesActivity$$ViewBinder<T extends com.microoffice.app.ui.activities.AssociatesActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296723, "field 'tbAssociate'");
    target.tbAssociate = finder.castView(view, 2131296723, "field 'tbAssociate'");
    view = finder.findRequiredView(source, 2131296317, "field 'btClose'");
    target.btClose = finder.castView(view, 2131296317, "field 'btClose'");
    view = finder.findRequiredView(source, 2131296325, "field 'btSendRegReq'");
    target.btSendRegReq = finder.castView(view, 2131296325, "field 'btSendRegReq'");
    view = finder.findRequiredView(source, 2131296450, "field 'edUserName'");
    target.edUserName = finder.castView(view, 2131296450, "field 'edUserName'");
    view = finder.findRequiredView(source, 2131296437, "field 'edMobileNum'");
    target.edMobileNum = finder.castView(view, 2131296437, "field 'edMobileNum'");
    view = finder.findRequiredView(source, 2131296442, "field 'edPersonalMail'");
    target.edPersonalMail = finder.castView(view, 2131296442, "field 'edPersonalMail'");
    view = finder.findRequiredView(source, 2131296428, "field 'edCompanyMail'");
    target.edCompanyMail = finder.castView(view, 2131296428, "field 'edCompanyMail'");
    view = finder.findRequiredView(source, 2131296446, "field 'edSharedSecret'");
    target.edSharedSecret = finder.castView(view, 2131296446, "field 'edSharedSecret'");
    view = finder.findRequiredView(source, 2131296449, "field 'edStatusBox'");
    target.edStatusBox = finder.castView(view, 2131296449, "field 'edStatusBox'");
  }

  @Override public void unbind(T target) {
    target.tbAssociate = null;
    target.btClose = null;
    target.btSendRegReq = null;
    target.edUserName = null;
    target.edMobileNum = null;
    target.edPersonalMail = null;
    target.edCompanyMail = null;
    target.edSharedSecret = null;
    target.edStatusBox = null;
  }
}
