package com.microoffice.app.ui.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import com.microoffice.app.R;
import com.microoffice.app.ui.activities.AddEnquiryActivity;
import com.microoffice.app.utils.AppUtils;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.ArrayList;

import moffice.meta.com.molibrary.models.dbmodels.EnquiryDetails;
import moffice.meta.com.molibrary.models.dbmodels.UserList;
import moffice.meta.com.molibrary.utility.CommonDataArea;

/**
 * Created by alphonsa on 20/11/17.
 */

public class EnquiryListRecyclerAdapter extends RecyclerView.Adapter<EnquiryListRecyclerAdapter.RawTypeViewHolder> {

    private AlertDialog dialog;
    private ArrayList<EnquiryDetails> dataSet;
    Activity mContext;
    String newStatus;
    View viewParent;
    private static final String[] STATUS = new String[]{ "Open" ,"Assigned","Closed"};


    public EnquiryListRecyclerAdapter(ArrayList<EnquiryDetails> data, Activity context) {
        this.dataSet = data;
        this.mContext = context;
    }


    @Override
    public EnquiryListRecyclerAdapter.RawTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        viewParent = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_enquiry_data, parent, false);


        return new RawTypeViewHolder(viewParent);
        //  return null;
    }

    @Override
    public void onBindViewHolder(final  RawTypeViewHolder holder,final int listPosition) {

        if(dataSet.size()>0) {
            holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AppUtils.callToIntent(mContext,
                            AddEnquiryActivity.class,
                            false);

                    return false;
                }
            });
          /*  boolean flagFill=false;
            try {
                if (CommonDataArea.getInstType() != MOMain.MO_INSTTYPE_ADMIN) {
                   if(dataSet.get(listPosition).getName().equalsIgnoreCase(CommonDataArea.getCommonSettings().getName())){
                       flagFill=true;
                   }
                    if(dataSet.get(listPosition).getAssignedTo().equalsIgnoreCase(CommonDataArea.getCommonSettings().getName())){
                        flagFill=true;
                    }
                }else flagFill=true;
            }catch (Exception exp){

            }
            if(!flagFill) return;;*/
            holder.recUUID = dataSet.get(listPosition).getRecUUID();
            holder.tvCust.setText(dataSet.get(listPosition).getCustName());
            holder.tvName.setText(dataSet.get(listPosition).getName());
            holder.tvNumber.setText(dataSet.get(listPosition).getPhoneNumber());
            holder.tvProduct.setText(dataSet.get(listPosition).getProduct());
            final RawTypeViewHolder Holder = holder;
            holder.tvProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String id=dataSet.get(listPosition).getId().toString();
                    final String productName= Holder.tvProduct.getText().toString();
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    LayoutInflater inflater =mContext.getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.alert_new_alert,null);

                    builder.setView(dialogView);
                    final EditText NewValue = (EditText) dialogView.findViewById(R.id.newValue);
                    NewValue.setText(productName);
                    Button butOK = (Button) dialogView.findViewById(R.id.ok);
                    Button butCANCEL = (Button) dialogView.findViewById(R.id.cancel);
                    butOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                Dao<EnquiryDetails,Integer> EnquiryDao= CommonDataArea.getHelper(mContext).getEnquiryDao();
                                UpdateBuilder<EnquiryDetails, Integer> updateBuilder = EnquiryDao.updateBuilder();
                                updateBuilder.where().eq("_id",id);
                                updateBuilder.updateColumnValue("product", NewValue.getText().toString());
                                updateBuilder.updateColumnValue("sendstatus", 0);
                                updateBuilder.update();
                                notifyDataSetChanged();
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


            holder.tvDesc.setText(dataSet.get(listPosition).getDescription());
            holder.tvDesc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String id=dataSet.get(listPosition).getId().toString();
                    final String descName= Holder.tvDesc.getText().toString();
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    LayoutInflater inflater =mContext.getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.alert_new_alert,null);
                    builder.setView(dialogView);
                    final EditText NewValue = (EditText) dialogView.findViewById(R.id.newValue);
                    NewValue.setText(descName);
                    Button butOK = (Button) dialogView.findViewById(R.id.ok);
                    Button butCANCEL = (Button) dialogView.findViewById(R.id.cancel);
                    butOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                Dao<EnquiryDetails,Integer> EnquiryDao= CommonDataArea.getHelper(mContext).getEnquiryDao();
                                UpdateBuilder<EnquiryDetails, Integer> updateBuilder = EnquiryDao.updateBuilder();
                                updateBuilder.where().eq("_id",id);
                                updateBuilder.updateColumnValue("description", NewValue.getText().toString());
                                updateBuilder.updateColumnValue("sendstatus", 0);
                                updateBuilder.update();
                                notifyDataSetChanged();
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
            holder.tvDate.setText(dataSet.get(listPosition).getDate());
            holder.tvNotes.setText(dataSet.get(listPosition).getNote());
            holder.tvNotes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String id=dataSet.get(listPosition).getId().toString();
                    final String noteName= Holder.tvNotes.getText().toString();
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    LayoutInflater inflater =mContext.getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.alert_new_alert,null);
                    builder.setView(dialogView);
                    final EditText NewValue = (EditText) dialogView.findViewById(R.id.newValue);
                    NewValue.setText(noteName);
                    Button butOK = (Button) dialogView.findViewById(R.id.ok);
                    Button butCANCEL = (Button) dialogView.findViewById(R.id.cancel);
                    butOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                Dao<EnquiryDetails,Integer> EnquiryDao= CommonDataArea.getHelper(mContext).getEnquiryDao();
                                UpdateBuilder<EnquiryDetails, Integer> updateBuilder = EnquiryDao.updateBuilder();
                                updateBuilder.where().eq("_id",id);
                                updateBuilder.updateColumnValue("note", NewValue.getText().toString());
                                updateBuilder.updateColumnValue("sendstatus", 0);
                                updateBuilder.update();
                                notifyDataSetChanged();
                                viewParent.invalidate();
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
            holder.tvStatus.setText(dataSet.get(listPosition).getStatus());
            holder.tvStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String id=dataSet.get(listPosition).getId().toString();
                    final String statusName= Holder.tvStatus.getText().toString();
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    LayoutInflater inflater =mContext.getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.alert_spinner,null);
                    builder.setView(dialogView);
                    final Spinner NewValue = (Spinner) dialogView.findViewById(R.id.newValue);
                    ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, STATUS);
                    NewValue.setAdapter(spinAdapter);
                    NewValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            newStatus =adapterView.getItemAtPosition(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    Button butOK = (Button) dialogView.findViewById(R.id.ok);
                    Button butCANCEL = (Button) dialogView.findViewById(R.id.cancel);
                    butOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                Dao<EnquiryDetails,Integer> EnquiryDao= CommonDataArea.getHelper(mContext).getEnquiryDao();
                                UpdateBuilder<EnquiryDetails, Integer> updateBuilder = EnquiryDao.updateBuilder();
                                updateBuilder.where().eq("_id",id);
                                updateBuilder.updateColumnValue("status", newStatus);
                                updateBuilder.updateColumnValue("sendstatus", 0);
                                updateBuilder.update();
                                notifyDataSetChanged();
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
            holder.tvAssignedTo.setText(dataSet.get(listPosition).getAssignedTo());
            holder.tvAssignedTo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    try {
                        final String id=dataSet.get(listPosition).getId().toString();
                        final PopupMenu popup = new PopupMenu(view.getContext(), holder.itemView);
                        Dao<UserList, Integer> usrDao = CommonDataArea.getHelper(mContext).getUserListDao();
                        for(UserList user : usrDao) {
                            Menu menu = popup.getMenu();
                            menu.add(user.getName());
                        }
                        popup.show();
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                try {
                                    Dao<EnquiryDetails, Integer> enqDao = CommonDataArea.getHelper(mContext).getEnquiryDao();
                                    UpdateBuilder<EnquiryDetails, Integer> updateBuilder = enqDao.updateBuilder();
                                    updateBuilder.where().eq("_id",id);
                                    updateBuilder.updateColumnValue("assignedTo", menuItem.getTitle().toString());
                                    updateBuilder.updateColumnValue("sendstatus", 0);
                                    updateBuilder.update();

                                    notifyDataSetChanged();
                                    viewParent.invalidate();
                                }catch(Exception exp){
                                    return false;
                                }
                                return true;
                            }
                        });
                    }catch (Exception exp){

                    }

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class RawTypeViewHolder extends RecyclerView.ViewHolder  {

        private TextView tvName;
        private TextView tvCust;
        private TextView tvNumber;
        private TextView tvProduct;
        private TextView tvDesc;
        private TextView tvDate;
        private TextView tvNotes;
        private TextView tvAssignedTo;
        private TextView tvStatus;
        private LinearLayout linearLayout;
        public String recUUID;

        public RawTypeViewHolder(View itemView) {
            super(itemView);
            tvCust = (TextView) itemView.findViewById(R.id.tvCust);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvNumber = (TextView) itemView.findViewById(R.id.tvNumber);
            tvProduct = (TextView) itemView.findViewById(R.id.tvProduct);
            tvDesc = (TextView) itemView.findViewById(R.id.tvDesc);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvNotes = (TextView) itemView.findViewById(R.id.tvNotes);
            tvAssignedTo=(TextView)itemView.findViewById(R.id.tvAssignedTo);
            tvStatus=(TextView)itemView.findViewById(R.id.tvStatus);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.enquiryData);
        }


    }

}
