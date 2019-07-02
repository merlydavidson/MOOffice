package com.microoffice.app.ui.fragments;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.microoffice.app.R;
import com.microoffice.app.ui.activities.DashboardActivity;
import com.microoffice.app.ui.adapters.CustomSpinnerAdapter;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RawRowMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import moffice.meta.com.molibrary.models.dbmodels.Offices;
import moffice.meta.com.molibrary.utility.CommonDataArea;

/**
 * Created by DANIYAL KJ on 06-Dec-17.
 */

public class EmployeeListFragment extends Fragment {

    CustomSpinnerAdapter mOfficeSpinnerAdapter = null;
    RadioButton RBArrrivd,RBDeparted;
    RadioGroup Rbgroup;
    private Spinner spin;
    EditText  empname;
    Button btadd;
    ListView lvemployee;
    LinearLayout lvempview;
    AdView mAdView;
    Calendar date = Calendar.getInstance();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_employee_list, container, false);
//        calendar = Calendar.getInstance(TimeZone.getDefault());
        initViews(layout);
        initOfficeSpinner();

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        btadd.setOnClickListener(mAddOfficeListener);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getContext(),"helloo",Toast.LENGTH_SHORT).show();

        try {
            Dao<moffice.meta.com.molibrary.models.dbmodels.UserList, Integer> EmployeesDao = null;
            List<moffice.meta.com.molibrary.models.dbmodels.UserList> EmployeesArrayList = new ArrayList<>();
            EmployeesDao = ((DashboardActivity) getActivity()).getHelper().getUserListDao();
            EmployeesArrayList = EmployeesDao.queryForAll();
           /* if(EmployeesArrayList.size()>0) {
                AttnListAdapter empListAdapter = new AttnListAdapter(getActivity(), EmployeesArrayList);
                lvemployee.setAdapter(empListAdapter);
                lvempview.setVisibility(View.VISIBLE);
            }
       else
            {
                lvempview.setVisibility(View.GONE);
            }*/
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener mAddOfficeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
           /*  try {
              String emname=empname.getText().toString();
               String off=spin.getSelectedItem().toString();
               Employees emp=new Employees();
               emp.setEmpName(emname);
               emp.setOfficeName(off);
               if(RBArrrivd.isChecked())
               {
                   emp.setInOut("ARRIVED");
               }
               else if(RBDeparted.isChecked())
               {
                   emp.setInOut("DEPARTED");
               }
               emp.setLogYear(date.get(Calendar.YEAR));
               emp.setLogMonth(date.get(Calendar.MONTH)+1);
               emp.setLogDayOfMonth(date.get(Calendar.DAY_OF_MONTH));
               emp.setLogHour(date.get(Calendar.HOUR));
               emp.setLogMinute(date.get(Calendar.MINUTE));
                final Dao<UserList ,Integer> Empdao=CommonDataArea.getHelper(getContext()).getUserListDao();
                Empdao.create(emp);
                CommonDataArea.closeHelper();
                Toast.makeText(getContext(),"Inserted",Toast.LENGTH_SHORT).show();
            }catch(Exception exp){
                exp.printStackTrace();
            }*/
        }
    };
    private void initViews(View layout) {
        spin = layout.findViewById(R.id.spEmpNames);
        empname=layout.findViewById(R.id.etName);
        btadd=layout.findViewById(R.id.btadd);
        RBArrrivd=layout.findViewById(R.id.rbArrived);
        RBDeparted=layout.findViewById(R.id.rbDeparted);
        Rbgroup=layout.findViewById(R.id.rgArrivedDeparted);
        lvemployee=layout.findViewById(R.id.lvempList);
        lvempview=layout.findViewById(R.id.llempView);
        mAdView = layout.findViewById(R.id.adView);
    }

    private void initOfficeSpinner() {
        List<String>office = new ArrayList<>();
//        office.add("M2Comsys");
//        office.add("Axis Bank Atm");
//        office.add("Msquared Software and Services pvt ltd.");
//        office.add("Tata Elxi");
//        office.add("Test House");

        Dao<Offices, Integer> OfficesDao = null;
        try {
            OfficesDao = CommonDataArea.getHelper(getActivity()).getOfficesDao();

            office = OfficesDao.queryRaw("SELECT  OfficeName FROM Offices", new RawRowMapper<String>() {
                @Override
                public String mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
                    return resultColumns[0];
                }
            }).getResults();
            ;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        Resources res = getResources();

        // Create custom adapter object ( see below CustomAdapter.java )
        mOfficeSpinnerAdapter = new CustomSpinnerAdapter(getActivity(),
                R.layout.custom_spinner_row, office, res,
                getString(R.string.spinner_hint_office_name));

        // Set adapter to spinner
        spin.setAdapter(mOfficeSpinnerAdapter);

        // Listener called when spinner item selected
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {

                // Get selected row data to show on screen
                String strOfficeName = ((TextView) v.findViewById(R.id.tvTitle)).getText().toString();

               /* Toast.makeText(
                        getApplicationContext(),strOfficeName, Toast.LENGTH_LONG).show();*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

}
