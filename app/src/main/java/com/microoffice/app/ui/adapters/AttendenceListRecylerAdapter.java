package com.microoffice.app.ui.adapters;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.microoffice.app.R;

import java.util.ArrayList;

import moffice.meta.com.molibrary.models.dbmodels.AttnLog;


/**
 * Created by karan Nassa on 22/05/17.
 */
public class AttendenceListRecylerAdapter extends RecyclerView.Adapter<AttendenceListRecylerAdapter.RawTypeViewHolder> {

    private ArrayList<AttnLog> data;
    Context mContext;
    Typeface tfRobotoRegular, tfSfUiDisplayBold;

    public AttendenceListRecylerAdapter(ArrayList<AttnLog> attendanceListActivity) {
        this.data = attendanceListActivity;
    }

//
//    public AttendenceListRecylerAdapter(ArrayList<AttnLog> data, Context context) {
//        this.dataSet = data;
//        this.mContext = context;
//    }

    @Override
    public AttendenceListRecylerAdapter.RawTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_attendance_data, parent, false);
        return new RawTypeViewHolder(view);
    }


    @Override
    public int getItemViewType(int position) {

        /*switch (dataSet.get(position).type) {
            case 0:
                return FinanceFilter.TYPE_DATE;
            case 1:
                return FinanceFilter.TYPE_DATA;
            default:
                return -1;
        }*/
        return position;
    }

    @Override
    public void onBindViewHolder(final RawTypeViewHolder viewHolder, final int position) {

        if(data.size()>0) {
            viewHolder.tvname.setText(data.get(position).getUserName());
            if((data.get(position).getOfficeUUID()==null)||(data.get(position).getOfficeUUID().contentEquals("NA"))) {
                viewHolder.tvoffficename.setTextColor(Color.MAGENTA);
            }
            viewHolder.tvoffficename.setText(data.get(position).getOffice());
            viewHolder.tvdate.setText(Integer.toString(data.get(position).getLogDayOfMonth())+"/"+Integer.toString(data.get(position).getLogMonth())+"/"+Integer.toString(data.get(position).getLogYear()));
            if(data.get(position).getLogMinute()<10)
                viewHolder.tvtime.setText(Integer.toString(data.get(position).getLogHour())+":0"+Integer.toString(data.get(position).getLogMinute())+data.get(position).getANPM());
            else
                viewHolder.tvtime.setText(Integer.toString(data.get(position).getLogHour())+":"+Integer.toString(data.get(position).getLogMinute())+data.get(position).getANPM());
            viewHolder.tvinout.setText(data.get(position).getInOut());
            viewHolder.todaysPlan.setText(data.get(position).getTodaysPlan());
            viewHolder.place.setText(data.get(position).getPlaceVisit());

            viewHolder.tvoffficename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url=viewHolder.tvoffficename.getText().toString();
                    if( url.contains("https://")){
                      //  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                      //  viewHolder.itemView.getContext().startActivity(intent);

                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("geo:" + data.get(position).getLati()
                                        + "," + data.get(position).getLongi()
                                        + "?q=" + data.get(position).getLati()
                                        + "," + data.get(position).getLongi()
                                        + "(" + "Atten Loc" + ")"));
                        intent.setComponent(new ComponentName(
                                "com.google.android.apps.maps",
                                "com.google.android.maps.MapsActivity"));
                        viewHolder.itemView.getContext().startActivity(intent);
                    }
                }
            });
        }

    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public class RawTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvname,tvtime,tvdate;
        TextView tvoffficename,tvinout,todaysPlan,place;
        private RawTypeViewHolder(View view) {
            super(view);
           this.tvname = view.findViewById(R.id.tvname);
            this.tvoffficename = view.findViewById(R.id.tvofficename);
            this.tvdate = view.findViewById(R.id.tvdate);
            this.tvtime = view.findViewById(R.id.tvtime);
            this.tvinout = view.findViewById(R.id.tvinout);
            this.todaysPlan = view.findViewById(R.id.todaysplantxt);
            this.place = view.findViewById(R.id.placeVisit);

        }

        @Override
        public void onClick(View v) {
            /*if (dataSet.get(getAdapterPosition()).transactionListData.getTransactionId() != 0) {
                Intent intent = new Intent(mContext, FinanceDetailWebActivity.class);
                intent.putExtra("IsTopup", dataSet.get(getAdapterPosition()).transactionListData.getIsTopup());
                intent.putExtra("iTransactionId", dataSet.get(getAdapterPosition()).transactionListData.getTransactionId());
                intent.putExtra("strMemberId", ((YourAccountActivity) mContext).getMemberId());
                intent.putExtra("titleOfScreen", dataSet.get(getAdapterPosition()).transactionListData.getTitle());
                mContext.startActivity(intent);
            }else{
                ((YourAccountActivity)mContext).showAlertMessage(mContext.getResources().getString(R.string.error_no_transaction));
            }*/
        }
    }
}
