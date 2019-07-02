package com.microoffice.app.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.microoffice.app.BuildConfig;
import com.microoffice.app.R;
import com.microoffice.app.ui.activities.AddEnquiryActivity;
import com.microoffice.app.ui.activities.AddSupportActivity;
import com.microoffice.app.ui.activities.DashboardActivity;
import com.microoffice.app.ui.activities.MarkAttendanceActivity;
import com.microoffice.app.ui.activities.MarkVisitActivity;
import com.microoffice.app.utils.AppUtils;


public class HomeFragment extends Fragment {

    LinearLayout llMarkAttendance,llAddEnquiry;
    LinearLayout llMarkVisit,llAddSupport;
    Button bttn;
   Button btTest;
    AdView mAdView;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        llMarkAttendance = rootView.findViewById(R.id.llMarkAttendance);
        llMarkAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.callToIntent(getActivity(),
                        MarkAttendanceActivity.class,
                        false);
            }
        });
        llMarkVisit= rootView.findViewById(R.id.llMarkVisit);
        llMarkVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppUtils.callToIntent(getActivity(),
                        MarkVisitActivity.class,
                        false);
            }
        });

        mAdView = (AdView)rootView. findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        llAddEnquiry = (LinearLayout) rootView.findViewById(R.id.ll_addEnquiry);
        llAddEnquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BuildConfig.FLAVOR == "free") {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setMessage("This feature is available in paid version only");
                    alertDialogBuilder.setPositiveButton("OK",null);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    return;

                }
                AppUtils.callToIntent(getActivity(), AddEnquiryActivity.class, false);
            }
        });

        llAddSupport= (LinearLayout) rootView.findViewById(R.id.ll_addSupport);
        llAddSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (BuildConfig.FLAVOR == "free") {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setMessage("This feature is available in paid version only");
                    alertDialogBuilder.setPositiveButton("OK",null);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    return;

                }
                AppUtils.callToIntent(getActivity(), AddSupportActivity.class, false);
            }
        });

/*btTest = rootView.findViewById(R.id.btTest);
        btTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.callToIntent(getActivity(), MapsVisitsActivity.class, false);
            }
        });*/

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}