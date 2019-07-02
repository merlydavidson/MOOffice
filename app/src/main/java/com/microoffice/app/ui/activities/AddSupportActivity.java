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
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import moffice.meta.com.molibrary.core.MOMain;
import moffice.meta.com.molibrary.models.dbmodels.Products;
import moffice.meta.com.molibrary.models.dbmodels.SupportDetails;
import moffice.meta.com.molibrary.models.dbmodels.UserList;
import moffice.meta.com.molibrary.utility.CommonDataArea;

public class AddSupportActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    Toolbar tbSupport;
    EditText edUserName;
    EditText edUserPhNumber;
    Spinner edPoduct,edAssignedTo;
    EditText edDescription;
    EditText edDate;
    EditText edNote;
    Button supportBtn;
    String product;
    String assignedTo;
    String phNumber;
    String name;
    Cursor managedCursor;
    CustomSpinnerAdapter mProductSpinner = null;
    CustomSpinnerAdapter mAssignedToSpinner = null;
    ImageButton contactButton,addProduct;
    int mYear, mMonth, mDay;
    java.util.Calendar calendar;
    String myFormat = "dd/MM/yyyy"; //In which you need put here
    DateFormat sdf = new SimpleDateFormat(myFormat);
    private AdView mAdView;
    int dd, mm, yyyy;
    private AlertDialog dialog;
    public static CallLogRecyclerAdapter mCallLogListRecAdapter;
    ArrayList<CallLogs> modelClasses=new ArrayList<>();
    private DatabaseHelperORM databaseHelperORM = null;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_support);
        mAdView=(AdView)findViewById(R.id.adView);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        calendar = Calendar.getInstance(TimeZone.getDefault());
        tbSupport=(Toolbar)findViewById(R.id.tbAddSupport);
        edUserName = (EditText) findViewById(R.id.etUserName);
        edAssignedTo = (Spinner) findViewById(R.id.etAssignedTo);
        edUserPhNumber = (EditText) findViewById(R.id.etMobileNo);
        edPoduct = (Spinner) findViewById(R.id.etProduct);
        edDescription = (EditText) findViewById(R.id.etDescription);
        edNote = (EditText) findViewById(R.id.etNote);
        supportBtn = (Button) findViewById(R.id.supportbtn);
        edDate = (EditText) findViewById(R.id.etDate);
        contactButton=(ImageButton)findViewById(R.id.contactButton);
        addProduct=(ImageButton)findViewById(R.id.addButton);

        if (tbSupport != null) {
            setSupportActionBar(tbSupport);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Support");
        }
        edDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                showDatePicker();
//                Calendar mcurrentDate = Calendar.getInstance();
//                mYear = mcurrentDate.get(Calendar.YEAR);
//                mMonth = mcurrentDate.get(Calendar.MONTH);
//                mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
//                DatePickerDialog pickerDialog = new DatePickerDialog(AddEnquiryActivity.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//                        edDate.setText(i2 + "-" + (i1+1) + "-" + i);
//                    }
//                }, mYear, mMonth, mDay);
//                pickerDialog.setTitle("Select Date");
//                pickerDialog.show();
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

        mProductSpinner=  new CustomSpinnerAdapter(AddSupportActivity.this,
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
                try {
                    if (CommonDataArea.getInstType() != MOMain.MO_INSTTYPE_ADMIN) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddSupportActivity.this);
                        builder.setMessage("You are not permitted to add products. Please contact Admin");
                        builder.setPositiveButton("OK",null);
                        builder.create().show();
                        return;
                    }
                }catch(Exception exp){

                }
                AlertDialog.Builder builder = new AlertDialog.Builder(AddSupportActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.alert_add_product,null);
                builder.setView(dialogView);
                final EditText edtProduct = (EditText) dialogView.findViewById(R.id.prdtName);
                Button butOK = (Button) dialogView.findViewById(R.id.ok);
                Button butCANCEL = (Button) dialogView.findViewById(R.id.cancel);
                butOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String prodName = edtProduct.getText().toString();
                        dialog.dismiss();

                        try {
                            Dao<Products, Integer> productDao = CommonDataArea.getHelper(getApplicationContext()).getProductDao();
                            QueryBuilder<Products, Integer> queryBuilder = productDao.queryBuilder();
                            queryBuilder.where().eq("ProductName", prodName);
                            List<Products> prodList = queryBuilder.query();
                            if (prodList.size() == 0) {
                                Products products = new Products();
                                products.setProductName(prodName);
                                UUID uuidProd = UUID.randomUUID();
                                String recUUIDProd = uuidProd.toString();
                                products.setRecUUID(recUUIDProd);
                                products.setSendStatus(0);
                                productDao.create(products);
                                finalProductList.add(edtProduct.getText().toString());
                                mProductSpinner.notifyDataSetChanged();
                            }else{
                                Toast.makeText(AddSupportActivity.this,"Product already in the list",Toast.LENGTH_LONG).show();
                            }
                        }catch(Exception exp){

                        }

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
        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddSupportActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.fragment_recentcalllogs,null);
                builder.setView(dialogView);
                final RecyclerView rvCallLogList=(RecyclerView)dialogView.findViewById(R.id.rvCallLogList);
                rvCallLogList.setLayoutManager(new LinearLayoutManager(AddSupportActivity.this));
                mCallLogListRecAdapter=new CallLogRecyclerAdapter(AddSupportActivity.this,modelClasses);
                rvCallLogList.setAdapter(mCallLogListRecAdapter);
                mCallLogListRecAdapter.notifyDataSetChanged();
                dialog = builder.create();
                dialog.show();
                LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mMessageReceiver,
                        new IntentFilter("log"));
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

        mAssignedToSpinner=  new CustomSpinnerAdapter(AddSupportActivity.this,
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

        supportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
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
                } else {
                    UUID uuid = UUID.randomUUID();
                    String recUUID = uuid.toString();
                    Products products = new Products();
                    products.setProductName(product);
                    SupportDetails supportDetails = new SupportDetails();
                    supportDetails.setUserUUID(CommonDataArea.getUserUUID());
                    supportDetails.setRecUUID(recUUID);
                    supportDetails.setName(CommonDataArea.getCommonSettings().getName().toString());
                    supportDetails.setCustName(edUserName.getText().toString());
                    supportDetails.setPhoneNumber(edUserPhNumber.getText().toString());
                    supportDetails.setProduct(product);
                    supportDetails.setDescription(edDescription.getText().toString());
                    supportDetails.setDate(edDate.getText().toString());
                    if (edNote.getText().toString().equals(""))
                        supportDetails.setNote("");
                    else
                        supportDetails.setNote(edNote.getText().toString());
                    supportDetails.setAssignedTo(assignedTo);
                    supportDetails.setStatus("Open");
                    supportDetails.setLogYear(yyyy);
                    supportDetails.setLogMonth(mm + 1);
                    supportDetails.setLogDayOfMonth(dd);
                    supportDetails.setSendStatus(0);
                    try {
                        Dao<Products,Integer> productDao=CommonDataArea.getHelper(getApplicationContext()).getProductDao();
                        QueryBuilder<Products, Integer> queryBuilder = productDao.queryBuilder();
                        queryBuilder.where().eq("ProductName", products.getProductName());
                        List<Products> prodList = queryBuilder.query();
                        if(prodList.size()==0){
                            UUID uuidProd = UUID.randomUUID();
                            String recUUIDProd = uuidProd.toString();
                            products.setRecUUID(recUUIDProd);
                            products.setSendStatus(0);
                            productDao.create(products);
                        }

                        Dao<SupportDetails, Integer> supportDetailses = CommonDataArea.getHelper(getApplicationContext()).getSupportDao();
                        supportDetailses.create(supportDetails);
                        Toast.makeText(AddSupportActivity.this, "Record added successfully", Toast.LENGTH_LONG).show();
//                        showDialog();
                        CommonDataArea.closeHelper();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    edUserName.setText("");
                    edUserPhNumber.setText("");
                    edDescription.setText("");
                    edNote.setText("");
                }
                }catch(Exception exp){

                }
            }
        });
    }
    private void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(AddSupportActivity.this, this,
                calendar.get(java.util.Calendar.YEAR), calendar.get(java.util.Calendar.MONTH),
                calendar.get(java.util.Calendar.DAY_OF_MONTH));
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