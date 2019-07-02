package com.microoffice.app.ui.adapters;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.microoffice.app.R;
import com.microoffice.app.ui.activities.LeaveDetailActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import moffice.meta.com.molibrary.core.MOLeaveManager;
import moffice.meta.com.molibrary.core.MOMain;
import moffice.meta.com.molibrary.models.dbmodels.AttnLog;
import moffice.meta.com.molibrary.models.dbmodels.LeaveList;
import moffice.meta.com.molibrary.utility.CommonDataArea;

public class LeaveListAdapter extends RecyclerView.Adapter<LeaveListAdapter.RawTypeViewHolder> {

    private ArrayList<LeaveList> data;
    String reason = "";

    Typeface tfRobotoRegular, tfSfUiDisplayBold;
    Context context;

    public LeaveListAdapter(ArrayList<LeaveList> leaveListActivity, Context context) {
        this.data = leaveListActivity;
        this.context = context;
    }

    @Override
    public LeaveListAdapter.RawTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_leave_data, parent, false);
        return new LeaveListAdapter.RawTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final  RawTypeViewHolder holder, final int position) {


        // attaching data adapter to spinner

        if (data.size() > 0) {
            String status = "";
            if (data.get(position).getLvStatus() == 1)
                status = "Requested";
            if (data.get(position).getLvStatus() == 2)
                status = "Approved";
            if (data.get(position).getLvStatus() == 3)
                status = "Rejected";
            if (data.get(position).getLvStatus() == 4)
                status = "Discarded";
            try {
                if (CommonDataArea.getInstType() == MOMain.MO_INSTTYPE_ADMIN ) {
                    List<String> statusSp = new ArrayList<String>();
                    statusSp.add("Change Status");
                    statusSp.add("Requested");
                    statusSp.add("Approved");
                    statusSp.add("Rejected");
                    statusSp.add("Discarded");
                    final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, statusSp);
                    // Drop down layout style - list view with radio button
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    holder.spinner.setAdapter(dataAdapter);
                    holder.spinner.setSelection(0);
                    holder.spinner.setVisibility(View.VISIBLE);
                    holder.tvname.setClickable(true);
                    holder.leaveStatus.setVisibility(View.VISIBLE);
                    holder.leaveStatus.setText(status);
                } else if(CommonDataArea.getInstType() == MOMain.MO_INSTTYPE_ASSOS){
                    List<String> statusSp2 = new ArrayList<String>();
                    statusSp2.add("Change Status");
                    statusSp2.add("Discarded");
                    final ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, statusSp2);
                    holder.spinner.setAdapter(dataAdapter2);
                    holder.spinner.setSelection(0);
                    holder.spinner.setVisibility(View.VISIBLE);
                    holder.leaveStatus.setVisibility(View.VISIBLE);
                    holder.leaveStatus.setText(status);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.leaveListItem = data.get(position);
            holder.tvname.setText(data.get(position).getUserName());
            holder.tvStartDAte.setText(data.get(position).getStartDat());
            holder.tvEndDate.setText(data.get(position).getEndDate());
            holder.numberOfDays.setText(Float.toString(data.get(position).getNumDays()));
            if (data.get(position).getLvType() == 1)
                holder.leaveType.setText("Casual Leave");
            if (data.get(position).getLvType() == 2)
                holder.leaveType.setText("Earned Leave ");
            if (data.get(position).getLvType() == 3)
                holder.leaveType.setText("LOP  ");

            holder.leaveReason.setText(data.get(position).getNotes());
            holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(holder.ignoreFirstSel==true){
                        holder.ignoreFirstSel=false;
                        return;
                    }
                    final String selItem =  holder.spinner.getSelectedItem().toString();
                    final EditText input = new EditText(context);
                    if (i != 0) {
                        final int pos = i;
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        if (selItem.equals("Rejected")) {
                            input.setHint("Reason");
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT);
                            input.setLayoutParams(lp);
                            alertDialogBuilder.setView(input);
                        }
                        alertDialogBuilder.setTitle("Leave Request");
                        alertDialogBuilder.setMessage("Are you sure about this action?");
                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    if (!input.getText().toString().equalsIgnoreCase("")) {
                                        reason = input.getText().toString();
                                    }
                                    Calendar c = Calendar.getInstance();
                                    if(CommonDataArea.getInstType()== MOMain.MO_INSTTYPE_ADMIN) {
                                        switch (selItem) {
                                            case "Approved":
                                                if ((holder.leaveListItem.getLvStatus() == MOMain.LEAVESTATUS_REQUESTED)||(holder.leaveListItem.getLvStatus() == MOMain.LEAVESTATUS_REJECTED)) {
                                                    MOLeaveManager.updateLeaveStatus(holder.leaveListItem.getLeaveID(), holder.leaveListItem.getUserUUID(), reason, MOMain.LEAVESTATUS_APROVED, context);
                                                } else {
                                                    Toast.makeText(context, "Leave request not in REQUESTED status ,Can not approve!!!", Toast.LENGTH_LONG).show();
                                                }
                                                break;
                                            case "Discarded":
                                                if (holder.leaveListItem.getStartDatTimeStamp() < c.getTimeInMillis() &&
                                                        (holder.leaveListItem.getLvStatus()== MOMain.LEAVESTATUS_APROVED)) {
                                                    Toast.makeText(context, "Leave request canot DISCARDED after approved and leave started!!!", Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toast.makeText(context, "Leave request discarded!!!", Toast.LENGTH_LONG).show();
                                                    MOLeaveManager.updateLeaveStatus(holder.leaveListItem.getLeaveID(), holder.leaveListItem.getUserUUID(), reason, MOMain.LEAVESTATUS_DISCARDED, context);
                                                }

                                                break;
                                            case "Rejected":
                                                if (holder.leaveListItem.getStartDatTimeStamp() > c.getTimeInMillis() && (holder.leaveListItem.getLvStatus() == MOMain.LEAVESTATUS_APROVED)) {
                                                    Toast.makeText(context, "Leave request canot REJECTED after approved and leave started!!!", Toast.LENGTH_LONG).show();

                                                } else {
                                                    if (holder.leaveListItem.getLvStatus() != MOMain.LEAVESTATUS_DISCARDED) {
                                                        MOLeaveManager.updateLeaveStatus(holder.leaveListItem.getLeaveID(), holder.leaveListItem.getUserUUID(), reason, MOMain.LEAVESTATUS_REJECTED, context);
                                                    }
                                                }
                                                break;
                                        }
                                    }else{
                                        if(selItem.equals("Discarded")){
                                            if ((holder.leaveListItem.getStartDatTimeStamp() > c.getTimeInMillis())&&(holder.leaveListItem.getLvStatus()== MOMain.LEAVESTATUS_APROVED)) {
                                                Toast.makeText(context, "Leave request canot DISCARDED after approved and leave started!!!", Toast.LENGTH_LONG).show();
                                            }else{
                                                MOLeaveManager.updateLeaveStatus(holder.leaveListItem.getLeaveID(), holder.leaveListItem.getUserUUID(), reason, MOMain.LEAVESTATUS_DISCARDED, context);
                                            }
                                        }else {
                                            Toast.makeText(context, "Leave request canot be modified by associate after posting, Discard and re try!!!", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                try {
                                    Toast.makeText(context, "No action ", Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();


                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }
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
    public int getItemCount() {
        return data.size();
    }

    public class RawTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvname, tvStartDAte, tvEndDate;
        TextView numberOfDays, leaveType, leaveReason, leaveStatus;
        Spinner spinner;
        LeaveList leaveListItem;
        boolean ignoreFirstSel = true;

        private RawTypeViewHolder(View view) {
            super(view);
            this.tvname = view.findViewById(R.id.tvname);
            this.tvStartDAte = view.findViewById(R.id.startDate);
            this.tvEndDate = view.findViewById(R.id.endDate);
            this.numberOfDays = view.findViewById(R.id.noOfDays);
            this.leaveType = view.findViewById(R.id.leaveType);
            this.leaveReason = view.findViewById(R.id.leaveReason);
            this.leaveStatus = view.findViewById(R.id.leaveStatus);
            this.spinner = view.findViewById(R.id.spleaveStatus);

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
