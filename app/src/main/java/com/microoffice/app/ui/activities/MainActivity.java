package com.microoffice.app.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.microoffice.app.R;
import com.microoffice.app.ui.listeners.PermissionListener;
import com.microoffice.app.utils.AppUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import moffice.meta.com.molibrary.core.MOMain;
import moffice.meta.com.molibrary.utility.LogWriter;

public class MainActivity extends BaseActivity implements PermissionListener {

    @Bind(R.id.tbLoginType)
    Toolbar tbLoginType;

    @Bind(R.id.rgInstallType)
    RadioGroup rgInstallType;

    @Bind(R.id.rbAdministrator)
    RadioButton rbAdministrator;

    @Bind(R.id.rbAssociate)
    RadioButton rbAssociate;

    private RadioGroup.OnCheckedChangeListener mRadioListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {

            RadioButton radioSelection = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
            try {
                switch (radioSelection.getId()) {
                    case R.id.rbAdministrator:
                        MOMain.setInstallationType(MainActivity.this, MOMain.MO_INSTTYPE_ADMIN);
                        AppUtils.callToIntent(MainActivity.this, AdministratorActivity.class, true);
                        break;

                    case R.id.rbAssociate:
                        MOMain.setInstallationType(MainActivity.this, MOMain.MO_INSTTYPE_ASSOS);
                        AppUtils.callToIntent(MainActivity.this, AssociatesActivity.class, true);
                        break;
                }
            }catch(Exception exp){
                LogWriter.writeLog("MainActivity",exp.getMessage());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(MainActivity.this);

        if (tbLoginType != null) {
            setSupportActionBar(tbLoginType);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        }

        checkPermission((PermissionListener) MainActivity.this);

        rgInstallType.setOnCheckedChangeListener(mRadioListener);


    }

    @Override
    protected void onPause() {
        super.onPause();

        rgInstallType.setOnCheckedChangeListener(null);
        rbAdministrator.setChecked(false);
        rbAssociate.setChecked(false);
        rgInstallType.setOnCheckedChangeListener(mRadioListener);
    }

    @Override
    public void onSuccessListener() {

    }

    @Override
    public void onFailureListener() {

    }
}
