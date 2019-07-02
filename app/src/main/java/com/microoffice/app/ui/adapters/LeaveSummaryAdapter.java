package com.microoffice.app.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.microoffice.app.R;
import com.microoffice.app.ui.activities.LeaveDetailActivity;

import java.util.ArrayList;

import moffice.meta.com.molibrary.models.dbmodels.LeaveList;
import moffice.meta.com.molibrary.models.dbmodels.LeaveSummary;

public class LeaveSummaryAdapter extends RecyclerView.Adapter<LeaveSummaryAdapter.RawTypeViewHolder> {
    ArrayList<LeaveSummary> leaveSummaries;
    Context context;

    public LeaveSummaryAdapter(ArrayList<LeaveSummary> leaveSummaries, Context context) {
        this.leaveSummaries = leaveSummaries;
        this.context = context;
    }

    @Override
    public RawTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_summary_data, parent, false);
        return new LeaveSummaryAdapter.RawTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RawTypeViewHolder holder, final int position) {
        if(leaveSummaries.size()!=0){
            holder.linearLayout.setVisibility(View.VISIBLE);
        holder.tvname.setText(leaveSummaries.get(position).getUserName());
        holder.tvCasualLeave.setText(String.valueOf(leaveSummaries.get(position).getCasualLeave()));
        holder.tvEarnedLeave.setText(String.valueOf(leaveSummaries.get(position).getEarnedlLeave()));
        holder.tvLOP.setText(String.valueOf(leaveSummaries.get(position).getLopLeave()));
        holder.tvTotal.setText(String.valueOf(leaveSummaries.get(position).getTotalLeave()));
            holder.tvname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle=new Bundle();
                    bundle.putString("uuid",leaveSummaries.get(position).getUuid());
                    Intent intent=new Intent(context, LeaveDetailActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
        else
        {
            holder.linearLayout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return leaveSummaries.size();
    }

    public class RawTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvname, tvCasualLeave, tvEarnedLeave, tvLOP, tvTotal;
LinearLayout linearLayout;

        private RawTypeViewHolder(View view) {
            super(view);
            this.tvname = view.findViewById(R.id.tvname);
            this.tvCasualLeave = view.findViewById(R.id.tvCasualLeave);
            this.tvEarnedLeave = view.findViewById(R.id.tvEarnedLeave);
            this.tvLOP = view.findViewById(R.id.tvLOP);
            this.tvTotal = view.findViewById(R.id.tvTotalLeave);
            this.linearLayout = view.findViewById(R.id.llsummary);

        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
