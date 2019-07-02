package com.microoffice.app.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.microoffice.app.R;
import com.microoffice.app.utils.AppUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import moffice.meta.com.molibrary.core.MOAttenManager;
import moffice.meta.com.molibrary.core.MOOfficeLocManager;

public class MarkAttendanceActivity extends AppCompatActivity
        implements LocationListener, View.OnClickListener {
    private LocationManager locationManager;
    private String provider;
    @Bind(R.id.tbMarkAttendance)
    Toolbar tbMarkAttendance;
    @Bind(R.id.plan_edt)
    EditText todayPlanEdt;
    @Bind(R.id.place_edt)
    EditText placeEdt;
    @Bind(R.id.btAddendanceView)
    Button btAddendanceView;
    @Bind(R.id.btMarkAttendance)
    Button btMarkAttendance;
    //    @Bind(R.id.btviewAttendance)
//    Button btviewattendence;
    @Bind(R.id.textOfficeName)
    TextView txtOfficeName;
    @Bind(R.id.rgINOUT)
    RadioGroup radioGroup;
    @Bind(R.id.rbIN)
    RadioButton rbIN;
    @Bind(R.id.rbOUT)
    RadioButton rbOUT;
    /* @Bind(R.id.rgINOUT)
     RadioGroup rgINOUT;*/
    Location loc;
    String officeName;
    String officeID;
    String todaysPlan;
    String place;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_mark_attendance);

            ButterKnife.bind(MarkAttendanceActivity.this);
            mAdView=(AdView)findViewById(R.id.adView);

            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            if (tbMarkAttendance != null) {
                placeEdt.setVisibility(View.VISIBLE);
                setSupportActionBar(tbMarkAttendance);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                getSupportActionBar().setTitle(getResources().getString(R.string.tb_title_mark_attendance));
            } else {
                placeEdt.setVisibility(View.INVISIBLE);
            }
            rbIN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (rbIN.isChecked()) {
                        todayPlanEdt.setVisibility(View.VISIBLE);
                        placeEdt.setText("");

                    } else {
                        todayPlanEdt.setText("");

                        todayPlanEdt.setVisibility(View.INVISIBLE);
                        placeEdt.setText(MOAttenManager.lastMarkedInPlace(MarkAttendanceActivity.this));
                        placeEdt.setSelection(placeEdt.getText().length());

                    }
                }
            });
            rbOUT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (rbIN.isChecked()) {
                        todayPlanEdt.setVisibility(View.VISIBLE);
                        placeEdt.setText("");

                    } else {
                        todayPlanEdt.setText("");
                        placeEdt.setText(MOAttenManager.lastMarkedInPlace(MarkAttendanceActivity.this));
                        placeEdt.setSelection(placeEdt.getText().length());
                        todayPlanEdt.setVisibility(View.INVISIBLE);


                    }
                }
            });
            // Get the location manager
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location loc = getLastKnownLocation();
//            loc = MOOfficeLocManager.getLocation(this);
            if (loc == null) {
                txtOfficeName.setText("Failed to detect Location");
            } else if (MOOfficeLocManager.detectOffice(this, loc)) {
                txtOfficeName.setText(MOOfficeLocManager.officeName);
            } else {
                txtOfficeName.setText("Not at office");
            }
            btAddendanceView.setOnClickListener(this);
            btMarkAttendance.setOnClickListener(this);
            if (MOAttenManager.IsLastMarkedIn(MarkAttendanceActivity.this)) {
                placeEdt.setText(MOAttenManager.lastMarkedInPlace(MarkAttendanceActivity.this));
                placeEdt.setSelection(placeEdt.getText().length());
                rbOUT.setChecked(true);
                rbIN.setChecked(false);
            } else {
                rbOUT.setChecked(false);
                rbIN.setChecked(true);
            }
//            btviewattendence.setOnClickListener(this);
        } catch (Exception exp) {
            Log.e("Exp", exp.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    String lastRecInId = null;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btAddendanceView:
                AppUtils.callToIntent(MarkAttendanceActivity.this,
                        AttendanceListActivity.class,
                        false);
                break;

            case R.id.btMarkAttendance:
                lastRecInId = null;
                todaysPlan = todayPlanEdt.getText().toString();
                place = placeEdt.getText().toString();


                Location loc = getLastKnownLocation();
                if (loc == null) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setMessage("Location not available. Check Whether Location  Enabled");
                    alertDialogBuilder.setPositiveButton("OK", null);

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    break;
                }
                MOOfficeLocManager.officeName = null;
                if (MOOfficeLocManager.detectOffice(this, loc)) {

                    MOOfficeLocManager.markType = MOOfficeLocManager.MOA_MARK_OFFICEID;
                    if (rbIN.isChecked()) {
                        String lastRecInId = MOAttenManager.IsLastIn(MarkAttendanceActivity.this);
                        if (lastRecInId != null) {
                            WarnInMark(todaysPlan);
                        } else {
                            if (MOAttenManager.markAttendance(this, 1, 1, null, todaysPlan, place)) {
                                Toast.makeText(getApplicationContext(), "Attendence in Marked", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else if (rbOUT.isChecked()) {
                        if (MOAttenManager.markAttendance(this, 0, 1, null, todaysPlan, place)) {
                            Toast.makeText(getApplicationContext(), "Attendence out Marked", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "in/out not selected", Toast.LENGTH_LONG).show();
                    }


                } else {
                    Location location = getLastKnownLocation();
                    String locName = null;
                    Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
                    try {
                        List<Address> address = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (address.size() > 0) {

                            locName = address.get(0).getFeatureName() + ", " + address.get(0).getLocality() + ", " + address.get(0).getAdminArea() + ", " + address.get(0).getCountryName();
                        }
                    } catch (IOException e) {
                    } catch (NullPointerException e) {
                    }
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    if (locName != null) {
                        MOOfficeLocManager.markType = MOOfficeLocManager.MOA_MARK_OFFICE_LOCNAME;
                        alertDialogBuilder.setMessage("You are not in any Office!!!\r\nYour Location Is Detected as:\r\n\b" + locName + "\r\n\r\n Do you want to mark attendance with this loc??");
                        MOOfficeLocManager.officeName = locName;
                    } else {
                        MOOfficeLocManager.markType = MOOfficeLocManager.MOA_MARK_OFFICE_COORDINATES;
                        MOOfficeLocManager.officeName = null;
                        alertDialogBuilder.setMessage("You are not in any Office!!!\r\n We failed detect the location name ,Do you still want to mark attendance with this loc co-ordinated??");
                    }
                    alertDialogBuilder.setPositiveButton("Yes Mark Attendance", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (rbIN.isChecked()) {
                                lastRecInId = MOAttenManager.IsLastIn(MarkAttendanceActivity.this);
                                if (lastRecInId != null) {
                                    WarnInMark(todaysPlan);
                                } else {
                                    if (MOAttenManager.markAttendance(MarkAttendanceActivity.this, 1, 1, null, todaysPlan, place)) {
                                        Toast.makeText(getApplicationContext(), "Attendence in Marked", Toast.LENGTH_LONG).show();
                                    }
                                }
                            } else if (rbOUT.isChecked()) {
                                if (MOAttenManager.markAttendance(MarkAttendanceActivity.this, 0, 1, null, todaysPlan, place)) {
                                    Toast.makeText(getApplicationContext(), "Attendence out Marked", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                    alertDialogBuilder.setNegativeButton("No Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplicationContext(), "Attendance not marked", Toast.LENGTH_LONG).show();

                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }

                break;
        }

    }

    boolean selection = false;

    boolean WarnInMark(final String todaysPlan) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Last Marked is an IN ");
        alertDialogBuilder.setPositiveButton("Update Last In with Current Time", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "Updaing last IN", Toast.LENGTH_LONG).show();
                if (MOAttenManager.markAttendance(MarkAttendanceActivity.this, 1, 1, lastRecInId, todaysPlan, place)) {
                    Toast.makeText(getApplicationContext(), "Attendence in Marked", Toast.LENGTH_LONG).show();
                }
                selection = true;
            }
        });
        alertDialogBuilder.setNegativeButton("No Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "Attendance not marked", Toast.LENGTH_LONG).show();
                selection = false;
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return selection;
    }


    private Location getLastKnownLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = locationManager.getLastKnownLocation(provider);

            if (l == null) {
                continue;
            }
            if (bestLocation == null
                    || l.getAccuracy() < bestLocation.getAccuracy()) {
//                ALog.d("found best last known location: %s", l);
                bestLocation = l;
            }
        }
        if (bestLocation == null) {
            return null;
        }
        return bestLocation;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
