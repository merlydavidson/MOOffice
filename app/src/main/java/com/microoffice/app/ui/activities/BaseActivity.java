package com.microoffice.app.ui.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.microoffice.app.ui.listeners.PermissionListener;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import moffice.meta.com.molibrary.database.DatabaseHelperORM;

/**
 * Created by com.moffice.com.microoffice.app on 18-09-2017.
 */

public class BaseActivity extends AppCompatActivity {

    private PermissionListener mPermissionListener = null;

    private final int READ_CALL_LOGS = 101;
    private final int READ_CONTACTS = 102;
    private final int SEND_SMS = 103;
    private final int WRITE_CONTACTS = 104;
    private final int ACCESS_FINE_LOCATION = 105;
    private final int WRITE_STORAGE = 106;

    DatabaseHelperORM databaseHelperORM = null;

    public final String[] ANDROID_PERMISSIONS = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void checkPermission(PermissionListener mPermissionListener){

        this.mPermissionListener = mPermissionListener;

        if (ContextCompat.checkSelfPermission(BaseActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BaseActivity.this,
                    ANDROID_PERMISSIONS, WRITE_STORAGE);
        }else if (ContextCompat.checkSelfPermission(BaseActivity.this,
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BaseActivity.this,
                    ANDROID_PERMISSIONS, SEND_SMS);
        }
        else if (ContextCompat.checkSelfPermission(BaseActivity.this,
                Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BaseActivity.this,
                    ANDROID_PERMISSIONS, WRITE_CONTACTS);
        }
        else if (ContextCompat.checkSelfPermission(BaseActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BaseActivity.this,
                    ANDROID_PERMISSIONS, ACCESS_FINE_LOCATION);
        }else if (ContextCompat.checkSelfPermission(BaseActivity.this,
                Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BaseActivity.this,
                    ANDROID_PERMISSIONS, READ_CALL_LOGS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_CONTACTS:
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                   // Toast.makeText(this, "Cannot open epub it needs storage access !", Toast.LENGTH_SHORT).show();
                    mPermissionListener.onFailureListener();
                }else{
                    mPermissionListener.onSuccessListener();
                }
                break;

            case SEND_SMS:
                if (grantResults[1] == PackageManager.PERMISSION_DENIED) {
                    // Toast.makeText(this, "Cannot open epub it needs storage access !", Toast.LENGTH_SHORT).show();
                    mPermissionListener.onFailureListener();
                }else{
                    mPermissionListener.onSuccessListener();
                }
                break;

            case WRITE_CONTACTS:
                if (grantResults[2] == PackageManager.PERMISSION_DENIED) {
                    // Toast.makeText(this, "Cannot open epub it needs storage access !", Toast.LENGTH_SHORT).show();
                    mPermissionListener.onFailureListener();
                }else{
                    mPermissionListener.onSuccessListener();
                }
                break;

            case ACCESS_FINE_LOCATION:
                if (grantResults[3] == PackageManager.PERMISSION_DENIED) {
                    // Toast.makeText(this, "Cannot open epub it needs storage access !", Toast.LENGTH_SHORT).show();
                    mPermissionListener.onFailureListener();
                }else{
                    mPermissionListener.onSuccessListener();
                }
                break;

            case READ_CALL_LOGS:
                if (grantResults[4] == PackageManager.PERMISSION_DENIED) {
                    // Toast.makeText(this, "Cannot open epub it needs storage access !", Toast.LENGTH_SHORT).show();
                    mPermissionListener.onFailureListener();
                }else{
                    mPermissionListener.onSuccessListener();
                }
                break;
            case WRITE_STORAGE:
                if (grantResults[5] == PackageManager.PERMISSION_DENIED) {
                    // Toast.makeText(this, "Cannot open epub it needs storage access !", Toast.LENGTH_SHORT).show();
                    mPermissionListener.onFailureListener();
                }else{
                    mPermissionListener.onSuccessListener();
                }
                break;
        }
    }

    public DatabaseHelperORM getHelper() {
        if (databaseHelperORM == null) {
            databaseHelperORM = OpenHelperManager.getHelper(this, DatabaseHelperORM.class);
        }
        return databaseHelperORM;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelperORM != null) {
            OpenHelperManager.releaseHelper();
            databaseHelperORM = null;
        }
    }
}
