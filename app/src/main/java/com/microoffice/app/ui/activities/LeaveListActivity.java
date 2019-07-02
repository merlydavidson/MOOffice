package com.microoffice.app.ui.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.microoffice.app.R;
import com.microoffice.app.ui.adapters.AttendenceListRecylerAdapter;
import com.microoffice.app.ui.adapters.CustomSpinnerAdapter;
import com.microoffice.app.ui.adapters.LeaveListAdapter;

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
import mehdi.sakout.fancybuttons.FancyButton;
import moffice.meta.com.molibrary.core.MOMain;
import moffice.meta.com.molibrary.models.dbmodels.AttnLog;
import moffice.meta.com.molibrary.models.dbmodels.LeaveList;
import moffice.meta.com.molibrary.models.dbmodels.Offices;
import moffice.meta.com.molibrary.models.dbmodels.UserList;
import moffice.meta.com.molibrary.utility.CommonDataArea;
import moffice.meta.com.molibrary.utility.LogWriter;

public class LeaveListActivity extends AppCompatActivity implements View.OnClickListener
        , DatePickerDialog.OnDateSetListener {



    @Bind(R.id.spEmpNames)
    Spinner spEmpNames;
    @Bind(R.id.spLeaveStatus)
    Spinner spLeaveStatus;
    @Bind(R.id.llStartDate)
    LinearLayout llStartDate;
    @Bind(R.id.empty_view)
    TextView emptyText;

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
    @Bind(R.id.toolbarleave)
    Toolbar toolbar;

    @Bind(R.id.employeeLayout)
    FrameLayout employeelayout;


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
    private LeaveListAdapter mLeaveListRecylerAdapter;
    private LinearLayout llattnView;
    List<String> office = null;
    String start="";
    String end="";
int leavStatus=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_list);

        ButterKnife.bind(LeaveListActivity.this);
        calendar = Calendar.getInstance(TimeZone.getDefault());
        mAdView=(AdView)findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        if (toolbar != null) {

           setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
           getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.tb_title_mark_attendance));
        }

        try {
            if (CommonDataArea.getInstType() != MOMain.MO_INSTTYPE_ASSOS) {

                initSpinner();
                initLeaveTypeSpinner();
            } else {
initLeaveTypeSpinner();
               employeelayout.setVisibility(View.GONE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvWhichView = tvStartDate;
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
        try {
            start="";
            end ="";
            listResult();
        } catch (SQLException e) {
            e.printStackTrace();
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

    boolean datePicked =false;
    private void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(LeaveListActivity.this, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
        datePicked=true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.llStartDate:
            case R.id.tvStartDate:
                tvWhichView = tvStartDate;
                showDatePicker();
                break;


            case R.id.btn_view:
                Intent intent=new Intent(LeaveListActivity.this,LeaveSummaryActivity.class);
                startActivity(intent);
                break;

            case R.id.tvEndDate:
                tvWhichView = tvEndDate;
                showDatePicker();
                break;
            case R.id.tvFilterReset:
                try {
                     start= tvStartDate.getText().toString();
                     end  = tvEndDate.getText().toString();
                     listResult();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_leaveRequest:
                Intent intentSummary=new Intent(LeaveListActivity.this,LeaveManagementActivity.class);
                startActivity(intentSummary  );
                break;
        }
    }

    public void listResult() throws SQLException {

        Dao<LeaveList, Integer> attnListChk = CommonDataArea.getHelper(this).getLeaveLogDao();

        if (start.equals("Select Date") || end.equals("Select Date")) {
            new AlertDialog.Builder(this)
                    .setTitle("Message")
                    .setMessage("please select a start and end date")
                    .setPositiveButton("OK", null).show();
        } else {
            SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy");

            java.util.Date date = null, endate = null;
            long startTimeStamp =0;
            long endTimeStamp =0;
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

                Dao<LeaveList, Integer> LeaveDao;
                List<LeaveList> leaveArrayList;
                LeaveDao = CommonDataArea.getHelper(getApplicationContext()).getLeaveLogDao();
                QueryBuilder<LeaveList, Integer> queryBuilder1 =
                        LeaveDao.queryBuilder();
                Where<LeaveList, Integer> where = queryBuilder1.where();
                if (CommonDataArea.getInstType() == MOMain.MO_INSTTYPE_ASSOS) {
                    where.eq("UserUUID", CommonDataArea.getUserUUID());
                    where.and();
                    where.eq("lvActiveRec",MOMain.RECSTATUS_ACTIVE);
                    where.and();
                } else {
                    if (selectedEmployee != null) {
                        where.eq("UserName", selectedEmployee);
                        where.and();
                    }
                }
               if ((datePicked)&&(startTimeStamp !=0) && (endTimeStamp != 0)) {
                    where.between("StartDatTimeStamp", startTimeStamp,endTimeStamp);
                    where.and();
                }
                if(leavStatus !=0) {
                    where.eq("LvStatus", leavStatus);
                }
                else {
                    where.between("LvStatus", MOMain.LEAVESTATUS_REQUESTED,MOMain.LEAVESTATUS_DISCARDED);
                }
                queryBuilder1.orderBy("TimeStamp", true);

                leaveArrayList = queryBuilder1.query();
                rvAttendanceList.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LeaveListActivity.this);
                rvAttendanceList.setLayoutManager(linearLayoutManager);
                if(leaveArrayList.size()<=0) {
                    emptyText.setVisibility(View.VISIBLE);
                    rvAttendanceList.setVisibility(View.GONE);
                }
                else {
                    emptyText.setVisibility(View.GONE);
                    rvAttendanceList.setVisibility(View.VISIBLE);
                }
                mLeaveListRecylerAdapter = new LeaveListAdapter((ArrayList<LeaveList>) leaveArrayList,this);
                llFilterData.setVisibility(View.VISIBLE);
                rvAttendanceList.setAdapter(mLeaveListRecylerAdapter);
                CommonDataArea.closeHelper();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        yyyy = year;
        mm = monthOfYear;
        dd = dayOfMonth;

        tvWhichView.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(dd).append("/").append(mm + 1).append("/").append(yyyy).append(" "));
    }


    private void initSpinner() throws SQLException {
        //Link between employee and office is now not established. So list all employees
        // selectedOffice=spOfficeNames.getSelectedItem().toString();

        List<String> employee = null;
        Dao<UserList, Integer> employeeDao = null;

        try {

            employeeDao = CommonDataArea.getHelper(getApplicationContext()).getUserListDao();
            employee = employeeDao.queryRaw("SELECT Name FROM UserList ", new RawRowMapper<String>() {
                @Override
                public String mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
                    return resultColumns[0];
                }
            }).getResults();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Resources res = getResources();
        employee.add(0, "Select Employee");

        mEmpSpinnerAdapter = new CustomSpinnerAdapter(LeaveListActivity.this,
                R.layout.custom_spinner_row, employee, res,
                getString(R.string.spinner_hint_emp_name));
        spEmpNames.setAdapter(mEmpSpinnerAdapter);


        spEmpNames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                if (position == 0)
                    selectedEmployee = null;
                else {
                    selectedEmployee = parentView.getItemAtPosition(position).toString();
                    selectedOffice = null;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                selectedEmployee = null;
            }

        });

    }

public void initLeaveTypeSpinner()
{

    List<String> statusSp = new ArrayList<String>();
    statusSp.add("All");
    statusSp.add("Requested");
    statusSp.add("Approved");
    statusSp.add("Rejected");
    statusSp.add("Discarded");
    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, statusSp);

    // Drop down layout style - list view with radio button
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    // attaching data adapter to spinner
    spLeaveStatus.setAdapter(dataAdapter);
    spLeaveStatus.setSelection(1);
    spLeaveStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            leavStatus=i;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    });
}
}