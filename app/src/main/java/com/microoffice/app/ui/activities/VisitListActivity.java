package com.microoffice.app.ui.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.microoffice.app.R;
import com.microoffice.app.ui.adapters.CustomSpinnerAdapter;
import com.microoffice.app.ui.adapters.VisitListRecyclerAdapter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;
import moffice.meta.com.molibrary.core.MOMain;
import moffice.meta.com.molibrary.models.dbmodels.Employees;
import moffice.meta.com.molibrary.models.dbmodels.Offices;
import moffice.meta.com.molibrary.models.dbmodels.UserList;
import moffice.meta.com.molibrary.models.dbmodels.VisitLog;
import moffice.meta.com.molibrary.utility.CommonDataArea;
import moffice.meta.com.molibrary.utility.LogWriter;

public class VisitListActivity extends AppCompatActivity implements View.OnClickListener
        , DatePickerDialog.OnDateSetListener {

    @Bind(R.id.ivCrossWhite)
    ImageView ivCrossWhite;

    @Bind(R.id.frame_llt)
    FrameLayout frameLayout;

    @Bind(R.id.spEmpNames)
    Spinner spEmpNames;

    @Bind(R.id.llStartDate)
    LinearLayout llStartDate;

    @Bind(R.id.llEndDate)
    LinearLayout llEndDate;

    @Bind(R.id.tvStartDate)
    TextView tvStartDate;

    @Bind(R.id.tvEndDate)
    TextView tvEndDate;

  //  @Bind(R.id.tvPlacePcker)
  //  TextView tvPlacePckr;

    @Bind(R.id.tvFilterReset)
    TextView tvFilterReset;

    @Bind(R.id.rvVisitList)
    RecyclerView rvVisitList;

    @Bind(R.id.llFilterData)
    LinearLayout llFilterData;

   // @Bind(R.id.tvPlaceView)
   // TextView tvPlaceView;

    CustomSpinnerAdapter mOfficeSpinnerAdapter = null;
    CustomSpinnerAdapter mEmpSpinnerAdapter = null;

    private VisitListRecyclerAdapter mVisitListRecyclerAdapter;
    private LinearLayout llattnView;
    String location;
    String userName;
    String officeName;
    String offi;

    Date startDate, endDate;
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final LatLngBounds BOUNDS_TECHNOPARK = new LatLngBounds(
            new LatLng(8.5572, 76.8765), new LatLng(8.5572, 76.8765));

    Calendar calendar;
    String myFormat = "dd/MM/yyyy"; //In which you need put here
    DateFormat sdf = new SimpleDateFormat(myFormat);

    int dd, mm, yyyy;
    boolean isFilter = true;

    TextView tvWhichView;

    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_list);

        ButterKnife.bind(VisitListActivity.this);
        calendar = Calendar.getInstance(TimeZone.getDefault());
        mAdView=(AdView)findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        try {
        //    initOfficeSpinner();
            if(CommonDataArea.getInstType()!= MOMain.MO_INSTTYPE_ASSOS) {
                spEmpNames.setEnabled(true);
                initEmployeeSpinner();
            }else  frameLayout.setVisibility(View.GONE);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception exp){

        }

        tvStartDate.setText(sdf.format(new Date()));
        tvEndDate.setText(sdf.format(new Date()));


        tvWhichView = tvStartDate;
        ivCrossWhite.setOnClickListener(this);
        llStartDate.setOnClickListener(this);
        llEndDate.setOnClickListener(this);
        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);
        tvFilterReset.setOnClickListener(this);
        //tvPlacePckr.setOnClickListener(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("MO_VIS_DISP"));

    }


    @Override
    protected void onResume() {
        super.onResume();
        try {

            Dao<VisitLog, Integer> VisitDao;
            List<VisitLog> visitArrayList;

            VisitDao = CommonDataArea.getHelper(getApplicationContext()).getVisitLogDao();
            QueryBuilder<VisitLog, Integer> queryBuilder =
                    VisitDao.queryBuilder();

            String start = tvStartDate.getText().toString();
            String end = tvEndDate.getText().toString();
            long startDateMills=0;
            long endDateMills=0;
            try {
                startDate = sdf.parse(start);
                DateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                endDate = sdf1.parse(end+" 23:59:59");

                startDateMills = startDate.getTime();
                endDateMills = endDate.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }


            Where<VisitLog, Integer> where = queryBuilder.where();
            where.between("TimeStamp",startDateMills,endDateMills);
            if(CommonDataArea.getInstType()== MOMain.MO_INSTTYPE_ASSOS)
            {
            where.and();
            where.eq("UserUUID",CommonDataArea.getUserUUID());
            }else{
                if((userName!=null)&&(userName.length()>0)) {
                    where.and();
                    where.eq("UserUUID",CommonDataArea.getUserUUID());
                }
            }
            visitArrayList = queryBuilder.query();


            mVisitListRecyclerAdapter = new VisitListRecyclerAdapter((ArrayList<VisitLog>) visitArrayList, this);
            rvVisitList.setHasFixedSize(true);
            rvVisitList.setLayoutManager(new LinearLayoutManager(this));
            rvVisitList.setAdapter(mVisitListRecyclerAdapter);
            mVisitListRecyclerAdapter.notifyDataSetChanged();

            llFilterData.setVisibility(View.VISIBLE);
            CommonDataArea.closeHelper();

        } catch (SQLException e) {
            e.printStackTrace();
            LogWriter.writeLogException("VisitActivity:Resume",e);
        }
        catch (Exception e) {
            e.printStackTrace();
            LogWriter.writeLogException("VisitActivity:Resume",e);
        }
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

    private void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(VisitListActivity.this, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        yyyy = year;
        mm = monthOfYear;
        dd = dayOfMonth;

        tvWhichView.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(dd).append("/").append(mm + 1).append("/").append(yyyy).append(" "));

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ivCrossWhite:
                finish();
                break;
            case R.id.llStartDate:
            case R.id.tvStartDate:
                tvWhichView = tvStartDate;
                showDatePicker();
                break;

            case R.id.llEndDate:
            case R.id.tvEndDate:
                tvWhichView = tvEndDate;
                showDatePicker();
                break;

            case R.id.tvFilterReset:

                String start = tvStartDate.getText().toString();
                String end = tvEndDate.getText().toString();
                long startDateMills=0;
                long endDateMills=0;
                try {
                    startDate = sdf.parse(start);
                    DateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    endDate = sdf1.parse(end+" 23:59:59");

                    startDateMills = startDate.getTime();
                    endDateMills = endDate.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {

                    VisitListRecyclerAdapter visitListRecyclerAdapter;
                    Dao<VisitLog, Integer> VisitDao;
                    Dao<Employees, Integer> EmployeeDao;
                    List<VisitLog> visitArrayList;
                    VisitDao = CommonDataArea.getHelper(getApplicationContext()).getVisitLogDao();
                    QueryBuilder<VisitLog, Integer> queryBuilder = VisitDao.queryBuilder();

                    boolean addAnd = false;
                    Where<VisitLog, Integer> where = queryBuilder.where();
                    if(CommonDataArea.getInstType()== MOMain.MO_INSTTYPE_ASSOS)
                    {
                        where.eq("UserUUID",CommonDataArea.getUserUUID());
                        addAnd=true;
                    }else
                        {
                        if((userName!=null)&&(userName.length()>0)) {
                            addAnd=true;
                            where.eq("UserName", userName);
                        }
                        }
                    if((startDateMills>0)&&(endDateMills>0)) {
                        if(addAnd){ where.and();}
                        where.between("TimeStamp", startDateMills, endDateMills);
                    }
                    visitArrayList = queryBuilder.query();

                    visitListRecyclerAdapter = new VisitListRecyclerAdapter((ArrayList<VisitLog>) visitArrayList, this);
                    rvVisitList.setLayoutManager(new LinearLayoutManager(this));
                    rvVisitList.setAdapter(visitListRecyclerAdapter);

                    mVisitListRecyclerAdapter.notifyDataSetChanged();
                    llFilterData.setVisibility(View.VISIBLE);
                    CommonDataArea.closeHelper();

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    LogWriter.writeLogException("VisitSerach",e);
                }
                break;
          /*  case R.id.tvPlacePcker:

                try {
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();
                    intentBuilder.setLatLngBounds(BOUNDS_TECHNOPARK);
                    Intent intent = intentBuilder.build(VisitListActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException
                        | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }*/
        }
    }

    public void showMessage(String success, String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(success);
        builder.setMessage(s);
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {

            final Place place = PlacePicker.getPlace(this, data);
            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();

            //tvPlaceView.setText(name);

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void initOfficeSpinner() throws SQLException {
        List<String> office = null;
        Dao<Offices, Integer> OfficeDao = null;
        try {
            OfficeDao = CommonDataArea.getHelper(getApplicationContext()).getOfficesDao();
            office = OfficeDao.queryRaw("SELECT OfficeName FROM Offices", new RawRowMapper<String>() {
                @Override
                public String mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
                    return resultColumns[0];
                }
            }).getResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        office.add(0,"Select Office");
        // Resources passed to adapter to get image
        Resources res = getResources();
        // Create custom adapter object ( see below CustomAdapter.java )
        mOfficeSpinnerAdapter = new CustomSpinnerAdapter(VisitListActivity.this,
                R.layout.custom_spinner_row, office, res, getString(R.string.spinner_hint_office_name));
      /*  spOfficeNames.setAdapter(mOfficeSpinnerAdapter);
//        // Listener called when spinner item selected
        spOfficeNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                try {
                    if (position == 0) officeName = null;
                    else {
                        officeName = parentView.getItemAtPosition(position).toString();
                        userName = null;
                        spEmpNames.setSelection(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                officeName = null;
            }

        });*/

    }

    private void initEmployeeSpinner() throws SQLException {

        List<String> employee = null;
        Dao<UserList, Integer> employeeDao = null;

        try {
            employeeDao = CommonDataArea.getHelper(getApplicationContext()).getUserListDao();
            employee = employeeDao.queryRaw("SELECT Name FROM UserList  ", new RawRowMapper<String>() {
                @Override
                public String mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
                    return resultColumns[0];
                }
            }).getResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        employee.add(0, "Select An Employee");
        Resources res = getResources();

        mEmpSpinnerAdapter = new CustomSpinnerAdapter(VisitListActivity.this,
                R.layout.custom_spinner_row, employee, res,
                getString(R.string.spinner_hint_emp_name));
        spEmpNames.setAdapter(mEmpSpinnerAdapter);


        spEmpNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                if (position == 0) userName = null;
                else {
                    userName = parentView.getItemAtPosition(position).toString();
                    officeName = null;
                    //spOfficeNames.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                userName = null;
            }

        });
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
       /*     String location = intent.getStringExtra("loc");
            Location placeLoc = CommonDataArea.placeLoc;
            final LatLngBounds YOUR_LOCATION= new LatLngBounds(
                    new LatLng(placeLoc.getLatitude(),placeLoc.getLongitude()), new LatLng(placeLoc.getLatitude(),placeLoc.getLongitude()));
            try {
                PlacePicker.IntentBuilder intentBuilder =
                        new PlacePicker.IntentBuilder();

                intentBuilder.setLatLngBounds(YOUR_LOCATION);
                Intent mIntent = intentBuilder.build(VisitListActivity.this);
                startActivityForResult(mIntent, PLACE_PICKER_REQUEST);

            } catch (GooglePlayServicesRepairableException
                    | GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }*/


            Intent intent1 = new Intent(context, MapsVisitsActivity.class);
            startActivity(intent1);

       /*     if (Geocoder.isPresent()){

                try {
                    Geocoder gc=new Geocoder(VisitListActivity.this);
                    List<Address> addresses=gc.getFromLocationName(location,5);
                    List<LatLng> latLngs =new ArrayList<LatLng>(addresses.size());
                    for (Address a:addresses){

                        if (a.hasLatitude()&&a.hasLongitude()){


                             final LatLngBounds YOUR_LOCATION= new LatLngBounds(
                                    new LatLng(a.getLatitude(),a.getLongitude()), new LatLng(a.getLatitude(),a.getLongitude()));


                            try {
                                PlacePicker.IntentBuilder intentBuilder =
                                        new PlacePicker.IntentBuilder();
                                intentBuilder.setLatLngBounds(YOUR_LOCATION);
                                Intent mIntent = intentBuilder.build(VisitListActivity.this);
                                startActivityForResult(mIntent, PLACE_PICKER_REQUEST);

                            } catch (GooglePlayServicesRepairableException
                                    | GooglePlayServicesNotAvailableException e) {
                                e.printStackTrace();
                            }
                        }

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
*/


        }
    };


}

