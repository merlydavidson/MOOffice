package com.microoffice.app.ui.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.microoffice.app.R;
import com.microoffice.app.ui.adapters.CallLogRecyclerAdapter;
import com.microoffice.app.ui.adapters.CustomSpinnerAdapter;
import com.microoffice.app.ui.database.DatabaseHelperORM;
import com.microoffice.app.utils.CallLogs;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RawRowMapper;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import moffice.meta.com.molibrary.models.dbmodels.EnquiryDetails;
import moffice.meta.com.molibrary.models.dbmodels.Products;
import moffice.meta.com.molibrary.models.dbmodels.UserList;
import moffice.meta.com.molibrary.utility.CommonDataArea;


public class LeaveReqActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    Toolbar tbEnquiry;
    EditText edUserName;
    EditText edUserPhNumber;
    Spinner edPoduct,edAssignedTo;
    EditText edDescription;
    EditText edDate;
    EditText edNote;
    Button enquiryBtn;
    String product;
    String assignedTo;
    String phNumber;
    String name;
    Cursor managedCursor;
    CustomSpinnerAdapter mProductSpinner = null;
    CustomSpinnerAdapter mAssignedToSpinner = null;
    ImageButton contactButton,addProduct;
    int mYear, mMonth, mDay;
    Calendar calendar;
    String myFormat = "dd/MM/yyyy"; //In which you need put here
    DateFormat sdf = new SimpleDateFormat(myFormat);
    private AdView mAdView;
    int dd, mm, yyyy;
    private AlertDialog dialog;
    public static CallLogRecyclerAdapter mCallLogListRecAdapter;
    ArrayList<CallLogs> modelClasses=new ArrayList<>();
    private DatabaseHelperORM databaseHelperORM = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_req);
        mAdView=(AdView)findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        calendar = Calendar.getInstance(TimeZone.getDefault());
        tbEnquiry=(Toolbar)findViewById(R.id.tbAddEnquiry);
        edUserName = (EditText) findViewById(R.id.etUserName);
        edAssignedTo= (Spinner) findViewById(R.id.etAssignedTo);
        edUserPhNumber = (EditText) findViewById(R.id.etMobileNo);
        edPoduct = (Spinner) findViewById(R.id.etProduct);
        edDescription = (EditText) findViewById(R.id.etDescription);
        edNote = (EditText) findViewById(R.id.etNote);
        enquiryBtn = (Button) findViewById(R.id.enquirybtn);
        edDate = (EditText) findViewById(R.id.etDate);
        contactButton=(ImageButton)findViewById(R.id.contactButton);
        addProduct=(ImageButton)findViewById(R.id.addButton);
        if (tbEnquiry != null) {
            setSupportActionBar(tbEnquiry);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Enquiry");
        }
        edDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                showDatePicker();

            }
        });
        Resources res = getResources();
        List<String> productList=null;
        Dao<Products,Integer> ProductDao=null;

        try{

            ProductDao= CommonDataArea.getHelper(getApplicationContext()).getProductDao();
            productList=ProductDao.queryRaw("SELECT ProductName FROM Products", new RawRowMapper<String >() {
                @Override
                public String mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
                    return resultColumns[0];
                }
            }).getResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mProductSpinner=  new CustomSpinnerAdapter(LeaveReqActivity.this,
                R.layout.custom_spinner_row, productList, res, getString(R.string.spinner_hint_office_name));
        edPoduct.setAdapter(mProductSpinner);
        edPoduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                product=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        final List<String> finalProductList = productList;
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LeaveReqActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.alert_add_product,null);
                builder.setView(dialogView);
                final EditText edtProduct = (EditText) dialogView.findViewById(R.id.prdtName);
                Button butOK = (Button) dialogView.findViewById(R.id.ok);
                Button butCANCEL = (Button) dialogView.findViewById(R.id.cancel);
                butOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finalProductList.add(edtProduct.getText().toString());
                        mProductSpinner.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                butCANCEL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog = builder.create();
                dialog.show();
            }
        });

        List<String> assignedToList=null;
        Dao<UserList,Integer> AssignedDao=null;

        try{

            AssignedDao=CommonDataArea.getHelper(getApplicationContext()).getUserListDao();
            assignedToList=AssignedDao.queryRaw("SELECT Name FROM UserList", new RawRowMapper<String >() {
                @Override
                public String mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
                    return resultColumns[0];
                }
            }).getResults();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mAssignedToSpinner=  new CustomSpinnerAdapter(LeaveReqActivity.this,
                R.layout.custom_spinner_row, assignedToList, res, getString(R.string.spinner_hint_office_name));
        edAssignedTo.setAdapter(mAssignedToSpinner);
        edAssignedTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                assignedTo=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LeaveReqActivity.this);
                    LayoutInflater inflater = getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.fragment_recentcalllogs,null);
                    builder.setView(dialogView);
                    final RecyclerView rvCallLogList=(RecyclerView)dialogView.findViewById(R.id.rvCallLogList);
                    rvCallLogList.setLayoutManager(new LinearLayoutManager(LeaveReqActivity.this));
                    mCallLogListRecAdapter=new CallLogRecyclerAdapter(LeaveReqActivity.this,modelClasses);
                    rvCallLogList.setAdapter(mCallLogListRecAdapter);
                    mCallLogListRecAdapter.notifyDataSetChanged();
                dialog = builder.create();
                dialog.show();
                LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mMessageReceiver,
                        new IntentFilter("log"));
            }

        });

        enquiryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date=new Date();
                Log.d("textValue", edUserName.getText().toString());
                if (edUserName.getText().toString().equals("")) {
                    edUserName.setError("enter a valid name");
                } else if (edUserPhNumber.getText().toString().equals("") || edUserPhNumber.getText().toString().length() < 10) {
                    edUserPhNumber.setError("enter a valid number");
                }  else if (edDate.getText().toString().equals("")) {
                    edDate.setError("enter a valid date");
                } else if (edDescription.getText().toString().equals("")) {
                    edDescription.setError("enter a valid description");
                }  else {
                    Products products=new Products();
                    products.setProductName(product);
                    EnquiryDetails enquiryDetails = new EnquiryDetails();
                    enquiryDetails.setName(edUserName.getText().toString());
                    enquiryDetails.setPhoneNumber(edUserPhNumber.getText().toString());
                    enquiryDetails.setProduct(product);
                    enquiryDetails.setDescription(edDescription.getText().toString());
                    enquiryDetails.setDate(edDate.getText().toString());
                    enquiryDetails.setAssignedTo(assignedTo);
                    enquiryDetails.setStatus("Open");
                    enquiryDetails.setLogYear(yyyy);
                    enquiryDetails.setLogMonth(mm+1);
                    enquiryDetails.setLogDayOfMonth(dd);
                    if(edNote.getText().toString().equals(""))
                        enquiryDetails.setNote("");
                    else
                        enquiryDetails.setNote(edNote.getText().toString());
                    try {
                        Dao<Products,Integer> productDao=CommonDataArea.getHelper(getApplicationContext()).getProductDao();
                        productDao.create(products);
                        Dao<EnquiryDetails, Integer> enquiryDetailses = CommonDataArea.getHelper(getApplicationContext()).getEnquiryDao();
                        enquiryDetailses.create(enquiryDetails);
                        Toast.makeText(LeaveReqActivity.this,"Record added successfully",Toast.LENGTH_LONG).show();
//                        showDialog();
                        CommonDataArea.closeHelper();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(LeaveReqActivity.this, this,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

        yyyy = year;
        mm = monthOfYear;
        dd = dayOfMonth;

        edDate.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(dd).append("/").append(mm + 1).append("/").append(yyyy).append(" "));

    }
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String Name = intent.getStringExtra("name");
            String Mobile = intent.getStringExtra("mobile");
            edUserName.setText(Name);
            edUserPhNumber.setText(Mobile);
            dialog.dismiss();
        }
    };

    private void reset() {
        edUserName.setText("");
        edUserPhNumber.setText("");
        edDescription.setText("");
        edNote.setText("");
        edDate.setText("");
    }



    @Override
    protected void onResume() {
        super.onResume();
        managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int nameIdx = managedCursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME);
        CallLogs callLogs;
        modelClasses.clear();
        while (managedCursor.moveToNext())
        {
            callLogs=new CallLogs();
            phNumber = managedCursor.getString(number);
            name = managedCursor.getString(nameIdx);
            if(name==null)
                callLogs.setName("No Name");
            else
                callLogs.setName(name);
            callLogs.setMobile(phNumber);
            modelClasses.add(callLogs);
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