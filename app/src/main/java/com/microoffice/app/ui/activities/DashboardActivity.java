package com.microoffice.app.ui.activities;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.microoffice.app.R;
import com.microoffice.app.iap.IabBroadcastReceiver;
import com.microoffice.app.iap.IabHelper;
import com.microoffice.app.iap.IabResult;
import com.microoffice.app.iap.Inventory;
import com.microoffice.app.iap.Purchase;
import com.microoffice.app.ui.fragments.AccountInfoFragment;
import com.microoffice.app.ui.fragments.HomeFragment;
import com.microoffice.app.ui.fragments.InviteUsersFragment;
import com.microoffice.app.ui.fragments.MonthYearPickerDialog;
import com.microoffice.app.ui.fragments.NavDrawerFragment;
import com.microoffice.app.ui.fragments.OfficeListFragment;
import com.microoffice.app.ui.fragments.UsersListFragment;
import com.microoffice.app.ui.listeners.PermissionListener;
import com.microoffice.app.utils.ExportImportDB;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import moffice.meta.com.molibrary.core.MOMain;
import moffice.meta.com.molibrary.core.MOOfficeLocManager;
import moffice.meta.com.molibrary.core.MOUserManager;
import moffice.meta.com.molibrary.utility.CommonDataArea;
import moffice.meta.com.molibrary.utility.LogWriter;

public class DashboardActivity extends BaseActivity
        implements NavDrawerFragment.FragmentDrawerListener, IabBroadcastReceiver.IabBroadcastListener,
        PermissionListener {

    public final int POSITION_USERS = 4;
    public final int POSITION_OFFICES = 5;
    public final int POSITION_ACCOUNT = 6;
    public final int POSITION_INVITE = 7;
    public final int POSITION_EXPORT_DB = 8;
    public final int POSITION_EXPORT_EXCELL = 9;
    public final int POSITION_SYNC_ALL = 10;

    public final int POSITION_IAP = 11;

    @Bind(R.id.tbDashboard)
    Toolbar tbDashboard;
    AdView mAdView;

    private NavDrawerFragment drawerFragment;
    private Menu menu;
    Fragment fragment;
    String title = "";
    boolean isReadyToUpdate = true;
    int whichPosition = 0;
    String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0cocydnRFPLdY/HuFZq/ASsAajMABDBvDNi/ZIByMEq6yFlxlIEqVaDY9o92VMHvNksKn9/AqpzaFn9u6/eQNEjyQYRBMZbuujLtR7a3i4b1IFt7UADqaqaXuz2BXoY2ziMBwILDHrhBuKBZWNp8OtK1qNMavAg+K9qWBoIfXTLTBSdEy1c2eiovPWhcxL9LS0S2+CrMz/IWvfwhr0uuggaYacFFfWSyajC6dDs6h1rYaMAiwFcofzCAMed9IMXjV13tVoW4/oZ2CSF+LqxusvTIwH4d82tNd/ABTmPibFS8wFK7GkJ2SeXzyG/ExOn4WOEDD6ZLGFOr9jsiBb8kvQIDAQAB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ButterKnife.bind(DashboardActivity.this);
        MOMain.init(getApplicationContext());
        if (tbDashboard != null) {
            setSupportActionBar(tbDashboard);
        }

        drawerFragment = (NavDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout), tbDashboard);
        drawerFragment.setDrawerListener(this);
//        mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
        // display the first navigation drawer view on app launch
        try {
            displayView(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        verifyExcellLic();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_search, menu);

        switch (whichPosition) {
            case POSITION_INVITE:
            case POSITION_USERS:
                showOption(R.id.action_search);
                break;
            default:
                hideOption(R.id.action_search);
                break;
        }

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                try {
                    performSearch(newText);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
//            case R.id.action_search:
//                String url = null;
//                if (url == null)
//                    return true;
//                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//                startActivity(intent);
//                return true;
        }
        return false;
    }

    // We're being destroyed. It's important to dispose of the helper here!
    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.disposeWhenFinished();
            mHelper = null;
        }
    }

    @Override
    public void onSuccessListener() {
        isReadyToUpdate = true;
        if (!InviteUsersFragment.running) {
            fragment = new InviteUsersFragment();
            title = getString(R.string.nav_item_invite);
            loadFragment();
        }
    }

    @Override
    public void onFailureListener() {
//        AppUtils.showAlertOk(DashboardActivity.this,
//                "Alert",
//                getString(R.string.error_contacts));
        Toast.makeText(getApplicationContext(), R.string.error_contacts, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        try {
            displayView(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayView(int position) throws Exception {

        whichPosition = position;

        fragment = null;
        title = getString(R.string.app_name);
        isReadyToUpdate = true;
        switch (position) {
            case POSITION_OFFICES:
                fragment = new OfficeListFragment();
                title = getString(R.string.nav_item_offices);

                break;

            /*case 1:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;

            case 2:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;*/
           /* case POSITION_EMPLOYEES:
                fragment=new EmployeeListFragment();
                title=getString(R.string.nav_item_employeelist);
                break;*/
            case POSITION_INVITE:
//                if (CommonDataArea.getInstType() == MOMain.MO_INSTTYPE_ASSOS) {
//                    fragment = new SorryFragment();
//                    title = "Not Available";
//                } else
                    if (CommonDataArea.getInstType() == MOMain.MO_INSTTYPE_ADMIN) {
                    if (MOMain.getUserLicense() > 0) {
                        checkPermission(DashboardActivity.this);
                        fragment = new InviteUsersFragment();
                        title = getString(R.string.nav_item_invite);
                    } else {
                        iapBuyUserLic(1);
                    }
                }
                // isReadyToUpdate = false;
                break;

            case POSITION_USERS:
                fragment = new UsersListFragment();
                title = getString(R.string.nav_item_user);
                break;

            case POSITION_ACCOUNT:
                fragment = new AccountInfoFragment();
                title = getString(R.string.nav_item_accout);
                break;
          /*  case POSITION_USERS_UPD:
                MOUserManager.sendUserList(this,CommonDataArea.getUserUUID());
                break;*/
            case POSITION_EXPORT_DB:
                if (CommonDataArea.getInstType() == MOMain.MO_INSTTYPE_ADMIN) {
                    // MOMain.exportDB(DashboardActivity.this);
                    if(ExportImportDB.exportDB(this)){
                        Toast.makeText(this, "Database export successful", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Database export FAILED", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "You are not having the privilege to export", Toast.LENGTH_LONG).show();
                }
                break;
            case POSITION_EXPORT_EXCELL:
                if (CommonDataArea.getInstType() == MOMain.MO_INSTTYPE_ADMIN) {
                    Calendar calendar = Calendar.getInstance();
                    MonthYearPickerDialog pd = MonthYearPickerDialog.newInstance(calendar.get(Calendar.MONTH) + 1,
                            calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR));
                    pd.setCancelable(true);
                    pd.setListener(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            if (MOMain.getExcelLicense())
                                MOMain.exportExcel(DashboardActivity.this, i1, i);
                            else iapExcelLicense();
                        }
                    });
                    pd.show(getFragmentManager(), "MonthYearPickerDialog");

                } else {
                    Toast.makeText(this, "You are not having the privilege to export", Toast.LENGTH_LONG).show();
                }

                break;
            case POSITION_SYNC_ALL:
                if (CommonDataArea.getInstType() == MOMain.MO_INSTTYPE_ADMIN) {
                    MOUserManager.sendUserList(this, MOMain.MOMESG_TOALL);
                    MOMain.sendProdData(this, true);
                    MOOfficeLocManager.sendOfficeLocations(this, MOMain.MOMESG_TOALL);
                    // Toast.mcreateDialogWithoutDateFieldakeText(this,"Start Sending...",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "You are not having the privilege to export", Toast.LENGTH_LONG).show();
                }
                break;
           /* case POSITION_IAP:
                iapUserLicense();
                break;*/


            default:
                fragment = new HomeFragment();
                title = getString(R.string.app_name);
                break;

        }

        /*if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }*/
        loadFragment();
    }

    private void loadFragment() {
        if (fragment != null && isReadyToUpdate) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(title);
        }
    }

    /**
     * Implements a method to Quick search bar
     * in tool bar.
     */
    public void performSearch(final String queryText) throws Exception {

        switch (whichPosition) {
            case POSITION_INVITE:
                if (CommonDataArea.getInstType() != MOMain.MO_INSTTYPE_ASSOS) {
                if (InviteUsersFragment.mContactsRAdapter != null) {

                    InviteUsersFragment.mContactsRAdapter.getFilter().filter(queryText);
                    //MembersFragment.mAdapter.setHeaderViewVisible(TextUtils.isEmpty(queryText));
                    // InviteUsersFragment.mContactsRAdapter.notifyDataSetChanged();
                }}
                break;

            case POSITION_USERS:
                if (UsersListFragment.mUsersListRecAdapter != null) {

                    UsersListFragment.mUsersListRecAdapter.getFilter().filter(queryText);
                    //MembersFragment.mAdapter.setHeaderViewVisible(TextUtils.isEmpty(queryText));
                    // InviteUsersFragment.mContactsRAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private void hideOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(false);
    }

    private void showOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(true);
    }

    public void onBackPressed() {
        if (fragment != null) {
            if (!title.contains(getString(R.string.app_name))) {
                fragment = new HomeFragment();
                title = getString(R.string.app_name);
                loadFragment();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    @Override
    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        Log.d(TAG, "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain("Error querying inventory. Another async operation in progress.");
        }
    }

    private DatePickerDialog createDialogWithoutDateField() {
        DatePickerDialog dpd = new DatePickerDialog(this, null, 2014, 1, 24);
        try {
            java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            ((ViewGroup) dpd.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields) {
                        Log.i("test", datePickerField.getName());
                        if ("mDaySpinner".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
        return dpd;
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }

    IabHelper mHelper;
    static final String TAG = "IAP MOffice";
    IabBroadcastReceiver mBroadcastReceiver;

    private void iapInit() {
        /* base64EncodedPublicKey should be YOUR APPLICATION'S PUBLIC KEY
         * (that you got from the Google Play developer console). This is not your
         * developer public key, it's the *app-specific* public key.
         *
         * Instead of just storing the entire literal string here embedded in the
         * program,  construct the key at runtime from pieces or
         * use bit manipulation (for example, XOR with some other string) to hide
         * the actual key.  The key itself is not secret information, but we don't
         * want to make it easy for an attacker to replace the public key with one
         * of their own and then fake messages from the server.
         */

        // Some sanity checks to see if the developer (that's you!) really followed the
        // instructions to run this sample (don't put these checks on your app!)
        if (base64EncodedPublicKey.contains("CONSTRUCT_YOUR")) {
            throw new RuntimeException("Please put your app's public key in MainActivity.java. See README.");
        }
        if (getPackageName().startsWith("com.example")) {
            throw new RuntimeException("Please change the sample's package name! See README.");
        }
        // Create the helper, passing it our context and the public key to verify signatures with
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.

    }

    private void verifyExcellLic() {
        iapInit();
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // Important: Dynamically register for broadcast messages about updated pur chases.
                // We register the receiver here instead of as a <receiver> in the Manifest
                // because we always call getPurchases() at startup, so therefore we can ignore
                // any broadcasts sent while the app isn't running.
                // Note: registering this listener in an Activity is a bad idea, but is done here
                // because this is a SAMPLE. Regardless, the receiver must be registered after
                // IabHelper is setup, but before first call to getPurchases().
                mBroadcastReceiver = new IabBroadcastReceiver(DashboardActivity.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain("Error querying inventory. Another async operation in progress.");
                }

            }
        });

    }

    private void iapBuyUserLic(final int numLic) {
        try {
            iapInit();
            Log.d(TAG, "Starting setup.");
            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                public void onIabSetupFinished(IabResult result) {
                    Log.d(TAG, "Setup finished.");

                    if (!result.isSuccess()) {
                        // Oh noes, there was a problem.
                        complain("Problem setting up in-app billing: " + result);
                        LogWriter.writeLog("IAP Init", "IAP init failed");
                        return;
                    }

                    // Have we been disposed of in the meantime? If so, quit.
                    if (mHelper == null) return;

                    // Important: Dynamically register for broadcast messages about updated purchases.
                    // We register the receiver here instead of as a <receiver> in the Manifest
                    // because we always call getPurchases() at startup, so therefore we can ignore
                    // any broadcasts sent while the app isn't running.
                    // Note: registering this listener in an Activity is a bad idea, but is done here
                    // because this is a SAMPLE. Regardless, the receiver must be registered after
                    // IabHelper is setup, but before first call to getPurchases().
                    mBroadcastReceiver = new IabBroadcastReceiver(DashboardActivity.this);
                    IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                    registerReceiver(mBroadcastReceiver, broadcastFilter);

                    try {
                        //android.test.purchased
                        if (numLic == 1) {
                            LogWriter.writeLog("IAP", "Start purchase user pack 1");
                            mHelper.launchPurchaseFlow(DashboardActivity.this, "microoffice.userpack.1", 10002,
                                    mPurchaseFinishedListener, "bmjo");
                        } else {
                            mHelper.launchPurchaseFlow(DashboardActivity.this, "microoffice.userpack.5", 10003,
                                    mPurchaseFinishedListener, "bmjo");
                        }

                    } catch (IabHelper.IabAsyncInProgressException exp) {
                        LogWriter.writeLogException("IAP Error", exp);
                    }
                }
            });
        } catch (Exception exp) {

        }
    }

    private void iapExcelLicense() {
        try {
            iapInit();
            Log.d(TAG, "Starting setup.");
            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                public void onIabSetupFinished(IabResult result) {
                    Log.d(TAG, "Setup finished.");

                    if (!result.isSuccess()) {
                        // Oh noes, there was a problem.
                        complain("Problem setting up in-app billing: " + result);
                        return;
                    }

                    // Have we been disposed of in the meantime? If so, quit.
                    if (mHelper == null) return;

                    // Important: Dynamically register for broadcast messages about updated purchases.
                    // We register the receiver here instead of as a <receiver> in the Manifest
                    // because we always call getPurchases() at startup, so therefore we can ignore
                    // any broadcasts sent while the app isn't running.
                    // Note: registering this listener in an Activity is a bad idea, but is done here
                    // because this is a SAMPLE. Regardless, the receiver must be registered after
                    // IabHelper is setup, but before first call to getPurchases().
                    mBroadcastReceiver = new IabBroadcastReceiver(DashboardActivity.this);
                    IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                    registerReceiver(mBroadcastReceiver, broadcastFilter);

                    // IAB is fully set up. Now, let's get an inventory of stuff we own.
                    Log.d(TAG, "Setup successful. Querying inventory.");
               /*     try {
                        mHelper.queryInventoryAsync(mGotInventoryListener);
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        complain("Error querying inventory. Another async operation in progress.");
                    }*/
                    try {
                        //android.test.purchased
                        mHelper.launchPurchaseFlow(DashboardActivity.this, "microoffice.excellexport", 10001,
                                mPurchaseFinishedListener, "bmjo");
                      /*  mHelper.launchPurchaseFlow(DashboardActivity.this, "android.test.canceled", 10001,
                                mPurchaseFinishedListener, "bmjo");*/
                    } catch (IabHelper.IabAsyncInProgressException exp) {
                        LogWriter.writeLogException("IAP", exp);
                    }
                }
            });
        } catch (Exception exp) {

        }
    }

    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }
            Log.d(TAG, "Query inventory was successful.");

            Purchase exelLic = inventory.getPurchase("microoffice.excellexport");
            boolean excelLicence = (exelLic != null && verifyDeveloperPayload(exelLic));
            if (excelLicence)
                MOMain.setExcelLicense(excelLicence, "bmjobmjo");

            Purchase userLicense = inventory.getPurchase("microoffice.userpack.1");
            boolean userLicence = (userLicense != null && verifyDeveloperPayload(exelLic));
            if (userLicence) {
                MOMain.setUserLicense(1, "bmjobmjo");
                try {
                    mHelper.consumeAsync(inventory.getPurchase("microoffice.userpack.1"), mConsumeFinishedListener);
                    LogWriter.writeLog("IAP Invetory Lis", "User Lic Installed and cosumed");
                } catch (IabHelper.IabAsyncInProgressException exp) {
                    LogWriter.writeLogException("IAP", exp);
                }
            }
        }
    };

    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        if (payload.contains("bmjo"))
            return true;
        else return false;
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
                LogWriter.writeLog("IAP Purchase Finish", "Purchase error");
                return;
            }

            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                return;
            }

            Log.d(TAG, "Purchase successful.");
            if (purchase.getSku().equals("microoffice.excellexport")) {
                // bought license save it. Life time valid until reinstall
                MOMain.setExcelLicense(true, "bmjobmjo");
                LogWriter.writeLog("IAP", "Installed excel lic");
                alert("Excel license installed");
            }

            boolean userLicence = false;
            if (purchase.getSku().equals("microoffice.userpack.1")) userLicence = true;
            if (userLicence) {
                MOMain.setUserLicense(1, "bmjobmjo");
                LogWriter.writeLog("IAP", "Installed user  lic");
                try {
                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                    LogWriter.writeLog("IAP", "Installed user lic and consumed");
                    alert("User license installed");
                } catch (IabHelper.IabAsyncInProgressException exp) {
                    LogWriter.writeLogException("IAP", exp);
                }
            }

        }
    };

    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.d(TAG, "Consumption successful. Provisioning.");
                if (purchase.getSku().contains("microoffice.userpack.1")) {
                    LogWriter.writeLog("IAP", "microoffice.userpack.1 consumed");
                }
            } else {
                complain("Error while consuming: " + result);
                LogWriter.writeLog("IAP", "microoffice.userpack.1 consumption error");
            }

            Log.d(TAG, "End consumption flow.");
        }
    };

    BroadcastReceiver userLicRequest = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };
}