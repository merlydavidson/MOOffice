package com.microoffice.app.ui.activities;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.microoffice.app.R;
import com.microoffice.app.ui.adapters.CustomSpinnerAdapter;
import com.microoffice.app.ui.adapters.EnquiryListRecyclerAdapter;
import com.microoffice.app.ui.database.DatabaseHelperORM;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
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
import moffice.meta.com.molibrary.models.dbmodels.EnquiryDetails;
import moffice.meta.com.molibrary.utility.CommonDataArea;
import moffice.meta.com.molibrary.utility.LogWriter;

public class EnquiryListActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @Bind(R.id.ivCrossWhite)
    ImageView ivCrossWhite;

    @Bind(R.id.llStartDate)
    LinearLayout llStartDate;

    @Bind(R.id.llEndDate)
    LinearLayout llEndDate;

    @Bind(R.id.tvStartDate)
    TextView tvStartDate;

    @Bind(R.id.tvEndDate)
    TextView tvEndDate;

    @Bind(R.id.etSearch)
    EditText etSearch;

    @Bind(R.id.etSearchStatus)
    Spinner etSearchStatus;

    @Bind(R.id.tvFilterReset)
    TextView tvFilterReset;

    @Bind(R.id.rvEnquiryList)
    RecyclerView rvEnquiryList;

    @Bind(R.id.llFilterData)
    LinearLayout llFilterData;

    @Bind(R.id.frm_enqry_customer)
    FrameLayout frm_customer;

    @Bind(R.id.frm_enqry_status)
    FrameLayout frm_status;

    private AdView mAdView;

    TextView tvWhichView;
    private Calendar calendar;
    private int dd, mm, yyyy;
    private boolean isFilter = true;
    Date startDate,endDate;
    String empName,empstatus;
    String myFormat = "dd/MM/yyyy"; //In which you need put here
    DateFormat sdf = new SimpleDateFormat(myFormat);
    CustomSpinnerAdapter mUserSpinnerAdapter=null;
    CustomSpinnerAdapter mStatusSpinnerAdapter=null;
    private EnquiryListRecyclerAdapter mRecyclerAdapter;
    private List<EnquiryDetails> mEnquiryList = new ArrayList<>();
    private List<EnquiryDetails> mFilteredEnqList = new ArrayList<>();

    private DatabaseHelperORM databaseHelperORM = null;

    View.OnClickListener mClickListner = new View.OnClickListener() {
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
                    getDBEnquiryDetails();
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_enquiry_list);

            ButterKnife.bind(EnquiryListActivity.this);
            mAdView=(AdView)findViewById(R.id.adView);

            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            calendar = Calendar.getInstance(TimeZone.getDefault());
            ivCrossWhite.setOnClickListener(mClickListner);
            llStartDate.setOnClickListener(mClickListner);
            llEndDate.setOnClickListener(mClickListner);
            tvStartDate.setOnClickListener(mClickListner);
            tvEndDate.setOnClickListener(mClickListner);
            tvFilterReset.setOnClickListener(mClickListner);

            if (CommonDataArea.getInstType() != MOMain.MO_INSTTYPE_ADMIN) {
                etSearch.setEnabled(false);

            }
        }catch (Exception exp){
exp.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            //initUserSpinner();
            initStatusSpinner();
            tvStartDate.setText(sdf.format(new Date()));
            tvEndDate.setText(sdf.format(new Date()));
            Dao<EnquiryDetails, Integer> EnquiryDao ;
            List<EnquiryDetails> EnquiryArrayList ;

                EnquiryDao = CommonDataArea.getHelper(getApplicationContext()).getEnquiryDao();

                QueryBuilder<EnquiryDetails, Integer> queryBuilder =
                        EnquiryDao.queryBuilder();

                Where<EnquiryDetails, Integer> where = queryBuilder.where();
                where.eq("LogYear",  calendar.get(Calendar.YEAR));
               // where.and();
                where.eq("LogDayOfMonth", calendar.get(Calendar.DAY_OF_MONTH));
                //where.and();
                where.eq("LogMonth", calendar.get(Calendar.MONTH)+1);
                where.and(3);
            if(CommonDataArea.getInstType()!= MOMain.MO_INSTTYPE_ADMIN){
                frm_customer.setVisibility(View.GONE);
                frm_status.setVisibility(View.GONE);
                where.eq("name",CommonDataArea.getCommonSettings().getName());
                where.eq("assignedTo",CommonDataArea.getCommonSettings().getName());
                where.or(2);
                where.and(2);
            }
                EnquiryArrayList=queryBuilder.query();
                mRecyclerAdapter=new EnquiryListRecyclerAdapter((ArrayList<EnquiryDetails>)EnquiryArrayList,EnquiryListActivity.this);
                rvEnquiryList.setHasFixedSize(true);
                rvEnquiryList.setLayoutManager(new LinearLayoutManager(this));
                rvEnquiryList.setAdapter(mRecyclerAdapter);
                mRecyclerAdapter.notifyDataSetChanged();


            llFilterData.setVisibility(View.VISIBLE);


            CommonDataArea.closeHelper();

        } catch (SQLException e) {
            e.printStackTrace();
            LogWriter.writeLogException("EnquiryView",e);
        }catch (Exception exp){
            LogWriter.writeLogException("EnquiryView",exp);
        }
    }

    private void initUserSpinner() throws SQLException {
        ArrayList<String> user=null;
        Dao<EnquiryDetails,Integer> UserDao=null;

        try{

            UserDao=CommonDataArea.getHelper(getApplicationContext()).getEnquiryDao();
            QueryBuilder<EnquiryDetails, Integer> queryBuilder =
                    UserDao.queryBuilder();

            List<EnquiryDetails> distinctName= queryBuilder.distinct().selectColumns("name").query();
            if(distinctName.size()>0) {
                user = new ArrayList<String>() ;
                for (EnquiryDetails enq : distinctName) {
                    user.add(enq.getName());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // Resources passed to adapter to get image
        Resources res = getResources();

        user.add(0,"Select Created By");
        // Create custom adapter object ( see below CustomAdapter.java )
        mUserSpinnerAdapter = new CustomSpinnerAdapter(EnquiryListActivity.this,
                R.layout.custom_spinner_row, user, res, getString(R.string.spinner_hint_office_name));

      /*  etSearch.setAdapter(mUserSpinnerAdapter);

        etSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                empName=parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });*/

    }

    private void initStatusSpinner() throws SQLException {
        ArrayList<String> status=null;
        Dao<EnquiryDetails,Integer> StatusDao=null;

        try{
            StatusDao=CommonDataArea.getHelper(getApplicationContext()).getEnquiryDao();
            QueryBuilder<EnquiryDetails, Integer> queryBuilder =
                    StatusDao.queryBuilder();

            List<EnquiryDetails> distinctStatus= queryBuilder.distinct().selectColumns("Status").query();
            if(distinctStatus.size()>0) {
                status = new ArrayList<String>() ;
                for (EnquiryDetails enq : distinctStatus) {
                    status.add(enq.getStatus());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        // Resources passed to adapter to get image
        Resources res = getResources();


        // Create custom adapter object ( see below CustomAdapter.java )
        mStatusSpinnerAdapter = new CustomSpinnerAdapter(EnquiryListActivity.this,
                R.layout.custom_spinner_row, status, res, getString(R.string.spinner_hint_office_name));

//        // Set adapter to spinner
        etSearchStatus.setAdapter(mStatusSpinnerAdapter);
//
//        // Listener called when spinner item selected
        etSearchStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                empstatus=parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }


    private void filter(String s) {
        List<EnquiryDetails> temp = new ArrayList();
        for(EnquiryDetails d: mEnquiryList){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getName().contains(s)){
                temp.add(d);
            }
        }
        //update recyclerview
        updateList(temp);
    }

    public void updateList(List<EnquiryDetails> list){
        mFilteredEnqList.clear();
        mFilteredEnqList.addAll(list);
        mRecyclerAdapter.notifyDataSetChanged();
    }

    private void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(EnquiryListActivity.this, this,
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

    private void getDBEnquiryDetails() {
        if (Validate()) {
            String start = tvStartDate.getText().toString().trim();
            String end = tvEndDate.getText().toString().trim();
            try {
                startDate=sdf.parse(start);
                endDate=sdf.parse(end);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String searchStr = etSearch.getText().toString();
            try {

                String startDay = new SimpleDateFormat("dd").format(startDate);
                String endDay = new SimpleDateFormat("dd").format(endDate);
                String startMonth = new SimpleDateFormat("MM").format(startDate);
                String endMonth = new SimpleDateFormat("MM").format(endDate);
                String startYear = new SimpleDateFormat("yyyy").format(startDate);
                String endYear = new SimpleDateFormat("yyyy").format(endDate);






                Dao<EnquiryDetails, Integer> EnquiryDao ;
                List<EnquiryDetails> enquiryDetailsList ;
                EnquiryDao = CommonDataArea.getHelper(getApplicationContext()).getEnquiryDao();
                QueryBuilder<EnquiryDetails, Integer> queryBuilder =
                        EnquiryDao.queryBuilder();

                Where<EnquiryDetails, Integer> where = queryBuilder.where();
                where.between("LogDayOfMonth",startDay,endDay);

                where.between("LogMonth",startMonth,endMonth);

                where.between("LogYear",startYear,endYear);
                where.and(3);
                if(CommonDataArea.getInstType()!= MOMain.MO_INSTTYPE_ADMIN){
                    frm_customer.setVisibility(View.GONE);
                    frm_status.setVisibility(View.GONE);
                    where.eq("name",CommonDataArea.getCommonSettings().getName());
                    where.eq("assignedTo",CommonDataArea.getCommonSettings().getName());
                    where.or(2);
                    where.and(2);
                }
                else if(CommonDataArea.getInstType()== MOMain.MO_INSTTYPE_ADMIN){
                    where.like("name","%"+searchStr+"%");
                    where.like("custName","%"+searchStr+"%");
                    where.or(2);
                    where.and(2);
                }



            enquiryDetailsList=queryBuilder.query();












//                Dao<EnquiryDetails, Integer> EnquiryDao ;
//                List<EnquiryDetails> enquiryDetailsList ;
//                EnquiryDao = CommonDataArea.getHelper(getApplicationContext()).getEnquiryDao();
//                QueryBuilder<EnquiryDetails, Integer> queryBuilder =
//                        EnquiryDao.queryBuilder();
//
//                Where<EnquiryDetails, Integer> where = queryBuilder.where();
//                where.eq("status",empstatus);
//
//                where.between("LogDayOfMonth",startDay,endDay);
//
//                where.between("LogMonth",startMonth,endMonth);
//
//                where.between("LogYear",startYear,endYear);
//                where.and(4);
//                if(CommonDataArea.getInstType()!= MOMain.MO_INSTTYPE_ADMIN){
//                    where.eq("name",CommonDataArea.getCommonSettings().getName());
//                    where.eq("assignedTo",CommonDataArea.getCommonSettings().getName());
//                    where.or(2);
//                    where.and(2);
//                }else if(CommonDataArea.getInstType()== MOMain.MO_INSTTYPE_ADMIN){
//                    where.like("name","%"+searchStr+"%");
//                    where.like("custName","%"+searchStr+"%");
//                    where.or(2);
//                    where.and(2);
//                }

                mRecyclerAdapter=new EnquiryListRecyclerAdapter((ArrayList<EnquiryDetails>)enquiryDetailsList,this);
                rvEnquiryList.setLayoutManager(new LinearLayoutManager(this));
                rvEnquiryList.setAdapter(mRecyclerAdapter);
//                mEnquiryList.clear();
//                mEnquiryList = getHelper().getAllWhere(EnquiryDetails.class, startDate, endDate);
//                Log.d("test", "EnquiryDetails " + mEnquiryList.size());
//                mFilteredEnqList.clear();
//                mFilteredEnqList.addAll(mEnquiryList);
                mRecyclerAdapter.notifyDataSetChanged();
                llFilterData.setVisibility(View.VISIBLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean Validate() {
        tvStartDate.setError(null);
        tvEndDate.setError(null);
        if (tvStartDate.getText().toString().trim().equals("Select Date")) {
            tvStartDate.setError("Invalid Date!");
            return false;
        }
        if (tvEndDate.getText().toString().trim().equals("Select Date")) {
            tvEndDate.setError("Invalid Date!");
            return false;
        }

        return true;
    }

    private DatabaseHelperORM getHelper() {
        if (databaseHelperORM == null) {
            databaseHelperORM = OpenHelperManager.getHelper(this, DatabaseHelperORM.class);

        }
        return databaseHelperORM;
    }
}
