package com.microoffice.app.ui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.microoffice.app.R;
import com.microoffice.app.ui.activities.AddOfficeLocationActivity;
import com.microoffice.app.ui.activities.DashboardActivity;
import com.microoffice.app.ui.adapters.OfficeListAdapter;
import com.microoffice.app.utils.AppUtils;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import moffice.meta.com.molibrary.core.MOMain;
import moffice.meta.com.molibrary.core.MOOfficeLocManager;
import moffice.meta.com.molibrary.models.dbmodels.Offices;
import moffice.meta.com.molibrary.utility.CommonDataArea;

import static android.content.Context.MODE_PRIVATE;
import static moffice.meta.com.molibrary.core.MOMain.SHAREDPREF_INSTTYPE;
import static moffice.meta.com.molibrary.core.MOMain.SHAREDPREF_NAME;


/**
 * Created by com.moffice.com.microoffice.app on 02-08-2017.
 */
public class OfficeListFragment extends Fragment {

    private ImageView ivAddOffice;
    private ImageView btnSendLocation;
    private ListView lvOfficeList;
    private LinearLayout llOfficeView;
    OfficeListAdapter officeListAdapter = null;
    Dao<Offices, Integer> OfficesDao = null;
    List<Offices> officesArrayList = new ArrayList<>();
    AdView mAdView;
    String[] arr_office_names;
    String[] arr_office_lat;
    String[] arr_office_lon;
    SharedPreferences command;

    private View.OnClickListener mAddOfficeListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        try {
            if (CommonDataArea.getInstType() == MOMain.MO_INSTTYPE_ASSOS) {
                MOOfficeLocManager.sendOfficeLocListRequest();
            } else {
                AppUtils.callToIntent(getActivity(), AddOfficeLocationActivity.class, false);
            }
        }catch(Exception exp){

        }
        }
    };

    public OfficeListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arr_office_names = getActivity().getResources().getStringArray(R.array.arr_office_names);
        arr_office_lat = getActivity().getResources().getStringArray(R.array.arr_office_lat);
        arr_office_lon = getActivity().getResources().getStringArray(R.array.arr_office_lon);
        command     = getActivity().getSharedPreferences(SHAREDPREF_NAME, MODE_PRIVATE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_office_list, container, false);
        initViews(layout);
       // ivAddOffice.setOnClickListener(mAddOfficeListener);


        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        int a = command.getInt(SHAREDPREF_INSTTYPE,1);
        if(a==1){
            ivAddOffice.setVisibility(View.VISIBLE);
            btnSendLocation.setVisibility(View.VISIBLE);
            ivAddOffice.setImageDrawable(getResources().getDrawable(R.mipmap.ic_add_office, getActivity().getTheme()));
        }else {
           // ivAddOffice.setVisibility(View.INVISIBLE);
            btnSendLocation.setVisibility(View.INVISIBLE);
          //  ivAddOffice.setImageDrawable(getResources().getDrawable(R.mipmap.icons_downloading, getActivity().getTheme()));
        }
        ivAddOffice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                        if (CommonDataArea.getInstType() == MOMain.MO_INSTTYPE_ASSOS) {
                            MOOfficeLocManager.sendOfficeLocListRequest();
                        } else {
                            AppUtils.callToIntent(getActivity(), AddOfficeLocationActivity.class, false);
                        }
                }catch(Exception exp){

                }
            }
        });

        btnSendLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getContext(),"Sending office list to users",Toast.LENGTH_LONG).show();
                MOOfficeLocManager.sendOfficeLocations(getActivity(),"TO ALL");
                Toast.makeText(getContext(),"Completed sending office List",Toast.LENGTH_LONG).show();

            }
        });
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            OfficesDao = ((DashboardActivity) getActivity()).getHelper().getOfficesDao();
            officesArrayList = OfficesDao.queryForAll();
            if (officesArrayList.size() > 0) {
                officeListAdapter = new OfficeListAdapter(getActivity(), officesArrayList);
                lvOfficeList.setAdapter(officeListAdapter);
                llOfficeView.setVisibility(View.VISIBLE);
            } else {
                llOfficeView.setVisibility(View.GONE);
//                AppUtils.showAlertOk(getActivity(),
//                        getString(R.string.alert_title_message),
//                        getString(R.string.message_no_office_found));
                Toast.makeText(getContext(),R.string.message_no_office_found,Toast.LENGTH_SHORT).show();
            }

            lvOfficeList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    removeItemFromList(position);
                    return false;
                }
            });


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removeItemFromList(int position) {
        final int deletePosition = position;
        AlertDialog.Builder aletDilog = new AlertDialog.Builder(getActivity());
        aletDilog.setMessage("Do you want delete this item ?");
        aletDilog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Offices office =(Offices) officesArrayList.get(deletePosition);
                if(office!=null){
                    MOOfficeLocManager.deleteOffice(office.getID());
                    officesArrayList.remove(deletePosition);
                    officeListAdapter.notifyDataSetChanged();
                    officeListAdapter.notifyDataSetInvalidated();
                }else{
                    Toast.makeText(getActivity(),"Office Not found, Failed to delete",Toast.LENGTH_LONG).show();
                }

            }
        });

        aletDilog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });

        aletDilog.show();

    }

    private void initViews(View layout) {
        ivAddOffice = layout.findViewById(R.id.ivAddOffice);
        lvOfficeList = layout.findViewById(R.id.lvOfficeList);
        llOfficeView = layout.findViewById(R.id.llOfficeView);
        btnSendLocation = layout.findViewById(R.id.ivsendOffice);
        mAdView = layout. findViewById(R.id.adView);
    }

}
