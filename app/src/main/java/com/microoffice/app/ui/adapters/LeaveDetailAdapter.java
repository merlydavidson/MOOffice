package com.microoffice.app.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.microoffice.app.R;

import java.sql.SQLException;
import java.util.List;

import moffice.meta.com.molibrary.models.dbmodels.LeaveList;
import moffice.meta.com.molibrary.utility.CommonDataArea;

public class LeaveDetailAdapter extends RecyclerView.Adapter<LeaveDetailAdapter.RawTypeViewHolder> {
    Context context;

    List<LeaveList> leaveLists;
    public LeaveDetailAdapter(Context context, List<LeaveList> list) {
        this.context = context;
        this.leaveLists = list;
    }

    public LeaveDetailAdapter() {
    }


    @Override
    public RawTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_leave_detail_data, parent, false);
        return new LeaveDetailAdapter.RawTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RawTypeViewHolder holder, int position) {

        holder.tvStartDAte.setText(leaveLists.get(position).getStartDat());
        holder.tvEndDate.setText(leaveLists.get(position).getEndDate());
        holder.leaveRej.setText(leaveLists.get(position).getRejectionNotes());
        holder.numberOfDays.setText(String.valueOf(leaveLists.get(position).getNumDays()));
        if (leaveLists.get(position).getLvType() == 1)
            holder.leaveType.setText("Casual Leave");
        if (leaveLists.get(position).getLvType() == 2)
            holder.leaveType.setText("Earned Leave ");
        if (leaveLists.get(position).getLvType() == 3)
            holder.leaveType.setText("LOP  ");

        holder.leaveReason.setText(leaveLists.get(position).getNotes());
        if (leaveLists.get(position).getLvStatus() == 1)
            holder.leaveStatus.setText("Requested");

        if (leaveLists.get(position).getLvStatus() == 2)
            holder.leaveStatus.setText("Approved");
        if (leaveLists.get(position).getLvStatus() == 3)
            holder.leaveStatus.setText("Rejected");

        if (leaveLists.get(position).getLvStatus() == 4)
            holder.leaveStatus.setText("Discarded");


    }

    @Override
    public int getItemCount() {
        return leaveLists.size();
    }


    public class RawTypeViewHolder extends RecyclerView.ViewHolder  {
        TextView tvStartDAte, tvEndDate,leaveRej;
        TextView numberOfDays, leaveType, leaveReason, leaveStatus;
LinearLayout linearLayout;

        private RawTypeViewHolder(View view) {
            super(view);

            this.tvStartDAte = view.findViewById(R.id.tvdlstartDate);
            this.tvEndDate = view.findViewById(R.id.tvdlEndDate);
            this.numberOfDays = view.findViewById(R.id.tvdlNumOfDays);
            this.leaveType = view.findViewById(R.id.tvdlleaveType);
            this.leaveReason = view.findViewById(R.id.tvdlleaveReason);
            this.leaveStatus = view.findViewById(R.id.tvdlleaveStatus);
            this.leaveRej = view.findViewById(R.id.tvrejectionNotes);
        //    this.linearLayout = view.findViewById(R.id.lldetail);


        }


    }

    }
