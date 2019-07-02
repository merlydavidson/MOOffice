package com.microoffice.app.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.microoffice.app.R;
import com.microoffice.app.utils.CallLogs;

import java.util.ArrayList;

/**
 * Created by user on 12/13/2017.
 */

public class CallLogRecyclerAdapter extends RecyclerView.Adapter<CallLogRecyclerAdapter.ViewHolder> {
    ArrayList<CallLogs> callLogs = new ArrayList<>();
    Context mContext;
    public CallLogRecyclerAdapter(Context mContext,ArrayList<CallLogs> callLogs) {
        this.mContext=mContext;
        this.callLogs=callLogs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_call_log, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.name.setText(callLogs.get(position).getName());
        holder.mobile.setText(callLogs.get(position).getMobile());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=holder.name.getText().toString();
                String mobile=holder.mobile.getText().toString();
                Intent intent = new Intent("log");
                intent.putExtra("name",name);
                intent.putExtra("mobile",mobile);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return callLogs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView mobile;
        LinearLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            layout=(LinearLayout)itemView.findViewById(R.id.layout);
            name = (TextView) itemView.findViewById(R.id.tvContactName);
            mobile = (TextView) itemView.findViewById(R.id.tvContactNumber);
        }
    }
}
