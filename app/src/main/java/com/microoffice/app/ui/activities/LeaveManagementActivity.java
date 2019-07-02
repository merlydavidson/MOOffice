package com.microoffice.app.ui.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.microoffice.app.R;
import com.microoffice.app.ui.adapters.AttendenceListRecylerAdapter;
import com.microoffice.app.ui.adapters.CustomSpinnerAdapter;

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
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import moffice.meta.com.molibrary.core.MODataDispatchThread;
import moffice.meta.com.molibrary.core.MOLeaveManager;
import moffice.meta.com.molibrary.core.MOMain;
import moffice.meta.com.molibrary.core.Settings;
import moffice.meta.com.molibrary.models.dbmodels.AttnLog;
import moffice.meta.com.molibrary.models.dbmodels.LeaveDetails;
import moffice.meta.com.molibrary.models.dbmodels.LeaveList;
import moffice.meta.com.molibrary.models.dbmodels.UserList;
import moffice.meta.com.molibrary.utility.CommonDataArea;
import moffice.meta.com.molibrary.utility.LogWriter;

public class LeaveManagementActivity extends AppCompatActivity implements View.OnClickListener
        , DatePickerDialog.OnDateSetListener {
    @Bind(R.id.tbReqLeave)
    Toolbar tbLeave;
    @Bind(R.id.spTypeOfLeave)
    Spinner spTypeOfLeave;
    int selectedLeaveType;
    CustomSpinnerAdapter mEmpSpinnerAdapter = null;
    @Bind(R.id.btLeaveReq)
    Button leaveRequest;
    @Bind(R.id.llStartDate)
    LinearLayout llStartDate;
    Calendar calendar;
    @Bind(R.id.leavedays)
    EditText leaveDays;
    @Bind(R.id.tvStartDate)
    TextView tvStartDate;
    @Bind(R.id.edtReasonForLeave)
    EditText leaveReason;
    private AdView mAdView;

    TextView tvWhichView;
    String myFormat = "dd/MM/yyyy";
    int dd, mm, yyyy;
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_management);
        ButterKnife.bind(LeaveManagementActivity.this);
        calendar = Calendar.getInstance(TimeZone.getDefault());
        mAdView=(AdView)findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        if (tbLeave != null) {
            setSupportActionBar(tbLeave);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Leave ");
        }
        try {


            initTypeOfLeaveSpinner();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvWhichView = tvStartDate;
        llStartDate.setOnClickListener(this);
        //llEndDate.setOnClickListener(this);
        tvStartDate.setOnClickListener(this);
        //tvEndDate.setOnClickListener(this);
        leaveRequest.setOnClickListener(this);
    }

    private void initTypeOfLeaveSpinner() throws SQLException {


        List<String> employee = null;

        employee = new ArrayList<>();
        Resources res = getResources();
        employee.add(0, "Select Leave Type");
        employee.add(MOMain.LEAVETYPE_CASUAL, "Casual Leave");
        employee.add(MOMain.LEAVETYPE_EARNED, "Earned Leave");
        employee.add(MOMain.LEAVETYPE_LOP, "LOP");


        mEmpSpinnerAdapter = new CustomSpinnerAdapter(LeaveManagementActivity.this,
                R.layout.custom_spinner_row, employee, res,
                getString(R.string.spinner_hint_leave_type));

        spTypeOfLeave.setAdapter(mEmpSpinnerAdapter);


        spTypeOfLeave.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                if (position == 0)
                    selectedLeaveType = 0;
                else {
                    selectedLeaveType = position;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                selectedLeaveType = 0;
            }

        });
    }


    private void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(LeaveManagementActivity.this, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();


        tvStartDate.setText(sdf.format(new Date()));



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.llStartDate:
            case R.id.tvStartDate:
                tvWhichView = tvStartDate;
                showDatePicker();
                break;

            case R.id.llEndDate:

            case R.id.btLeaveReq:
                try {
                    markLeave();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


        }
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

    public void markLeave() throws Exception {
        String startDate = tvStartDate.getText().toString();
        String endDate = "";
        if (startDate.equalsIgnoreCase("")) {
            tvStartDate.setError("choose start date");
        } else if (leaveReason.getText().toString().equalsIgnoreCase("")) {
            leaveReason.setError("Enter reason for leave");
        } else if (leaveDays.getText().toString().equalsIgnoreCase("")) {
            leaveReason.setError("Enter leave days");
        } else if ((Float.parseFloat(leaveDays.getText().toString())) % 0.5 != 0) {
            leaveDays.setError("Enter correct days");
        } else if (selectedLeaveType == 0) {
//            TextView errorText = (TextView) spTypeOfLeave.getSelectedView();
//            errorText.setError("Choose a type");
        } else

        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Leave Request");
            alertDialogBuilder.setMessage("Are you sure about this request?");
            alertDialogBuilder.setPositiveButton("Request leave", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    try {
                        markLeaveValues();
                        Toast.makeText(getApplicationContext(), "Leave marked", Toast.LENGTH_LONG).show();
                        clearAllFields();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    try {
                        Toast.makeText(getApplicationContext(), "Leave not marked", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
    }

    public void markLeaveValues() throws Exception {
        float conVAlue;
        int finalValue = 0;
        String startDate = tvStartDate.getText().toString();
        String endDate = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar currentTime = Calendar.getInstance();

        Double val = Float.parseFloat(leaveDays.getText().toString()) / 0.5;
        if (val % 2 != 0) {
            conVAlue = (Float.parseFloat(leaveDays.getText().toString()));
            finalValue = Math.round(conVAlue);
        }
        else {
            finalValue=Integer.parseInt(leaveDays.getText().toString());
        }

        SimpleDateFormat df = new SimpleDateFormat("a");
        String currentTimeStr = df.format(currentTime.getTime());

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c1 = Calendar.getInstance();
        c1.setTime(sdf1.parse(startDate));
        c1.add(Calendar.DATE, (finalValue-1));  // number of days to add
        endDate = sdf1.format(c1.getTime());
        Log.d("afas", startDate);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(sdf1.parse(startDate));
        long startTimeStamp=c2.getTimeInMillis();

        Settings commonSettings = CommonDataArea.getCommonSettings();
        UUID uuid = UUID.randomUUID();
        String recUUID = uuid.toString();
        LeaveList leaveDetails = new LeaveList();
        leaveDetails.setUserName(commonSettings.getName());
        leaveDetails.setUserUUID(CommonDataArea.getUserUUID());
        leaveDetails.setNotes(leaveReason.getText().toString());
        leaveDetails.setLvType(selectedLeaveType);
        leaveDetails.setStartDat(startDate);
        leaveDetails.setEndDate(endDate);
        leaveDetails.setLvStatus(1);
        leaveDetails.setRejectionNotes("");
        leaveDetails.setSendStatus(MOMain.SENDSTATUS_NOT_SEND);
        leaveDetails.setRecUUID(recUUID);
        leaveDetails.setStartDatTimeStamp(startTimeStamp);
        leaveDetails.setNumDays(Float.parseFloat(leaveDays.getText().toString()));
        leaveDetails.setDateTime(currentTimeStr);
        leaveDetails.setTimeStamp(currentTime.getTimeInMillis());
        new MOLeaveManager(this, leaveDetails);
    }

    public void clearAllFields() {
        tvStartDate.setText("");

        leaveReason.setText("");
        leaveDays.setText("");
        spTypeOfLeave.setSelection(0);
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
}
