package com.microoffice.app.ui.fragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.microoffice.app.R;
import com.microoffice.app.ui.adapters.UsersListRecyclerAdapter;

import com.microoffice.app.utils.AppUtils;
import com.microoffice.app.utils.DividerItemDecoration;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import moffice.meta.com.molibrary.core.MOMain;
import moffice.meta.com.molibrary.core.MOUserManager;
import moffice.meta.com.molibrary.database.DatabaseHelperORM;
import moffice.meta.com.molibrary.models.dbmodels.UserList;
import moffice.meta.com.molibrary.utility.CommonDataArea;

import static android.content.Context.MODE_PRIVATE;
import static moffice.meta.com.molibrary.core.MOMain.SHAREDPREF_INSTTYPE;
import static moffice.meta.com.molibrary.core.MOMain.SHAREDPREF_NAME;


public class UsersListFragment extends Fragment {
    private RecyclerView rvUsersList;
    Button button_sendlist;
    SharedPreferences command;
    AdView mAdView;
    // Reference of DatabaseHelperORM class to access its DAOs and other components
    private DatabaseHelperORM databaseHelper = null;

    public static UsersListRecyclerAdapter mUsersListRecAdapter;
    List<UserList> mUsersList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_users_list, container, false);
        rvUsersList = (RecyclerView) rootView.findViewById(R.id.rvUsersList);
        button_sendlist = rootView.findViewById(R.id.button_sendlist);
        mAdView = (AdView)rootView. findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        command = getActivity().getSharedPreferences(SHAREDPREF_NAME, MODE_PRIVATE);
        int a = command.getInt(SHAREDPREF_INSTTYPE,1);
        if(a==1 ){
           button_sendlist.setVisibility(View.VISIBLE);

        }else {
            button_sendlist.setVisibility(View.INVISIBLE);

        }

        button_sendlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (CommonDataArea.getInstType() == MOMain.MO_INSTTYPE_ADMIN) {
                       // MOOfficeLocManager.sendOfficeLocListRequest();
                        MOUserManager.sendUserList(getActivity(),MOMain.MOMESG_TOALL);
                        MOMain.sendProdData(getActivity(),true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        reLoadList();

        AppUtils.hideLoading();

        return rootView;
    }

    public void reLoadList(){
        rvUsersList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvUsersList.addItemDecoration(new DividerItemDecoration(ContextCompat.getDrawable(getActivity(), R.drawable.ic_divider)));
        AppUtils.showLoading(getActivity());
        Dao<UserList, Integer> userListDao = null;
        try {
            userListDao = getHelper().getUserListDao();
            mUsersList = userListDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (mUsersList.size() > 0) {
            mUsersListRecAdapter = new UsersListRecyclerAdapter(getActivity(), mUsersList);
            mUsersListRecAdapter.fragment = this;
            mUsersListRecAdapter.parentView =rvUsersList;
            rvUsersList.setAdapter(mUsersListRecAdapter);

        } else {
//            AppUtils.showAlertOk(getActivity(),
//                    getString(R.string.alert_title_message),
//                    getString(R.string.message_no_users_found));
            Toast.makeText(getContext(),R.string.message_no_users_found,Toast.LENGTH_SHORT).show();
        }
        AppUtils.hideLoading();
    }
    @Override
    public void onDestroy() {

        super.onDestroy();
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }

    }

    // This is how, DatabaseHelperORM can be initialized for future use
    private DatabaseHelperORM getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelperORM.class);
        }
        return databaseHelper;
    }
//    public void popup(int type)
//    {final Dialog dialog = new Dialog(getActivity());
//
//        dialog.setContentView(R.layout.custom_popup);
//        dialog.setTitle("Custom Alert Dialog");
//
//
//        Button btnDesg         = (Button) dialog.findViewById(R.id.desg);
//        Button btnCancel        = (Button) dialog.findViewById(R.id.delete);
//        Button btnDelete        = (Button) dialog.findViewById(R.id.cancel);
//        if(type==0)
//            btnDesg.setText("Set as Employee");
//        else
//            btnDesg.setText("S")
//        dialog.show();
//
//    }
}