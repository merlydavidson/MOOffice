// Generated code from Butter Knife. Do not modify!
package com.microoffice.app.ui.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LeaveManagementActivity$$ViewBinder<T extends com.microoffice.app.ui.activities.LeaveManagementActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131296730, "field 'tbLeave'");
    target.tbLeave = finder.castView(view, 2131296730, "field 'tbLeave'");
    view = finder.findRequiredView(source, 2131296689, "field 'spTypeOfLeave'");
    target.spTypeOfLeave = finder.castView(view, 2131296689, "field 'spTypeOfLeave'");
    view = finder.findRequiredView(source, 2131296319, "field 'leaveRequest'");
    target.leaveRequest = finder.castView(view, 2131296319, "field 'leaveRequest'");
    view = finder.findRequiredView(source, 2131296525, "field 'llStartDate'");
    target.llStartDate = finder.castView(view, 2131296525, "field 'llStartDate'");
    view = finder.findRequiredView(source, 2131296511, "field 'leaveDays'");
    target.leaveDays = finder.castView(view, 2131296511, "field 'leaveDays'");
    view = finder.findRequiredView(source, 2131296799, "field 'tvStartDate'");
    target.tvStartDate = finder.castView(view, 2131296799, "field 'tvStartDate'");
    view = finder.findRequiredView(source, 2131296407, "field 'leaveReason'");
    target.leaveReason = finder.castView(view, 2131296407, "field 'leaveReason'");
  }

  @Override public void unbind(T target) {
    target.tbLeave = null;
    target.spTypeOfLeave = null;
    target.leaveRequest = null;
    target.llStartDate = null;
    target.leaveDays = null;
    target.tvStartDate = null;
    target.leaveReason = null;
  }
}
