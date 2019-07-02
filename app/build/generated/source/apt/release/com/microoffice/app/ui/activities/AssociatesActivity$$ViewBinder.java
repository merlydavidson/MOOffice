// Generated code from Butter Knife. Do not modify!
package com.microoffice.app.ui.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AssociatesActivity$$ViewBinder<T extends com.microoffice.app.ui.activities.AssociatesActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296721, "field 'tbAssociate'");
    target.tbAssociate = finder.castView(view, 2131296721, "field 'tbAssociate'");
    view = finder.findRequiredView(source, 2131296317, "field 'btClose'");
    target.btClose = finder.castView(view, 2131296317, "field 'btClose'");
    view = finder.findRequiredView(source, 2131296325, "field 'btSendRegReq'");
    target.btSendRegReq = finder.castView(view, 2131296325, "field 'btSendRegReq'");
    view = finder.findRequiredView(source, 2131296448, "field 'edUserName'");
    target.edUserName = finder.castView(view, 2131296448, "field 'edUserName'");
    view = finder.findRequiredView(source, 2131296435, "field 'edMobileNum'");
    target.edMobileNum = finder.castView(view, 2131296435, "field 'edMobileNum'");
    view = finder.findRequiredView(source, 2131296440, "field 'edPersonalMail'");
    target.edPersonalMail = finder.castView(view, 2131296440, "field 'edPersonalMail'");
    view = finder.findRequiredView(source, 2131296426, "field 'edCompanyMail'");
    target.edCompanyMail = finder.castView(view, 2131296426, "field 'edCompanyMail'");
    view = finder.findRequiredView(source, 2131296444, "field 'edSharedSecret'");
    target.edSharedSecret = finder.castView(view, 2131296444, "field 'edSharedSecret'");
    view = finder.findRequiredView(source, 2131296447, "field 'edStatusBox'");
    target.edStatusBox = finder.castView(view, 2131296447, "field 'edStatusBox'");
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
