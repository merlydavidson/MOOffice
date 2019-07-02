package com.microoffice.app.ui.activities;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
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
import com.microoffice.app.ui.adapters.AttendenceListRecylerAdapter;

import com.microoffice.app.ui.adapters.CustomSpinnerAdapter;
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
import java.util.Locale;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;
import moffice.meta.com.molibrary.core.MOMain;
import moffice.meta.com.molibrary.models.dbmodels.AttnLog;
import moffice.meta.com.molibrary.models.dbmodels.Offices;
import moffice.meta.com.molibrary.models.dbmodels.UserList;
import moffice.meta.com.molibrary.utility.CommonDataArea;
import moffice.meta.com.molibrary.utility.LogWriter;

public class AttendanceListActivity extends AppCompatActivity implements View.OnClickListener
        , DatePickerDialog.OnDateSetListener {

    @Bind(R.id.ivCrossWhite)
    ImageView ivCrossWhite;

    @Bind(R.id.spOfficeNames)
    Spinner spOfficeNames;

    @Bind(R.id.spEmpNames)
    Spinner spEmpNames;

    @Bind(R.id.attdncs_spinner_llt)
    FrameLayout att_frame;
    @Bind(R.id.frame_emp_llt)
    FrameLayout frame_emp_llt;


    @Bind(R.id.llStartDate)
    LinearLayout llStartDate;

    @Bind(R.id.llEndDate)
    LinearLayout llEndDate;

    @Bind(R.id.tvStartDate)
    TextView tvStartDate;

    @Bind(R.id.tvEndDate)
    TextView tvEndDate;

    @Bind(R.id.tvFilterReset)
    TextView tvFilterReset;

    @Bind(R.id.rvAttendanceList)
    RecyclerView rvAttendanceList;

    @Bind(R.id.llFilterData)
    LinearLayout llFilterData;

    private AdView mAdView;

    CustomSpinnerAdapter mOfficeSpinnerAdapter = null;
    CustomSpinnerAdapter mEmpSpinnerAdapter = null;
    Calendar calendar;
    String myFormat = "dd/MM/yyyy"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
    int dd, mm, yyyy;
    boolean isFilter = true;
    TextView tvWhichView;
    String selectedOffice;
    String selectedEmployee;
    private AttendenceListRecylerAdapter mAttendenceListRecylerAdapter;
    private LinearLayout llattnView;
    List<String>office = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);

        ButterKnife.bind(AttendanceListActivity.this);
        mAdView=(AdView)findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        calendar = Calendar.getInstance(TimeZone.getDefault());

        try {
            if(CommonDataArea.getInstType()!= MOMain.MO_INSTTYPE_ASSOS) {
                initOfficeSpinner();
                initEmployeeSpinner();
            } else {
                att_frame.setVisibility(View.GONE);
                frame_emp_llt.setVisibility(View.GONE);

               spOfficeNames.setVisibility(View.GONE);
               spEmpNames.setVisibility(View.GONE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
//        initEmployeeSpinner();
        tvWhichView = tvStartDate;
        ivCrossWhite.setOnClickListener(this);
        llStartDate.setOnClickListener(this);
        llEndDate.setOnClickListener(this);
        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);
        tvFilterReset.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        tvStartDate.setText(sdf.format(new Date()));
          tvEndDate.setText(sdf.format(new Date()));

        String  start=  tvStartDate.getText().toString();
        String end=tvEndDate.getText().toString();
        if(start.equals("Select Date")||end.equals("Select Date"))
        {
            new AlertDialog.Builder(this)
                    .setTitle("Message")
                    .setMessage("please select a start and end date")
                    .setPositiveButton("OK", null).show();
        }
        else {
            SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null, endate = null;
            long startTimeStamp=0;
            long endTimeStamp=0;
            try {
                date = form.parse(start);
                DateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                endate = sdf1.parse(end+" 23:59:59");

                startTimeStamp= date.getTime();
                endTimeStamp = endate.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {

                Dao<AttnLog, Integer> AttnDao;
                List<AttnLog> attnArrayList;
                AttnDao = CommonDataArea.getHelper(getApplicationContext()).getAttnLogDao();
                QueryBuilder<AttnLog, Integer> queryBuilder =
                        AttnDao.queryBuilder();
                Where<AttnLog, Integer> where = queryBuilder.where();
                if(CommonDataArea.getInstType()== MOMain.MO_INSTTYPE_ASSOS)
                {
                    where.eq("UserUUID", CommonDataArea.getUserUUID());
                    where.and();
                }else
                {
                    if(selectedEmployee!=null) {
                        where.eq("UserName", selectedEmployee);
                        where.and();
                    }else  if((selectedOffice!=null)&&(selectedOffice.length()>0)){
                        where.eq("Office", selectedOffice);
                        where.and();
                    }
                }
                if((startTimeStamp!=0)&&(endTimeStamp!=0))
                    where.between("TimeStamp", startTimeStamp, endTimeStamp);
                queryBuilder.orderBy("TimeStamp",true);

                attnArrayList = queryBuilder.query();
                rvAttendanceList.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AttendanceListActivity.this);
                rvAttendanceList.setLayoutManager(linearLayoutManager);
                mAttendenceListRecylerAdapter = new AttendenceListRecylerAdapter((ArrayList<AttnLog>) attnArrayList);
                rvAttendanceList.setAdapter(mAttendenceListRecylerAdapter);
                llFilterData.setVisibility(View.VISIBLE);
                CommonDataArea.closeHelper();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
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
        DatePickerDialog dialog = new DatePickerDialog(AttendanceListActivity.this, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
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
                String  start=  tvStartDate.getText().toString();
                String end=tvEndDate.getText().toString();
                if(start.equals("Select Date")||end.equals("Select Date"))
                {
                    new AlertDialog.Builder(this)
                            .setTitle("Message")
                            .setMessage("please select a start and end date")
                            .setPositiveButton("OK", null).show();
                }
                else {
                    SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy");
                    java.util.Date date = null, endate = null;
                    long startTimeStamp=0;
                    long endTimeStamp=0;
                    try {
                        date = form.parse(start);
                        DateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        endate = sdf1.parse(end+" 23:59:59");

                        startTimeStamp= date.getTime();
                        endTimeStamp = endate.getTime();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    try {

                        Dao<AttnLog, Integer> AttnDao;
                        List<AttnLog> attnArrayList;
                        AttnDao = CommonDataArea.getHelper(getApplicationContext()).getAttnLogDao();
                        QueryBuilder<AttnLog, Integer> queryBuilder =
                                AttnDao.queryBuilder();
                        Where<AttnLog, Integer> where = queryBuilder.where();
                        if(CommonDataArea.getInstType()== MOMain.MO_INSTTYPE_ASSOS)
                        {
                            where.eq("UserUUID",CommonDataArea.getUserUUID());
                            where.and();
                        }else
                            {
                            if(selectedEmployee!=null) {
                                where.eq("UserName", selectedEmployee);
                                where.and();
                            }else  if((selectedOffice!=null)&&(selectedOffice.length()>0)){
                                where.eq("Office", selectedOffice);
                                where.and();
                            }
                            }
                        if((startTimeStamp!=0)&&(endTimeStamp!=0))
                        where.between("TimeStamp", startTimeStamp, endTimeStamp);
                        queryBuilder.orderBy("TimeStamp",true);

                        attnArrayList = queryBuilder.query();
                        rvAttendanceList.setHasFixedSize(true);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AttendanceListActivity.this);
                        rvAttendanceList.setLayoutManager(linearLayoutManager);
                        mAttendenceListRecylerAdapter = new AttendenceListRecylerAdapter((ArrayList<AttnLog>) attnArrayList);
                        rvAttendanceList.setAdapter(mAttendenceListRecylerAdapter);
                        llFilterData.setVisibility(View.VISIBLE);
                        CommonDataArea.closeHelper();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
    public  void showMessage(String success, String s) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(success);
        builder.setMessage( s);
        builder.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        yyyy = year;
        mm = monthOfYear;
        dd = dayOfMonth;

        tvWhichView.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(dd).append("/").append(mm + 1).append("/").append(yyyy).append(" "));
    }

//    private void setDataToAdapter(ArrayList<AttnLog> data) {
//        rvAttendanceList.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AttendanceListActivity.this);
//        rvAttendanceList.setLayoutManager(linearLayoutManager);
////        rvAttendanceList.setItemAnimator(new DefaultItemAnimator());
//        mAttendenceListRecylerAdapter = new AttendenceListRecylerAdapter(data);
//        rvAttendanceList.setAdapter(mAttendenceListRecylerAdapter);
//    }

    private void initOfficeSpinner() throws SQLException {

        List<String> office=null;
        Dao<Offices,Integer> OfficeDao=null;

        try{

            OfficeDao=CommonDataArea.getHelper(getApplicationContext()).getOfficesDao();
            office=OfficeDao.queryRaw("SELECT OfficeName FROM Offices", new RawRowMapper<String >() {
                @Override
                public String mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
                    return resultColumns[0];
                }
            }).getResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Resources passed to adapter to get image
        Resources res = getResources();

        office.add(0,"Select Office");
        // Create custom adapter object ( see below CustomAdapter.java )
        mOfficeSpinnerAdapter = new CustomSpinnerAdapter(AttendanceListActivity.this,
                R.layout.custom_spinner_row, office, res, getString(R.string.spinner_hint_office_name));

        spOfficeNames.setAdapter(mOfficeSpinnerAdapter);

        spOfficeNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                if(position==0)
                    selectedOffice=null;
                else {
                    selectedOffice = parentView.getItemAtPosition(position).toString();
                    selectedEmployee = null;
                    spEmpNames.setSelection(0);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                selectedOffice=null;
            }

        });


    }

    private void initEmployeeSpinner() throws SQLException {
        //Link between employee and office is now not established. So list all employees
       // selectedOffice=spOfficeNames.getSelectedItem().toString();

        List<String> employee=null;
        Dao<UserList,Integer> employeeDao=null;

        try{

            employeeDao = CommonDataArea.getHelper(getApplicationContext()).getUserListDao();
            employee = employeeDao.queryRaw("SELECT Name FROM UserList ", new RawRowMapper<String>() {
                @Override
                public String mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
                    return resultColumns[0];
                }
            }).getResults();

        } catch (SQLException e) {
            e.printStackTrace();}

        Resources res = getResources();
        employee.add(0,"Select Employee");
        mEmpSpinnerAdapter = new CustomSpinnerAdapter(AttendanceListActivity.this,
                R.layout.custom_spinner_row, employee, res,
                getString(R.string.spinner_hint_emp_name));
        spEmpNames.setAdapter(mEmpSpinnerAdapter);


        spEmpNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                if(position==0)
                    selectedEmployee=null;
                else {
                    selectedEmployee = parentView.getItemAtPosition(position).toString();
                    selectedOffice = null;
                    spOfficeNames.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                selectedEmployee=null;
            }

        });
    }

}
