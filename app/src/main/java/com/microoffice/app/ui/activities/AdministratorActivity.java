package com.microoffice.app.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.microoffice.app.R;
import com.microoffice.app.utils.AppUtils;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import moffice.meta.com.molibrary.core.MOMain;
import moffice.meta.com.molibrary.core.Settings;
import moffice.meta.com.molibrary.database.DatabaseHelperORM;
import moffice.meta.com.molibrary.utility.CommonDataArea;
import moffice.meta.com.molibrary.utility.LogWriter;

import static moffice.meta.com.molibrary.core.MOMain.SHAREDPREF_REGISTERED;

public class AdministratorActivity extends AppCompatActivity {

    @Bind(R.id.tbAdministrator)
    Toolbar tbAdministrator;
    @Bind(R.id.btSave)
    Button btSave;

    @Bind(R.id.btClose)
    Button btClose;

    @Bind(R.id.btInvite)
    Button btInvite;

    @Bind(R.id.etAdminName)
    TextView edAdminName;

    @Bind(R.id.etMobileNo)
    TextView edAdminPhone;

    @Bind(R.id.etCompanyName)
    TextView edCompnayName;

    @Bind(R.id.etCompanyMail)
    TextView etCompanyMail;

    @Bind(R.id.etSharedSecret)
    TextView edSharedSecret;

    @Bind(R.id.etSharedSecretRetry)
    TextView edSharedSecretRetry;

    boolean regStatus;

    DatabaseHelperORM databaseHelper = null;
    MOMain moMain;

    private AdView mAdView;

    Calendar date = Calendar.getInstance();

    private View.OnClickListener mSaveBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            try {
                if (!edSharedSecret.getText().toString().contentEquals(edSharedSecretRetry.getText().toString())) {
                    Toast.makeText(AdministratorActivity.this, "Shared secrets mismatch", Toast.LENGTH_LONG).show();
                    return;
                }
                if(etCompanyMail.getText().length()<3){
                    Toast.makeText(AdministratorActivity.this, "Company mail is required", Toast.LENGTH_LONG).show();
                    return;
                }
                if(edSharedSecret.getText().length()<4){
                    Toast.makeText(AdministratorActivity.this, "Shared secret must be longer than 4", Toast.LENGTH_LONG).show();
                    return;
                }
                if(edAdminName.getText().length()<2){
                    Toast.makeText(AdministratorActivity.this, "Admin name required and must be longer than 2", Toast.LENGTH_LONG).show();
                    return;
                }
                if(edCompnayName.getText().length()<2){
                    Toast.makeText(AdministratorActivity.this, "Company name required and must be longer than 2", Toast.LENGTH_LONG).show();
                    return;
                }
                final Settings settings = MOMain.readSettings(getApplicationContext());
                settings.setName(edAdminName.getText().toString());
                settings.setMobile(edAdminPhone.getText().toString());
                settings.setCompany(edCompnayName.getText().toString());
                settings.setAdminEmail(etCompanyMail.getText().toString());
                settings.setSecret(edSharedSecret.getText().toString());
                settings.setRegStatus(true);

                moMain.saveSettingAdmin(AdministratorActivity.this, settings);
                //Save Login status here.
                moMain.savePreferenceBooleanValue(SHAREDPREF_REGISTERED, true);
                AppUtils.callToIntent(AdministratorActivity.this, DashboardActivity.class, true);

                MOMain.sendRegMesg(settings);
                CommonDataArea.closeHelper();
                Toast.makeText(AdministratorActivity.this, "Saved...", Toast.LENGTH_LONG).show();
            } catch (Exception exp) {
                LogWriter.writeLogException("Save Settings", exp);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);

        ButterKnife.bind(this);
        moMain = new MOMain(AdministratorActivity.this);
        mAdView=(AdView)findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        if (tbAdministrator != null) {
            setSupportActionBar(tbAdministrator);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.tb_title_administrator));
        }

        btSave.setOnClickListener(mSaveBtnListener);
        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void loadSetValues() {
        final Settings settings = MOMain.readSettings(getApplicationContext());
        regStatus = settings.isInstTypeSet();
        if (regStatus) {
            edAdminName.setText(settings.getName());
            edAdminPhone.setText(settings.getMobile());
            edCompnayName.setText(settings.getCompany());
            etCompanyMail.setText(settings.getEmail());
            edSharedSecret.setText(settings.getSecret());
        }
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
        AppUtils.callToIntent(AdministratorActivity.this,
                MainActivity.class, true);
    }

    // This is how, DatabaseHelper can be initialized for future use
    private DatabaseHelperORM getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelperORM.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*
         * You'll need this in your class to release the helper when done.
		 */
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }
}
