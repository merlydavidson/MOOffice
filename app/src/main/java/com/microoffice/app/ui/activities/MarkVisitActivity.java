package com.microoffice.app.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.microoffice.app.R;
import com.microoffice.app.utils.AppUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import moffice.meta.com.molibrary.core.MOVisitManager;

import static moffice.meta.com.molibrary.core.MOMain.SHAREDPREF_NAME;


public class MarkVisitActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {

    @Bind(R.id.tbMarkVisit)
    Toolbar tbMarkVisit;

    @Bind(R.id.btViewVisit)
    Button btViewVisit;

    @Bind(R.id.btAddVisit)
    Button btAddVisit;

    @Bind(R.id.textLocationName)
    TextView textLocationName;

    @Bind(R.id.textPurposeOfVisit)
    EditText textPurposeOfVisit;

    @Bind(R.id.textPlaceOfVisit)
    EditText textPlaceOfVisit;

    @Bind(R.id.pickerButton)
    ImageButton pickerButton;

//    @Bind(R.id.btn_refresh)
//    ImageButton refreshBtn;

    @Bind(R.id.rbArrived)
    RadioButton rbArrived;

    @Bind(R.id.rbDeparted)
    RadioButton rbDeparted;

    LocationManager locationManager;
    String bestProvider;
    String locationName;
    String placeOfVisit;
    Location location;
    String purpose;
    private Menu menu_item;
    public static SharedPreferences sh;
    public static SharedPreferences.Editor editor;
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final LatLngBounds BOUNDS_TECHNOPARK = new LatLngBounds(
            new LatLng(8.5572, 76.8765), new LatLng(8.5572, 76.8765));
    ImageButton btn_refresh;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_mark_visit);

            sh = getSharedPreferences("vispref", 0);
            editor = sh.edit();

            ButterKnife.bind(MarkVisitActivity.this);

            if (tbMarkVisit != null) {
                setSupportActionBar(tbMarkVisit);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                getSupportActionBar().setTitle(getResources().getString(R.string.tb_title_mark_visit));
            }

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            mAdView = (AdView) findViewById(R.id.adView);

            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            Criteria criteria = new Criteria();
            bestProvider = locationManager.getBestProvider(criteria, false);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            location = getLastKnownLocation();
            Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> address = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (address.size() > 0) {
                    textLocationName.setText(address.get(0).getFeatureName() + ", " + address.get(0).getLocality() + ", " + address.get(0).getAdminArea() + ", " + address.get(0).getCountryName());
                }
            } catch (IOException e) {
            } catch (NullPointerException e) {
            }

            pickerButton.setOnClickListener(this);
//        refreshBtn.setOnClickListener(this);
            btViewVisit.setOnClickListener(this);
            btAddVisit.setOnClickListener(this);

            if (MOVisitManager.IsLastMarkedArrived(this)) {
                rbArrived.setChecked(false);
                rbDeparted.setChecked(true);

                String lastVisPlace = sh.getString("LastPlaceVis", "_NA_");
                String lastPurpVis = sh.getString("LastPurpVis", "_NA_");
                if (!lastVisPlace.contains("_NA_")) textPlaceOfVisit.setText(lastVisPlace);
                if (!lastPurpVis.contains("_NA_")) textPurposeOfVisit.setText(lastPurpVis);
            } else {
                rbArrived.setChecked(true);
                rbDeparted.setChecked(false);
            }
        } catch (Exception exp) {
            Log.e("Exp", exp.getMessage());
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;


            case R.id.action_refresh:
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                return true;


            default:
                return false;

        }

    }

    private Location getLastKnownLocation() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint({"MissingPermission"}) Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        if (bestLocation == null)
            return null;
        return bestLocation;
    }


    @Override
    public void onClick(View view) {
        locationName = textLocationName.getText().toString();
        purpose = textPurposeOfVisit.getText().toString();
        placeOfVisit = textPlaceOfVisit.getText().toString();
        switch (view.getId()) {

            case R.id.pickerButton:
                try {
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();
                    intentBuilder.setLatLngBounds(BOUNDS_TECHNOPARK);
                    Intent intent = intentBuilder.build(MarkVisitActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btViewVisit:
                AppUtils.callToIntent(MarkVisitActivity.this,
                        VisitListActivity.class,
                        false);
                break;


            case R.id.btAddVisit:
                if (!placeOfVisit.equalsIgnoreCase("") && !purpose.equalsIgnoreCase("")) {
                    if (rbArrived.isChecked()) {
                        Date arrivedDate = new Date();
                        editor.putInt("ArrivedDeparted", 1);
                        editor.putLong("ArrivedDate", arrivedDate.getTime());
                        editor.putString("LastPlaceVis", placeOfVisit);
                        editor.putString("LastPurpVis", purpose);
                        editor.commit();
                        if (MOVisitManager.markVisit(this, location, 1, locationName, purpose, placeOfVisit, 0, 0)) {
                            Toast.makeText(this, "Visit marked successfully", Toast.LENGTH_LONG).show();
                        }
                    } else if (rbDeparted.isChecked()) {
                        int status = sh.getInt("ArrivedDeparted", 0);
                        if (status == 1) {
                            Date departedDate = new Date();
                            long millis = sh.getLong("ArrivedDate", 0L);
                            Date LastArrivedDate = new Date(millis);
                            long dateDiff = departedDate.getTime() - LastArrivedDate.getTime();
                            long minutesSpend = dateDiff / (60 * 1000);
                            long hoursSpend = dateDiff / (60 * 60 * 1000);


                            if (MOVisitManager.markVisit(this, location, 0, locationName, purpose, placeOfVisit, hoursSpend, minutesSpend)) {
                                editor.putInt("ArrivedDeparted", 0);
                                editor.commit();
                                Toast.makeText(this, "Visit marked successfully", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this, "Sorry no yet arrived", Toast.LENGTH_LONG).show();
                        }
                        textPlaceOfVisit.setText("");
                        textPurposeOfVisit.setText("");
                    } else {
                        Toast.makeText(this, "Arrived/Departed not selected", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {

            final Place place = PlacePicker.getPlace(this, data);
            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            LatLng latLng = place.getLatLng();
            double pLati = latLng.latitude;
            double pLongi = latLng.longitude;
            double distance = distance(pLati, location.getLatitude(), pLongi, location.getLongitude(), 0.0, 0.0);
            if (distance < 100) {
                textLocationName.setText(name + "," + address);
            } else {
                Toast.makeText(this, "Sorry this place is more than 100 away from your current location", Toast.LENGTH_LONG).show();
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    protected void onResume() {
        super.onResume();
//        tbMarkVisit.inflateMenu(R.menu.visit_list);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(bestProvider, 400, 1, this);
    }


    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.visit_list, menu);
        menu_item = menu;
        return super.onCreateOptionsMenu(menu);
    }

}
