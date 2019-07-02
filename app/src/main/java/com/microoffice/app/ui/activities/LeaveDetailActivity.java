package com.microoffice.app.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.microoffice.app.R;
import com.microoffice.app.ui.adapters.LeaveDetailAdapter;
import com.microoffice.app.ui.adapters.LeaveListAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import moffice.meta.com.molibrary.models.dbmodels.LeaveList;
import moffice.meta.com.molibrary.utility.CommonDataArea;

public class LeaveDetailActivity extends AppCompatActivity {
    @Bind(R.id.toolbarleaveDetails)
    Toolbar toolbar;

    @Bind(R.id.rvAttendanceListdetail)
    RecyclerView rvAttendanceListDetail;

    @Bind(R.id.empty_view_detail)
    TextView emptyView;

    @Bind(R.id.userName)
    TextView userName;

    LeaveDetailAdapter
            leaveDetailAdapter;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_detail);
        ButterKnife.bind(LeaveDetailActivity.this);

        if (toolbar != null) {

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.tb_title_mark_attendance));
        }
        mAdView=(AdView)findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        Bundle bundle=getIntent().getExtras();
        String uuid=bundle.getString("uuid");
        List<LeaveList> leaveLists = null;

        rvAttendanceListDetail.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(LeaveDetailActivity.this);
        rvAttendanceListDetail.setLayoutManager(linearLayoutManager);
        try {
           leaveLists =getUserLeaves(uuid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(leaveLists.size()>0){
        leaveDetailAdapter = new LeaveDetailAdapter(this,leaveLists);

        rvAttendanceListDetail.setAdapter(leaveDetailAdapter);}

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
    public List<LeaveList> getUserLeaves(String uuid) throws SQLException {
        Dao<LeaveList, Integer> LeaveDao;
        List<LeaveList> leaveArrayList;
        LeaveDao = CommonDataArea.getHelper(this).getLeaveLogDao();
        QueryBuilder<LeaveList, Integer> queryBuilder1 =
                LeaveDao.queryBuilder();
        Where<LeaveList, Integer> where = queryBuilder1.where();
        where.eq("UserUUID", uuid);

        leaveArrayList = queryBuilder1.query();
        CommonDataArea.closeHelper();
        userName.setText(leaveArrayList.get(0).getUserName());
        return leaveArrayList;
    }
}
