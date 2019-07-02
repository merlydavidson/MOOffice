package com.microoffice.app.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.microoffice.app.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import butterknife.Bind;
import butterknife.ButterKnife;
import moffice.meta.com.molibrary.core.MOOfficeLocManager;
import moffice.meta.com.molibrary.models.dbmodels.Offices;

public class AddOfficeLocationActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 1;

    @Bind(R.id.tbAddOffice)
    Toolbar tbAddOffice;

    @Bind(R.id.tvDesc)
    TextView tvDesc;

    @Bind(R.id.btOfficeLoc)
    Button btOfficeLoc;

    @Bind(R.id.btOffice_saveLoc)
    Button btSaveLocation;

     @Bind(R.id.tvoffice_add_name)
     TextView textView_Office_name;

    private AdView mAdView;

    Dao<Offices, Integer> OfficesDao = null;

    Place place = null;

    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(30.704649, 76.717873), new LatLng(30.733315, 76.779418));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_office);

        ButterKnife.bind(this);
        mAdView=(AdView)findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        try {
            OfficesDao = getHelper().getOfficesDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (tbAddOffice != null) {
            setSupportActionBar(tbAddOffice);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.tb_add_office_loc));
        }

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        btOfficeLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                        btSaveLocation.setEnabled(true);
                        loadGooglePicker();
                }catch(Exception exp){

                }
            }
        });

        btSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOfficeLocation();
                btSaveLocation.setEnabled(false);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.action_done:
                //saveOfficeLocation();
                return true;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        loadGooglePicker();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("onConnectionFailed", connectionResult.getErrorMessage());
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                place = PlacePicker.getPlace(data, this);
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                String address = String.format("%s", place.getAddress());
                stBuilder.append("Name: ");
                stBuilder.append(placename);
                stBuilder.append("\n");
                stBuilder.append("Latitude: ");
                stBuilder.append(latitude);
                stBuilder.append("\n");
                stBuilder.append("Logitude: ");
                stBuilder.append(longitude);
                stBuilder.append("\n");
                stBuilder.append("Address: ");
                stBuilder.append(address);
                //tvDesc.setText(stBuilder.toString());
                //saveOfficeLocation();
                textView_Office_name.setText(placename);
                btSaveLocation.setEnabled(true);
            }
        }
    }

    private void loadGooglePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(AddOfficeLocationActivity.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void saveOfficeLocation() {
        if (place == null) {
            Toast.makeText(AddOfficeLocationActivity.this, "Please Add office first.",
                    Toast.LENGTH_LONG).show();
            return;
        }else {
            String placeNameChange = textView_Office_name.getText().toString();
            if(!placeNameChange.contentEquals(place.getName())) {
                if (!MOOfficeLocManager.createOfficeLocation(place, placeNameChange,this)) {
                    Toast.makeText(AddOfficeLocationActivity.this, "Failed to save office location.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AddOfficeLocationActivity.this, "Office location Saved",
                            Toast.LENGTH_LONG).show();
                }
            }else{
                if (!MOOfficeLocManager.createOfficeLocation(place, null,this)) {
                    Toast.makeText(AddOfficeLocationActivity.this, "Failed to save office location.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AddOfficeLocationActivity.this, "Office location Saved",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
