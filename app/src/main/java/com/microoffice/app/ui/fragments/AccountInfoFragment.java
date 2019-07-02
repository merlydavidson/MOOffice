package com.microoffice.app.ui.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.microoffice.app.R;
import com.microoffice.app.utils.ExportImportDB;

import moffice.meta.com.molibrary.core.MOMain;
import moffice.meta.com.molibrary.core.Settings;
import moffice.meta.com.molibrary.database.DatabaseHelperORM;
import moffice.meta.com.molibrary.utility.CommonDataArea;

/**
 * Created by user on 12/15/2017.
 */

public class AccountInfoFragment extends Fragment{
    TextView UserName,CompanyName,Mobile,Email,Version;
    ImageView UserImage;
    Settings commonSettings;
    AdView mAdView;
    int numPresses =0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context con=getContext();
        View rootView = inflater.inflate(R.layout.fragment_account_info, container, false);
        try {
            commonSettings = CommonDataArea.getCommonSettings();
        } catch (Exception e) {
            e.printStackTrace();
        }
        UserImage=(ImageView)rootView.findViewById(R.id.userImage);
        UserName=(TextView)rootView.findViewById(R.id.etName);
        CompanyName=(TextView)rootView.findViewById(R.id.etCmpny);
        Mobile=(TextView)rootView.findViewById(R.id.etMobile);
        Email=(TextView)rootView.findViewById(R.id.etEmail);
        Version = (TextView)rootView.findViewById(R.id.etVersion);
        String versionName="NA";
        int versionCode=0;
        mAdView = (AdView)rootView. findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        try {
            versionName = con.getPackageManager().getPackageInfo(con.getPackageName(), 0).versionName;
            versionCode = con.getPackageManager().getPackageInfo(con.getPackageName(), 0).versionCode;
        }catch (Exception ex){

        }
        UserName.setText(commonSettings.getName());
        CompanyName.setText(commonSettings.getCompany());
        Mobile.setText(commonSettings.getMobile());
        Email.setText(commonSettings.getEmail());
        UserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //To make this installation as the admin type press 10 times
                try {
                    if (CommonDataArea.getInstType() == MOMain.MO_INSTTYPE_ADMIN) {
                        if(numPresses<10) {
                            ++numPresses;
                            Toast.makeText(AccountInfoFragment.this.getContext(), "Pressed " + numPresses + " times", Toast.LENGTH_LONG).show();
                        }else{
                            MOMain.setInstalledAsAdmin(AccountInfoFragment.this.getContext());
                        }
                    }

                } catch (Exception exp) {

                }
            }
        });
        Version.setText("VersionName : "+versionName+" VersionCode : "+versionCode);
        Button importDB = (Button) (TextView)rootView.findViewById(R.id.buttonImpDB);
        importDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Cation !!!!This will cause overwrite of existing database !!!");
                alertDialogBuilder.setMessage("Keep the copy of database to import in MicroOffice folder with Name MicroOffice1.db");
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(ExportImportDB.importDB()) {
                            Toast.makeText(AccountInfoFragment.this.getContext(),"Database Import Successfull",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(AccountInfoFragment.this.getContext(),"Database Import Failed",Toast.LENGTH_LONG).show();

                        }
                    }
                });
                alertDialogBuilder.setNegativeButton("Cancel",null);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
        Button cleanDB = (Button) (TextView)rootView.findViewById(R.id.buttonVacuumDB);
        cleanDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(AccountInfoFragment.this.getContext())
                        .setTitle("Clean DB")
                        .setMessage("Clean DB")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                VacuumTask vacTask = new VacuumTask();
                                vacTask.execute();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        return rootView;
    }
    class VacuumTask extends AsyncTask<String, Void, String>
    {

        ProgressDialog pd;
        protected String doInBackground(String... params) {
            DatabaseHelperORM orm = new DatabaseHelperORM(AccountInfoFragment.this.getContext());
            orm.DoVacuum();
            SharedPreferences settings = AccountInfoFragment.this.getContext().getSharedPreferences(MOMain.SHAREDPREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = settings.edit();
            ed.putLong(MOMain.SHAREDPREF_LAST_VACUUM_TIME,System.currentTimeMillis());
            ed.commit();
            return null;
        }


        protected void onPostExecute(String result) {
            pd.cancel();
        }


        protected void onPreExecute() {
            pd = new ProgressDialog(AccountInfoFragment.this.getContext());
            pd.setMessage("Cleaning");
            pd.show();
        }


        protected void onProgressUpdate(Void... values) {
        }
    }
}
