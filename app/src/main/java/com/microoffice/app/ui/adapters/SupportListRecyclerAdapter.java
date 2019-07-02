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
import moffice.meta.com.molibrary.models.dbmodels.SupportDetails;
import moffice.meta.com.molibrary.models.dbmodels.UserList;
import moffice.meta.com.molibrary.utility.CommonDataArea;

/**
 * Created by user on 12/14/2017.
 */

public class SupportListRecyclerAdapter extends RecyclerView.Adapter<SupportListRecyclerAdapter.RawTypeViewHolder> {

    private AlertDialog dialog;

    Activity mContext;
    String newStatus;

    private static final String[] STATUS = new String[]{ "Open" ,"Assigned","Closed"};
    private ArrayList<SupportDetails> dataSet;


    public SupportListRecyclerAdapter(ArrayList<SupportDetails> data, Activity context) {
        this.dataSet = data;
        this.mContext = context;
    }



    @Override
    public SupportListRecyclerAdapter.RawTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_support_data, parent, false);


        return new SupportListRecyclerAdapter.RawTypeViewHolder(view);

        //  return null;
    }

    @Override
    public void onBindViewHolder(final SupportListRecyclerAdapter.RawTypeViewHolder holder, final int listPosition) {

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

            holder.tvCust.setText(dataSet.get(listPosition).getCustName());
            holder.tvName.setText(dataSet.get(listPosition).getName());
            holder.tvNumber.setText(dataSet.get(listPosition).getPhoneNumber());
            holder.tvProduct.setText(dataSet.get(listPosition).getProduct());
            final SupportListRecyclerAdapter.RawTypeViewHolder Holder = holder;
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
                                Dao<SupportDetails,Integer> SupportDao= CommonDataArea.getHelper(mContext).getSupportDao();
                                UpdateBuilder<SupportDetails, Integer> updateBuilder = SupportDao.updateBuilder();
                                updateBuilder.where().eq("_id",id);
                                updateBuilder.updateColumnValue("product", NewValue.getText().toString());
                                updateBuilder.update();
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
                                Dao<SupportDetails,Integer> SupportDao= CommonDataArea.getHelper(mContext).getSupportDao();
                                UpdateBuilder<SupportDetails, Integer> updateBuilder = SupportDao.updateBuilder();
                                updateBuilder.where().eq("_id",id);
                                updateBuilder.updateColumnValue("description", NewValue.getText().toString());
                                updateBuilder.update();
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
                                Dao<SupportDetails,Integer> SupportDao= CommonDataArea.getHelper(mContext).getSupportDao();
                                UpdateBuilder<SupportDetails, Integer> updateBuilder = SupportDao.updateBuilder();
                                updateBuilder.where().eq("_id",id);
                                updateBuilder.updateColumnValue("note", NewValue.getText().toString());
                                updateBuilder.update();
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
            holder.tvAssignedTo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
                                    Dao<SupportDetails, Integer> enqDao = CommonDataArea.getHelper(mContext).getSupportDao();
                                    UpdateBuilder<SupportDetails, Integer> updateBuilder = enqDao.updateBuilder();
                                    updateBuilder.where().eq("_id",id);
                                    updateBuilder.updateColumnValue("assignedTo", menuItem.getTitle().toString());
                                    updateBuilder.updateColumnValue("sendstatus", 0);
                                    updateBuilder.update();

                                    notifyDataSetChanged();

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
            holder.tvStatus.setText(dataSet.get(listPosition).getStatus());
            holder.tvStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String statusName= Holder.tvStatus.getText().toString();
                    final String id=dataSet.get(listPosition).getId().toString();
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
                                Dao<SupportDetails,Integer> SupportDao= CommonDataArea.getHelper(mContext).getSupportDao();
                                UpdateBuilder<SupportDetails, Integer> updateBuilder = SupportDao.updateBuilder();
                                updateBuilder.where().eq("_id",id);
                                updateBuilder.updateColumnValue("status", newStatus);
                                updateBuilder.update();
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
        }


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class RawTypeViewHolder extends RecyclerView.ViewHolder  {

        private TextView tvCust;
        private TextView tvName;
        private TextView tvNumber;
        private TextView tvProduct;
        private TextView tvDesc;
        private TextView tvDate;
        private TextView tvNotes;
        private TextView tvAssignedTo;
        private TextView tvStatus;
        LinearLayout linearLayout;

        public RawTypeViewHolder(View itemView) {
            super(itemView);
            tvCust = (TextView) itemView.findViewById(R.id.tvCustName);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvNumber = (TextView) itemView.findViewById(R.id.tvNumber);
            tvProduct = (TextView) itemView.findViewById(R.id.tvProduct);
            tvDesc = (TextView) itemView.findViewById(R.id.tvDesc);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvNotes = (TextView) itemView.findViewById(R.id.tvNotes);
            tvAssignedTo=(TextView)itemView.findViewById(R.id.tvAssignedTo);
            tvStatus=(TextView)itemView.findViewById(R.id.tvStatus);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.supportData);

        }

//       tvTimeIn, tvTimeOut, tvTimeWorked;

//        private RawTypeViewHolder(View itemView) {


//            super(itemView);

//            this.tvDate = itemView.findViewById(R.id.tvDate);
//            this.tvNameOfOffice = itemView.findViewById(R.id.tvNameOfOffice);
//            this.tvTimeIn = itemView.findViewById(R.id.tvTimeIn);
//            this.tvTimeOut = itemView.findViewById(R.id.tvTimeOut);
//            this.tvTimeWorked = itemView.findViewById(R.id.tvTimeWorked);

        // llFinanceView.setOnClickListener(this);
    }

//        @Override
//        public void onClick(View v) {
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
//    }
