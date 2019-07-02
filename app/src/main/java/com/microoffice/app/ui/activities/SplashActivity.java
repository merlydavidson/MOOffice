package com.microoffice.app.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.LogWriter;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;
import com.microoffice.app.R;
import com.microoffice.app.utils.AppUtils;
import com.microoffice.app.utils.MyExceptionHandler;

import moffice.meta.com.molibrary.core.MODataDispatchThread;
import moffice.meta.com.molibrary.core.MOMain;
import moffice.meta.com.molibrary.database.MODatabase;
import moffice.meta.com.molibrary.utility.CommonDataArea;

import static moffice.meta.com.molibrary.utility.CommonDataArea.dispThreadRel;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_TIME = 2000;

    MOMain prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler());
        MobileAds.initialize(this, getString(R.string.ad_mob_addid));
        prefsManager = new MOMain(SplashActivity.this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                   new MODatabase(SplashActivity.this);
                    MOMain.init(getApplicationContext());

                    if(CommonDataArea.isRegistered()) {

                        AppUtils.callToIntent(SplashActivity.this, DashboardActivity.class, true);
                        MODataDispatchThread dispThread = new MODataDispatchThread(dispThreadRel);
                        dispThread.setBaseContext(getApplicationContext());
                        dispThread.start();
                    }
                    else{
                        if(!CommonDataArea.isInstTypeSelected()) {
                            AppUtils.callToIntent(SplashActivity.this, MainActivity.class, true);
                        }else if(CommonDataArea.getInstType()== MOMain.MO_INSTTYPE_ASSOS){
                            AppUtils.callToIntent(SplashActivity.this, AssociatesActivity.class, true);

                        }else if(CommonDataArea.getInstType()==MOMain.MO_INSTTYPE_ADMIN){
                            AppUtils.callToIntent(SplashActivity.this, AdministratorActivity.class, true);

                        }
                    }
                    } catch (Exception exp){
                    moffice.meta.com.molibrary.utility.LogWriter.writeLogException("Splash", exp);

                }

            }
        }, SPLASH_TIME);
    }
}
