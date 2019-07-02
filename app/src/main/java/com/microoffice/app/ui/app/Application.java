package com.microoffice.app.ui.app;

import com.microoffice.app.utils.MyExceptionHandler;

import moffice.meta.com.molibrary.utility.CommonDataArea;

/**
 * Created by Administrator on 01/06/2018.
 */

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Parse SDK stuff goes here
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if(CommonDataArea.dispThread!=null)
        CommonDataArea.dispThread.setTerminate(true);
        //Parse SDK stuff goes here
    }


}
