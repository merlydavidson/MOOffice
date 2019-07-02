package com.microoffice.app.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.microoffice.app.R;
import com.microoffice.app.ui.activities.AttendanceListActivity;
import com.microoffice.app.ui.activities.EnquiryListActivity;
import com.microoffice.app.ui.activities.LeaveListActivity;
import com.microoffice.app.ui.activities.LeaveManagementActivity;
import com.microoffice.app.ui.activities.LeaveSummaryActivity;
import com.microoffice.app.ui.activities.SupportListActivity;
import com.microoffice.app.ui.activities.VisitListActivity;
import com.microoffice.app.ui.adapters.NavigationDrawerAdapter;

import java.util.ArrayList;
import java.util.List;

import moffice.meta.com.molibrary.core.MOMain;
import moffice.meta.com.molibrary.models.NavDrawerItem;
import moffice.meta.com.molibrary.models.dbmodels.LeaveSummary;
import moffice.meta.com.molibrary.utility.CommonDataArea;


/**
 * Created by com.moffice.com.microoffice.app on 02-08-2017.
 */
public class NavDrawerFragment extends Fragment {

    private static String TAG = NavDrawerFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    private static String[] titles = null;
    private FragmentDrawerListener drawerListener;

    public NavDrawerFragment() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<NavDrawerItem> getData() throws Exception {
        List<NavDrawerItem> data = new ArrayList<>();
        // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();

                if(titles[i].equalsIgnoreCase("Invite Users")||titles[i].equalsIgnoreCase("Database Export")||titles[i].equalsIgnoreCase("Excel Export")||titles[i].equalsIgnoreCase("Sync Prod/User"))
                {
                    if (CommonDataArea.getInstType() != MOMain.MO_INSTTYPE_ASSOS) {

                        navItem.setTitle(titles[i]);
                        data.add(navItem);
                    }


                }
                else
                {
                    navItem.setTitle(titles[i]);
                    data.add(navItem);
                }


        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // drawer labels

        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        try {
            adapter = new NavigationDrawerAdapter(getActivity(), getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
//                drawerListener.onDrawerItemSelected(view, position);
                if(position==0)
                    startActivity(new Intent(getActivity(), AttendanceListActivity.class));
                if(position==1)
                    startActivity(new Intent(getActivity(), VisitListActivity.class));
                if(position==3)
                    startActivity(new Intent(getActivity(), EnquiryListActivity.class));
                if(position==2)
                    startActivity(new Intent(getActivity(), SupportListActivity.class));
                try {
                    if (CommonDataArea.getInstType() != MOMain.MO_INSTTYPE_ASSOS) {
                        if (position == 11)
                            startActivity(new Intent(getActivity(), LeaveSummaryActivity.class));
                    }
                    else
                    {
                        if (position == 7)
                            startActivity(new Intent(getActivity(), LeaveSummaryActivity.class));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
//                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return layout;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }


    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }
}
