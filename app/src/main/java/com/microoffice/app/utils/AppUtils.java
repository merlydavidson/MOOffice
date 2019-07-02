package com.microoffice.app.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by com.moffice.com.microoffice.app on 31-07-2017.
 */

public class AppUtils {

    //    static SweetAlertDialog pDialog = null;
    static ProgressDialog pDialog = null;

    /**
     * Intent to next class i.e. Destination class.
     */
    public static void callToIntent(Activity mActivity, Class myDestinationClass, boolean isShouldFinish) {
        Intent intent = new Intent(mActivity, myDestinationClass);
        mActivity.startActivity(intent);

        if(isShouldFinish){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mActivity.finish();
        }
    }

    public static void showLoading(Context context){
//        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(R.color.colorprimary);
//        pDialog.setTitleText("Loading");
//        pDialog.setCancelable(false);
//        pDialog.show();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading contacts...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public static void hideLoading(){
        pDialog.dismiss();
    }

    public static void showAlertOk(Context context, String strTitle
            , String strMessage){
//        new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
//                .setTitleText(strTitle)
//                .setContentText(strMessage)
//                .show();
        Toast.makeText(context,strMessage,Toast.LENGTH_SHORT).show();
    }
}
