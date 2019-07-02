package com.microoffice.app.ui.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.microoffice.app.R;
import com.microoffice.app.ui.adapters.MeetingListRecyclerAdapter;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;
import moffice.meta.com.molibrary.core.MOVisitManager;
import moffice.meta.com.molibrary.models.dbmodels.VisitLog;
import moffice.meta.com.molibrary.models.dbmodels.VisitMeetLog;
import moffice.meta.com.molibrary.utility.CommonDataArea;
import moffice.meta.com.molibrary.utility.LogWriter;

public class VisitMeetingList extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static String curVisUUID;
    public static VisitMeetLog curMeetingLog;
    public static VisitLog visLog;
    private AdView mAdView;
    @Bind(R.id.rvMeetingList)
    RecyclerView rvMeetingList;
    @Bind(R.id.btMlAddToMeeting)
    Button btAddToMeetingList;

    @Bind(R.id.vmPersonMet)
    TextView txtPersonMet;
    @Bind(R.id.vmPersonTitle)
    TextView txtPersonTitle;

    @Bind(R.id.loDatEntry)
    ScrollView loDataEntry;

    @Bind(R.id.mlFilterData)
    LinearLayout mlFilterData;

    @Bind(R.id.etLvNote)
    TextView txtNote;

    @Bind(R.id.tbVisMeet)
    Toolbar tbMeetingList;

    @Bind(R.id.vmNextDate)
    TextView txtNextDate;

    List<VisitMeetLog> meetingList;
    MeetingListRecyclerAdapter meetingListAdapter;

    private Calendar calendar;
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_meeing_req);
        ButterKnife.bind(VisitMeetingList.this);
        txtPersonMet.setEnabled(true);
        calendar = Calendar.getInstance(TimeZone.getDefault());
        mAdView=(AdView)findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        if (tbMeetingList != null) {
            setSupportActionBar(tbMeetingList);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Meeting List");

        }

        btAddToMeetingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String person = txtPersonMet.getText().toString();
                    String title = txtPersonTitle.getText().toString();
                    String note = txtNote.getText().toString();
                    String bizName = visLog.getBusinessName();
                    long timeStamp = visLog.getTimeStamp();
                    String nextDate = txtNextDate.getText().toString();
                    String curMeetUUID = null;
                    if(curMeetingLog!=null) curMeetUUID = curMeetingLog.getRecUUID();

                    if(txtPersonMet.getText().toString().length()>0)//&&(txtNextDate.getText().toString().length()>0)) {
                    {
                        if(MOVisitManager.saveMeetingInfo(VisitMeetingList.this, person,bizName, title,timeStamp, note, 0, curVisUUID, curMeetUUID,nextDate));
                        {
                            txtPersonMet.setText("");
                            txtPersonTitle.setText("");
                            txtNote.setText("");
                            txtNextDate.setText("");
                            Toast.makeText(VisitMeetingList.this, "Meeting added", Toast.LENGTH_LONG).show();
                            UpdateMeetingList();

                        }
                    } else {
                        Toast.makeText(VisitMeetingList.this,"Missing person name or next visit date",Toast.LENGTH_LONG).show();
                        return;
                    }



                }catch(Exception exp){

                }
            }
        });

        txtNextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateMeetingList();

    }

    private void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(VisitMeetingList.this, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

       int yyyy = year;
       int mm = monthOfYear;
       int dd = dayOfMonth;

        txtNextDate.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(dd).append("/").append(mm + 1).append("/").append(yyyy).append(" "));

    }

    void UpdateMeetingList(){
        try {
           // CommonDataArea.getHelper(getApplicationContext()).createVisitMeetingTable();
            Dao<VisitMeetLog, Integer> meetingListDao = CommonDataArea.getHelper(getApplicationContext()).getVisitMeetingDao();
            QueryBuilder<VisitMeetLog, Integer> queryBuilder =
                    meetingListDao.queryBuilder();

            Where<VisitMeetLog, Integer> where = queryBuilder.where();
            where.eq("VisitUUID",curVisUUID );
            where.and();
            where.lt("Status",2 ); //not deleted
            meetingList = queryBuilder.query();
            //    visitArrayList = VisitDao.queryForAll();

            meetingListAdapter = new MeetingListRecyclerAdapter(this,(ArrayList<VisitMeetLog>) meetingList);
            rvMeetingList.setHasFixedSize(true);
            rvMeetingList.setLayoutManager(new LinearLayoutManager(this));
            rvMeetingList.setAdapter(meetingListAdapter);
            meetingListAdapter.notifyDataSetChanged();

            mlFilterData.setVisibility(View.VISIBLE);
        }catch(SQLException exp){
            LogWriter.writeLogException("MeetingLog",exp);

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return false;

        }

    }
}
