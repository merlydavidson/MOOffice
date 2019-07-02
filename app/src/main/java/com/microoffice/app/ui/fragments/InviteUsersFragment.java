package com.microoffice.app.ui.fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.microoffice.app.R;
import com.microoffice.app.ui.adapters.ContactsRecyclerAdapter;
import com.microoffice.app.utils.AppUtils;
import com.microoffice.app.utils.DividerItemDecoration;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import moffice.meta.com.molibrary.core.MOMain;
import moffice.meta.com.molibrary.database.DatabaseHelperORM;
import moffice.meta.com.molibrary.models.Contacts;
import moffice.meta.com.molibrary.models.dbmodels.UserList;
import moffice.meta.com.molibrary.utility.LogWriter;


public class InviteUsersFragment extends Fragment {
    private RecyclerView rvContacts;
    private Handler updateBarHandler;
    Cursor cursor;
    int counter;
    AdView mAdView;

    public static boolean running = false;
    // Reference of DatabaseHelperORM class to access its DAOs and other components
    private DatabaseHelperORM databaseHelper = null;

    public static ContactsRecyclerAdapter mContactsRAdapter;
    List<Contacts> contactList = new ArrayList<Contacts>();
    List<String> mInvitedContacts = new ArrayList<>();
    List<UserList> users = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        running = true;
        View rootView = inflater.inflate(R.layout.fragment_invite_users, container, false);

        rvContacts = rootView.findViewById(R.id.rvContacts);
        mAdView = (AdView)rootView. findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        // mAlreadyInvitedList = MODatabaseAdapter.getAllUserList(getActivity());
        rvContacts.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvContacts.addItemDecoration(new DividerItemDecoration
                (ContextCompat.getDrawable(getActivity(), R.drawable.ic_divider)));


        Dao<UserList, Integer> userListDao = null;
        try {
            userListDao = getHelper().getUserListDao();
            List<UserList> userLists = userListDao.queryForAll();
            for (int iCount = 0; iCount < userLists.size(); iCount++) {
                if (userLists.get(iCount).getStatus() == MOMain.MO_DBUSERLIST_INVITED) {
                    mInvitedContacts.add(userLists.get(iCount).getMobile());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        mContactsRAdapter = new ContactsRecyclerAdapter(getActivity(), contactList, mInvitedContacts);
        rvContacts.setAdapter(mContactsRAdapter);

        updateBarHandler = new Handler();

        AppUtils.showLoading(getActivity());

        new Thread(new Runnable() {
            @Override
            public void run() {
                getContacts();
            }
        }).start();

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        running =false;
		/*
         * You'll need this in your class to release the helper when done.
		 */
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

    public void getContacts() {
        try {
            contactList.clear();
            String phoneNumber = null;
            String email = null;
            Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
            String _ID = ContactsContract.Contacts._ID;
            String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
            String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
            Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
            String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
            Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
            String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
            String DATA = ContactsContract.CommonDataKinds.Email.DATA;
            StringBuffer output;
            ContentResolver contentResolver = getActivity().getContentResolver();
            cursor = contentResolver.query(CONTENT_URI, null, null, null, null);
            // Iterate every contact in the phone
            if (cursor.getCount() > 0) {
                counter = 0;
                while (cursor.moveToNext()) {
                    output = new StringBuffer();
                    // Update the progress message
                    updateBarHandler.post(new Runnable() {
                        public void run() {
                            //progressContacts.setVisibility(View.VISIBLE);
                        }
                    });
                    String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                    String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                    int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));
                    Contacts contact = null;
                    if (hasPhoneNumber > 0) {
                        output.append("\n First Name:" + name);
                        //This is to read multiple phone numbers associated with the same contact
                        Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);
                        while (phoneCursor.moveToNext()) {
                            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                            output.append("\n Phone number:" + phoneNumber);
                        }
                        phoneCursor.close();
                        // Read every email id associated with the contact
                        Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?", new String[]{contact_id}, null);
                        while (emailCursor.moveToNext()) {
                            email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                            output.append("\n Email:" + email);
                        }
                        emailCursor.close();

                        contact = new Contacts(name, email, phoneNumber);
                    }
                    // Add the contact to the ArrayList
                    if (contact != null) {
                        contactList.add(contact);
                    }
                }
                // ListView has to be updated using a ui thread
                Activity activity = getActivity();
                if(activity!=null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                    /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item_contact, R.id.text1, contactList);
                    mListView.setAdapter(adapter);*/
                            Collections.sort(contactList, new Comparator<Contacts>() {
                                @Override
                                public int compare(Contacts lhs, Contacts rhs) {
                                    return lhs.getStrName().compareTo(rhs.getStrName());
                                }
                            });
                            mContactsRAdapter.notifyDataSetChanged();
                        }
                    });
                }
                // Dismiss the progressbar after 500 millisecondds
                updateBarHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Activity activity = getActivity();
                        if(activity!=null) {
                            activity.runOnUiThread(new Runnable() {
                                                       @Override
                                                       public void run() {
                                                           AppUtils.hideLoading();
                                                       }
                                                   });
                        }
//
                        }
                }, 100);
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AppUtils.hideLoading();
                        Toast.makeText(getActivity(), "No contact found...", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }catch (Exception exp){
            LogWriter.writeLogException("Invite User,Contact Filling",exp);
            updateBarHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AppUtils.hideLoading();
                }
            }, 100);
        }
    }


   /* void filter(String text){
        List<Contacts> temp = new ArrayList();
        for(DataHolder d: displayedList){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getEnglish().contains(text)){
                temp.add(d);
            }
        }
        //update recyclerview
        mContactsRAdapter.updateList(temp);
    }*/
}