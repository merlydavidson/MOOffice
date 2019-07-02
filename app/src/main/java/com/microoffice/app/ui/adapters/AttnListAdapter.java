package com.microoffice.app.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.microoffice.app.R;

import java.util.List;

import moffice.meta.com.molibrary.models.dbmodels.Employees;

/**
 * Created by DANIYAL KJ on 25-Nov-17.
 */

public class AttnListAdapter extends BaseAdapter {
    Activity context;
    List<Employees> data;

    public AttnListAdapter(Activity context, List<Employees> data) {
        this.context = context;
        this.data = data;
//        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

//        rowView = inflater.inflate(R.layout.list_item_attendance_data, parent, false);
        ViewHiolder viewHolder = new ViewHiolder();
//        View_Holder viewHolder = new View_Holder();
        if(view ==null) {
            LayoutInflater layoutInflater = null;
            layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item_employee_data, null);
            viewHolder.tvempname = view.findViewById(R.id.tvempname);
            viewHolder.tvoffficename = view.findViewById(R.id.tvofficename);
            viewHolder.tvdate = view.findViewById(R.id.tvdate);
            viewHolder.tvtime = view.findViewById(R.id.tvtime);
            viewHolder.tvinout = view.findViewById(R.id.tvinout);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHiolder)view.getTag();
        }
        viewHolder.tvempname.setText(data.get(position).getEmpName());
        viewHolder.tvoffficename.setText(data.get(position).getOfficeName());
        viewHolder.tvdate.setText(Integer.toString(data.get(position).getLogDayOfMonth())+"/"+Integer.toString(data.get(position).getLogMonth())+"/"+Integer.toString(data.get(position).getLogYear()));
        viewHolder.tvtime.setText(Integer.toString(data.get(position).getLogHour())+":"+Integer.toString(data.get(position).getLogMinute()));
        viewHolder.tvinout.setText(data.get(position).getInOut());
        return view;
    }
//    class View_Holder {
//
//    }

    private class ViewHiolder {
        TextView tvempname,tvtime,tvdate;
        TextView tvoffficename,tvinout;
    }
}
