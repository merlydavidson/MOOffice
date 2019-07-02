package com.microoffice.app.ui.activities;

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
import android.widget.FrameLayout;
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
import com.microoffice.app.ui.adapters.CustomSpinnerAdapter;
import com.microoffice.app.ui.adapters.LeaveListAdapter;
import com.microoffice.app.ui.adapters.LeaveSummaryAdapter;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import mehdi.sakout.fancybuttons.FancyButton;
import moffice.meta.com.molibrary.core.MOMain;
import moffice.meta.com.molibrary.models.dbmodels.LeaveList;
import moffice.meta.com.molibrary.models.dbmodels.LeaveSummary;
import moffice.meta.com.molibrary.models.dbmodels.UserList;
import moffice.meta.com.molibrary.utility.CommonDataArea;

public class LeaveSummaryActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.spEmpName)
    Spinner spEmpNames;
    @Bind(R.id.empty_view_summary)
    TextView emptyText;
    @Bind(R.id.emplyllot)
    FrameLayout employeelayout;
    @Bind(R.id.toolbarsummary)
    Toolbar toolbarSummary;
    @Bind(R.id.rvAttendanceSummaryList)
    RecyclerView rvAttenedanceSummary;

    @Bind(R.id.tvFilterResetSummary)
    TextView tvFilterReset;

    @Bind(R.id.casualLeave)
    TextView casualLeavetv;
    @Bind(R.id.earnedLeave)
    TextView earnedLeavetv;
    @Bind(R.id.lopLeave)
    TextView lopLeavetv;
    @Bind(R.id.totalLeave)
    TextView totalLeavetv;
    @Bind(R.id.emplyeellt)
    LinearLayout emplyllt;

    private AdView mAdView;
    @Bind(R.id.btn_leaveRequest)
    FancyButton fabRequestLeave;
    @Bind(R.id.btn_view)
    FancyButton fabSummary;

    CustomSpinnerAdapter mEmpSpinnerAdapter = null;
    String selectedEmployee;
    private LeaveSummaryAdapter mLeaveSummaryListRecylerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_summary);
        ButterKnife.bind(LeaveSummaryActivity.this);
        fabRequestLeave.setOnClickListener(this);
        fabSummary.setOnClickListener(this);
        tvFilterReset.setOnClickListener(this);
        mAdView=(AdView)findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        if (toolbarSummary != null) {

            setSupportActionBar(toolbarSummary);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.tb_title_leaveSummary_list));
        }
        try {
            if (CommonDataArea.getInstType() != MOMain.MO_INSTTYPE_ASSOS) {

                initSpinner();
            } else {

                employeelayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            getValues();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initSpinner() throws Exception {
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
        mEmpSpinnerAdapter = new CustomSpinnerAdapter(LeaveSummaryActivity.this,
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
                    try {
                        getValues();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                selectedEmployee = null;
            }

        });


    }

    public void getValues() throws Exception {
        List<String> employee = null;
        int casualLeave = 0;
        int earnedLeave = 0;
        int lop = 0;
        int total = 0;
        String name = "";
        LeaveSummary leaveSummary = null;
        String uuid = "";
        ArrayList<LeaveSummary> leaveListsSummaryArray = new ArrayList<>();
        Dao<LeaveList, Integer> LeaveDao;
        List<LeaveList> leaveArrayList;
        LeaveDao = CommonDataArea.getHelper(getApplicationContext()).getLeaveLogDao();
        QueryBuilder<LeaveList, Integer> queryBuilder1 =
                LeaveDao.queryBuilder();
        Where<LeaveList, Integer> where = queryBuilder1.where();
        Dao<UserList, Integer> employeeDao = null;
        try {
            employeeDao = CommonDataArea.getHelper(getApplicationContext()).getUserListDao();
            if(selectedEmployee==null) {
                employee = employeeDao.queryRaw("SELECT UserGUID FROM UserList ", new RawRowMapper<String>() {
                    @Override
                    public String mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
                        return resultColumns[0];
                    }
                }).getResults();
            }else {
                employee = employeeDao.queryRaw("SELECT UserGUID FROM UserList where Name like '"+selectedEmployee+"%'", new RawRowMapper<String>() {
                    @Override
                    public String mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
                        return resultColumns[0];
                    }
                }).getResults();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        leaveSummary = new LeaveSummary();
        if (CommonDataArea.getInstType() == MOMain.MO_INSTTYPE_ASSOS) {
            where.eq("UserUUID", CommonDataArea.getUserUUID());
            where.and();
            where.eq("LvStatus", MOMain.LEAVESTATUS_APROVED);
            leaveArrayList = queryBuilder1.query();

            if (leaveArrayList.size() > 0) {
                for (int i = 0; i < leaveArrayList.size(); i++) {
                    if (leaveArrayList.get(i).getLvType() == MOMain.LEAVETYPE_CASUAL) {
                        casualLeave += leaveArrayList.get(i).getNumDays();
                    }
                    if (leaveArrayList.get(i).getLvType() == MOMain.LEAVETYPE_EARNED) {
                        earnedLeave += leaveArrayList.get(i).getNumDays();
                    }
                    if (leaveArrayList.get(i).getLvType() == MOMain.LEAVETYPE_LOP) {
                        lop += leaveArrayList.get(i).getNumDays();;

                    }
                    name = leaveArrayList.get(i).getUserName();
                    uuid = leaveArrayList.get(1).getUserUUID();
                }
                total = casualLeave + earnedLeave + lop;
                if (total != 0 || casualLeave != 0 || earnedLeave != 0 || lop != 0) {
                    leaveSummary.setUserName(name);
                    leaveSummary.setUuid(uuid);
                    leaveSummary.setCasualLeave(casualLeave);
                    leaveSummary.setEarnedlLeave(earnedLeave);
                    leaveSummary.setLopLeave(lop);
                    leaveSummary.setTotalLeave(total);
                    leaveListsSummaryArray.add(leaveSummary);
                }
            }
        } else {
                for (String emp : employee) {
                    casualLeave=0;
                    earnedLeave=0;
                    lop=0;
                    Where<LeaveList, Integer> where1 = queryBuilder1.where();
                    if (selectedEmployee != null) {
                        // where1.eq("UserUUID", emp);
                        // where1.and();
                        where1.eq("LvStatus", MOMain.LEAVESTATUS_APROVED);
                        where1.and();
                        where1.like("UserName", selectedEmployee);
                    } else {
                        where1.eq("UserUUID", emp);
                        where1.and();
                        where1.eq("LvStatus", MOMain.LEAVESTATUS_APROVED);
                    }

                    leaveArrayList = queryBuilder1.query();
                    leaveSummary = new LeaveSummary();
                    for (int i = 0; i < leaveArrayList.size(); i++) {
                        if (leaveArrayList.get(i).getLvType() == MOMain.LEAVETYPE_CASUAL) {
                            casualLeave += leaveArrayList.get(i).getNumDays();
                            ;
                        }
                        if (leaveArrayList.get(i).getLvType() == MOMain.LEAVETYPE_EARNED) {
                            earnedLeave += leaveArrayList.get(i).getNumDays();
                        }
                        if (leaveArrayList.get(i).getLvType() == MOMain.LEAVETYPE_LOP) {
                            lop += leaveArrayList.get(i).getNumDays();

                        }
                        name = leaveArrayList.get(i).getUserName();
                        uuid = leaveArrayList.get(i).getUserUUID();
                    }
                    total = casualLeave + earnedLeave + lop;
                    if (total != 0 || casualLeave != 0 || earnedLeave != 0 || lop != 0) {
                        leaveSummary.setUserName(name);
                        leaveSummary.setUuid(uuid);
                        leaveSummary.setCasualLeave(casualLeave);
                        leaveSummary.setEarnedlLeave(earnedLeave);
                        leaveSummary.setLopLeave(lop);
                        leaveSummary.setTotalLeave(total);
                        leaveListsSummaryArray.add(leaveSummary);
                    }
                }
        }
        rvAttenedanceSummary.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LeaveSummaryActivity.this);
        rvAttenedanceSummary.setLayoutManager(linearLayoutManager);
        if (leaveListsSummaryArray.size() <= 0) {
            emptyText.setVisibility(View.VISIBLE);
            rvAttenedanceSummary.setVisibility(View.GONE);
        } else {
            if (CommonDataArea.getInstType() != MOMain.MO_INSTTYPE_ASSOS) {
                emptyText.setVisibility(View.GONE);
                rvAttenedanceSummary.setVisibility(View.VISIBLE);
                emplyllt.setVisibility(View.GONE);
                mLeaveSummaryListRecylerAdapter = new LeaveSummaryAdapter(leaveListsSummaryArray, this);
                /// llFilterData.setVisibility(View.VISIBLE);
                rvAttenedanceSummary.setAdapter(mLeaveSummaryListRecylerAdapter);
            }
            else {
                spEmpNames.setVisibility(View.GONE);
                tvFilterReset.setVisibility(View.GONE);
                emplyllt.setVisibility(View.VISIBLE);
                emptyText.setVisibility(View.GONE);
                rvAttenedanceSummary.setVisibility(View.GONE);
              casualLeavetv.setText(String.valueOf(leaveListsSummaryArray.get(0).getCasualLeave()));
              earnedLeavetv.setText(String.valueOf(leaveListsSummaryArray.get(0).getEarnedlLeave()));
              lopLeavetv.setText(String.valueOf(leaveListsSummaryArray.get(0).getLopLeave()));
              totalLeavetv.setText(String.valueOf(leaveListsSummaryArray.get(0).getTotalLeave()));
            }
        }

        CommonDataArea.closeHelper();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.btn_view:
                Intent intent = new Intent(LeaveSummaryActivity.this, LeaveListActivity.class);
                startActivity(intent);
                break;

            case R.id.tvFilterReset:

                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.btn_leaveRequest:
                Intent intentSummary = new Intent(LeaveSummaryActivity.this, LeaveManagementActivity.class);
                startActivity(intentSummary);
                break;
        }
    }
}

