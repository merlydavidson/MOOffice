package com.microoffice.app.ui.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.microoffice.app.R;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;

import moffice.meta.com.molibrary.models.dbmodels.Offices;
import moffice.meta.com.molibrary.utility.CommonDataArea;


public class OfficeListAdapter extends BaseAdapter {

    Activity context;
    List<Offices> data;
    LayoutInflater inflater = null;
    String strLastDate = "";
    boolean isJoinVisible;
    private AlertDialog dialog;
    Fragment fragment;
    String title = "";

    /**
     * Adapter to initialize all instances.
     *  @param context :        To hold context.
     * @param data
     *
     */
    public OfficeListAdapter(Activity context, List<Offices> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * @return array size.
     */
    @Override
    public int getCount() {
        return data.size();
    }

    /**
     * @return item position
     */
    @Override
    public Object getItem(int position) {
        return position;
    }

    /**
     * @return item id
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * @return View after create COMPETITIONS row
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        View_Holder viewHolder = null;
        rowView = inflater.inflate(R.layout.list_item_office_data, parent, false);
        viewHolder = new View_Holder();
        viewHolder.tvSno = rowView.findViewById(R.id.tvSno);
        viewHolder.tvOfficeName = rowView.findViewById(R.id.tvOfficeName);
        viewHolder.tvOfficeLat = rowView.findViewById(R.id.tvOfficeLat);
        viewHolder.tvOfficeLong = rowView.findViewById(R.id.tvOfficeLong);
        viewHolder.tvAddress = rowView.findViewById(R.id.tvAddress);

        rowView.setTag(viewHolder);

        viewHolder = (View_Holder) rowView.getTag();

        /**
         *  Set Course Diary events on each view.
         */
        viewHolder.tvSno.setText(""+data.get(position).getOfficeID());
        viewHolder.tvOfficeName.setText(data.get(position).getOfficeName());
        final View_Holder finalViewHolder = viewHolder;
        viewHolder.tvOfficeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String id=data.get(position).getOfficeID().toString();
                final String officeName= finalViewHolder.tvOfficeName.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                LayoutInflater inflater = context.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.alert_edit_office,null);
                builder.setView(dialogView);
                final EditText edtOfficeNewName = (EditText) dialogView.findViewById(R.id.officeName);
                edtOfficeNewName.setText(officeName);
                Button butOK = (Button) dialogView.findViewById(R.id.ok);
                Button butCANCEL = (Button) dialogView.findViewById(R.id.cancel);
                butOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Dao<Offices,Integer> OfficeDao= CommonDataArea.getHelper(context).getOfficesDao();
                            DeleteBuilder<Offices, Integer> deleteBuilder = OfficeDao.deleteBuilder();
                            deleteBuilder.where().eq("OfficeID", id);
                            deleteBuilder.prepare();
                            deleteBuilder.delete();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
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
        viewHolder.tvOfficeLat.setText(String.valueOf(data.get(position).getLatitude()));
        viewHolder.tvOfficeLong.setText(String.valueOf(data.get(position).getLongitude()));
        viewHolder.tvAddress.setText(data.get(position).getAddress());

        return rowView;
    }


    class View_Holder {
        /**
         * Text Row VIEW INSTANCES DECLARATION
         */
        TextView tvSno, tvOfficeLat, tvOfficeLong;
        TextView tvAddress;
        TextView tvOfficeName;
    }
}