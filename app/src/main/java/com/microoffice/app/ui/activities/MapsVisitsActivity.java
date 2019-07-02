package com.microoffice.app.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.microoffice.app.R;
import com.microoffice.app.ui.adapters.OfficeListAdapter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.ButtCap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.List;

import moffice.meta.com.molibrary.models.dbmodels.Offices;
import moffice.meta.com.molibrary.utility.CommonDataArea;
import moffice.meta.com.molibrary.utility.LogWriter;
import moffice.meta.com.molibrary.utility.MarkerToDraw;

public class MapsVisitsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<LatLng> points;
    OfficeListAdapter officeListAdapter = null;
    Dao<Offices, Integer> OfficesDao = null;
    List<Offices> officesArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_visits);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        points = new ArrayList<LatLng>();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }





    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
          //  OfficesDao = ((DashboardActivity) getApplicationContext()).getHelper().getOfficesDao();
           // officesArrayList = OfficesDao.queryForAll();

            if (officesArrayList.size() > 0) {
          //      officeListAdapter = new OfficeListAdapter((Activity) getApplicationContext(), officesArrayList);

            } else {


            }

        }catch (Exception e){

        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            setUpMap();
            if ((CommonDataArea.markers == null) || (CommonDataArea.markers.size() == 0)) {
                Toast.makeText(this, "No location to Display", Toast.LENGTH_LONG).show();
                return;
            }
            // Add a marker in Sydney and move the camera
            Marker markerLast = null;
            for (MarkerToDraw marker : CommonDataArea.markers) {
                LatLng markerLaLo = new LatLng(marker.placeLoc.getLatitude(), marker.placeLoc.getLongitude());

                Marker m1;
                if (marker.first){
                    MarkerOptions mops = new MarkerOptions().position(markerLaLo).title("START");
                    mops.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    m1 = mMap.addMarker(mops);

                }

                if (marker.last){
                    MarkerOptions mops = new MarkerOptions().position(markerLaLo).title("LAST");
                    mops.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                    m1 = mMap.addMarker(mops);

                }
                if (!marker.arrived) {
                    MarkerOptions mops = new MarkerOptions().position(markerLaLo).title(marker.placeName);
                    mops.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    m1 = mMap.addMarker(mops);
                    m1.setRotation(30);
                } else {
                    MarkerOptions mops = new MarkerOptions().position(markerLaLo).title(marker.placeName);

                    m1 = mMap.addMarker(mops);
                    m1.setRotation(330);
                }
                builder.include(m1.getPosition());

                if (markerLast != null) {
                    PolylineOptions polOp = (new PolylineOptions()
                            .add(markerLast.getPosition(), m1.getPosition())
                            .width(5)
                            .color(Color.RED));
                    ButtCap cap = new ButtCap();
                    polOp.endCap(cap);
                    Polyline line = mMap.addPolyline(polOp);
                }

                markerLast = m1;
            }
            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
            mMap.animateCamera(cu);
        }catch(Exception exp){
            LogWriter.writeLogException("MapDraw",exp);
        }
    }

    private void setUPMApsss(GoogleMap mMap) {
        PolylineOptions polylineOptions= new PolylineOptions();
        polylineOptions.color(Color.RED);
        polylineOptions.width(5);
        polylineOptions.addAll(CommonDataArea.arraylistLAti);
        mMap.addPolyline(polylineOptions);
        //mMap.addPolyline(polylineOptions);
    }

    public void setUpMap() {

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setTrafficEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                    location.getLongitude()), 12));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                /*MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Position");
                markerOptions.snippet("Latitude:" + latLng.latitude + "," + "Longitude:" + latLng.longitude);
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.color(Color.RED);
                polylineOptions.width(5);
                points.add(latLng);
                polylineOptions.addAll(points);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.addPolyline(polylineOptions);
                mMap.addMarker(markerOptions);*/
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng point) {
                mMap.clear();
               CommonDataArea.arraylistLAti.clear();
            }
        });


    }
}
