package com.microoffice.app.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.microoffice.app.R;
import com.microoffice.app.utils.AppUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import moffice.meta.com.molibrary.core.MOMain;
import moffice.meta.com.molibrary.core.Settings;
import moffice.meta.com.molibrary.core.MOUserManager;
import moffice.meta.com.molibrary.utility.CommonDataArea;
import moffice.meta.com.molibrary.utility.LogWriter;


public class AssociatesActivity extends AppCompatActivity {

    @Bind(R.id.tbAssociate)
    Toolbar tbAssociate;

    @Bind(R.id.btClose)
    Button btClose;

    @Bind(R.id.btSendRegRequet)
    Button btSendRegReq;

    @Bind(R.id.etUserName)
    EditText edUserName;

    @Bind(R.id.etMobileNo)
    EditText edMobileNum;

    @Bind(R.id.etPersonalMail)
    EditText edPersonalMail;

    @Bind(R.id.etCompanyMail)
    EditText edCompanyMail;

    @Bind(R.id.etSharedSecret)
    EditText edSharedSecret;

    @Bind(R.id.etStatusBox)
    EditText edStatusBox;

    private AdView mAdView;

    String userName;
    String mobile;
    String adminEmail;
    String persEmail;
    String sharedSecret;
    int waitCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associates);

        ButterKnife.bind(this);

        mAdView=(AdView)findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        Settings setting = MOMain.readSettings(getApplicationContext());
        if (tbAssociate != null) {
            setSupportActionBar(tbAssociate);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.tb_title_associate));
        }
        try {
            edCompanyMail.setEnabled(false);
            edSharedSecret.setEnabled(false);
            edStatusBox.setEnabled(false);
            edUserName.setEnabled(false);
            edMobileNum.setEnabled(false);
            edPersonalMail.setEnabled(false);
            if (CommonDataArea.isInstTypeSelected()) { //if registered allow edit
                userName = CommonDataArea.getCommonSettings().getName();
                mobile = CommonDataArea.getCommonSettings().getMobile();
                adminEmail = CommonDataArea.getCommonSettings().getAdminEmail();
                persEmail = CommonDataArea.getCommonSettings().getEmail();

                if ((!userName.contains("_NA_")) && (!mobile.contains("_NA_"))) {
                    edUserName.setText(userName);
                    edMobileNum.setText(mobile);
                    edCompanyMail.setText(adminEmail);
                    edPersonalMail.setText(persEmail);
                }
                edSharedSecret.setText(CommonDataArea.getCommonSettings().getSecret());

            }
            if (CommonDataArea.getCommonSettings().getRegReqStatus() == MOMain.MO_REGSTATUS_ASSOS_NOTSAVED) {
                //Search SMS
                edUserName.setEnabled(true);
                edMobileNum.setEnabled(true);
                edPersonalMail.setEnabled(true);
                try {
                    if (MOUserManager.readInvitationSMS(this)) {
                        adminEmail = CommonDataArea.getCommonSettings().getAdminEmail();
                        adminEmail=adminEmail.replace('_','@');
                        edCompanyMail.setText(adminEmail);
                        edSharedSecret.setText(CommonDataArea.getCommonSettings().getSecret());
                        edStatusBox.setText("Fill details and send reg request");
                    }else{
                        edCompanyMail.setEnabled(true);
                        edSharedSecret.setEnabled(true);
                        edStatusBox.setEnabled(true);
                    }
                }catch (Exception exp){
                    Toast.makeText(AssociatesActivity.this,exp.getMessage(),Toast.LENGTH_LONG).show();
                }
            } else if (CommonDataArea.getCommonSettings().getRegReqStatus() == MOMain.MO_REGSTATUS_ASSOS_REGISTERED) {
                edStatusBox.setEnabled(true);
                edStatusBox.setText("Registration Completed");
            }

            if (CommonDataArea.getCommonSettings().getRegReqStatus() != MOMain.MO_REGSTATUS_ASSOS_REGISTERED) {
                final Timer updateTimer = new Timer();
                updateTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            Settings setting = CommonDataArea.getCommonSettings();
                            ++waitCount;
                            if (setting.getRegReqStatus() == MOMain.MO_REGSTATUS_ASSOS_REGISTERED) {
                                updateTimer.cancel();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        edStatusBox.setEnabled(false);
                                        edStatusBox.setText(waitCount + ": Registration Completed");
                                    }
                                });
                            }

                            if (setting.getRegReqStatus() == MOMain.MO_REGSTATUS_ASSOS_PENDING) {
                                // updateTimer.cancel();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        edStatusBox.setEnabled(false);
                                        edStatusBox.setText(waitCount + ": Registration Pending");
                                    }
                                });
                            }

                            if (setting.getRegReqStatus() == MOMain.MO_REGSTATUS_ASSOS_REQSENT) {
                                //updateTimer.cancel();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        edStatusBox.setEnabled(false);
                                        edStatusBox.setText(waitCount + ": Registration Request Sent. Reply pending");
                                    }
                                });
                            }

                            if (setting.getRegReqStatus() == MOMain.MO_REGSTATUS_ASSOS_REJECTED) {
                                //updateTimer.cancel();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        edCompanyMail.setEnabled(true);
                                        edSharedSecret.setEnabled(true);
                                        edStatusBox.setEnabled(true);
                                        edUserName.setEnabled(true);
                                        edMobileNum.setEnabled(true);
                                        edPersonalMail.setEnabled(true);
                                        edStatusBox.setEnabled(true);
                                        String textSts = CommonDataArea.getRegErrorReason();
                                        edStatusBox.setText(waitCount + ":" + textSts);
                                    }
                                });
                            }
                        }
                        catch(Exception exp){
                        LogWriter.writeLogException("RegStatusUIUpdate",exp);
                        }
                    }
                }, 2000, 4000);
            }

        } catch (Exception exp) {
            LogWriter.writeLog("AssociateActivity", exp.getMessage());
        }
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Settings setting = MOMain.readSettings(getApplicationContext());
                if(setting.isRegStatus()) {
                    AppUtils.callToIntent(AssociatesActivity.this, DashboardActivity.class, true);
                }else finish();;
            }
        });

        btSendRegReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    userName = edUserName.getText().toString();
                    mobile = edMobileNum.getText().toString();
                    adminEmail = edCompanyMail.getText().toString();
                    persEmail = edPersonalMail.getText().toString();
                    sharedSecret = edSharedSecret.getText().toString();
                    if(userName.length()<4){
                        Toast.makeText(AssociatesActivity.this, "User name is required and must be longer than 4 charector", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if(persEmail.length()<4){
                        Toast.makeText(AssociatesActivity.this, "Personal Email is required and must be longer than 4 charector", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if(adminEmail.length()<5){
                        Toast.makeText(AssociatesActivity.this, "Admin Email is required and must be longer than 4 charector.\r\n This to be read from invitation SMS", Toast.LENGTH_LONG).show();
                        edCompanyMail.setEnabled(true);
                        edSharedSecret.setEnabled(true);
                        return;
                    }
                    if(sharedSecret.length()<4){
                        Toast.makeText(AssociatesActivity.this, "Shared secret is required and must be longer than 4 charector", Toast.LENGTH_LONG).show();
                        edCompanyMail.setEnabled(true);
                        edSharedSecret.setEnabled(true);
                        return;
                    }
                    if(adminEmail.contains("@")) adminEmail=adminEmail.replace('@','_');
                    Settings settings1 = CommonDataArea.getCommonSettings();
                    settings1.setName(userName);
                    settings1.setMobile(mobile);
                    settings1.setAdminEmail(adminEmail);
                    settings1.setEmail(persEmail);
                    settings1.setSecret(sharedSecret);

                    edCompanyMail.setEnabled(false);
                    edSharedSecret.setEnabled(false);
                    edStatusBox.setEnabled(false);
                    edUserName.setEnabled(false);
                    edMobileNum.setEnabled(false);
                    edPersonalMail.setEnabled(false);
                    MOMain.sendRegMesg(settings1);

                    MOMain.saveSettinsAssos(getApplicationContext(), settings1);
                    MOUserManager.sendRegistrationRequest((Context) AssociatesActivity.this, userName, mobile, persEmail, adminEmail, sharedSecret);

                    Toast.makeText(AssociatesActivity.this,"Registration Request Send",Toast.LENGTH_LONG).show();
                } catch (Exception exp) {

                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppUtils.callToIntent(AssociatesActivity.this,
                MainActivity.class, true);
    }
}
