package com.microoffice.app.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.microoffice.app.R;
import com.microoffice.app.ui.activities.VisitMeetingList;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import moffice.meta.com.molibrary.models.dbmodels.VisitLog;
import moffice.meta.com.molibrary.utility.CommonDataArea;
import moffice.meta.com.molibrary.utility.MarkerToDraw;


public class VisitListRecyclerAdapter extends RecyclerView.Adapter<VisitListRecyclerAdapter.RawTypeViewHolder> {


    private ArrayList<VisitLog> dataSet;
    Context mContext;
    String location;
    ArrayList<LatLng> points;
    Typeface tfRobotoRegular, tfSfUiDisplayBold;
    public static   SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    double lati;
    double longi;


//    public VisitListRecyclerAdapter(VisitListActivity visitListActivity, ArrayList<VisitLog> visitArrayList) {
//        this.mContext=visitListActivity;
//    }

    /*public static class DateTypeViewHolder extends RecyclerView.ViewHolder {

       TextView txtType;
       RelativeLayout rlFinanceTitleGroup;

       public DateTypeViewHolder(View itemView) {
           super(itemView);

           this.txtType = (TextView) itemView.findViewById(R.id.tvDateOfTrans);
           this.rlFinanceTitleGroup = (RelativeLayout) itemView.findViewById(R.id.rlFinanceTitleGroup);
       }

   }*/



    public VisitListRecyclerAdapter(ArrayList<VisitLog> data, Context context) {
        this.dataSet = data;
        this.mContext = context;
        points = new ArrayList<LatLng>();
    }



    @Override
    public VisitListRecyclerAdapter.RawTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_visit_data, parent, false);


        return new RawTypeViewHolder(view);

      //  return null;
    }




    //    @Override
//    public int getItemViewType(int position) {
//
//        /*switch (dataSet.get(position).type) {
//            case 0:
//                return FinanceFilter.TYPE_DATE;
//            case 1:
//                return FinanceFilter.TYPE_DATA;
//            default:
//                return -1;
//        }*/
//        return position;
//    }

    @Override
    public void onBindViewHolder(final  RawTypeViewHolder holder,final int listPosition) {


        if(dataSet.size()>0) {
            String dateStr = dataSet.get(listPosition).getDateTime();
            String datePart="NA";
            String hourPart="NA";
            if(dateStr.indexOf(":")>6) {
                 datePart = dateStr.substring(0, dateStr.indexOf(":"));
                 hourPart = dateStr.substring(dateStr.indexOf(":") + 1);
            }

            holder.tvDate.setText(datePart);//dataSet.get(listPosition).getLogDayOfMonth()+"/"+dataSet.get(listPosition).getLogMonth()+"/"+dataSet.get(listPosition).getLogYear());
            holder.tvName.setText(dataSet.get(listPosition).getUserName());
            holder.tvBiz.setText(dataSet.get(listPosition).getBusinessName());
            holder.tvLocation.setText(dataSet.get(listPosition).getPlaceName());
            if(dataSet.get(listPosition).getLogMinute()<9)
                holder.tvTime.setText(dataSet.get(listPosition).getLogHour()+":0"+dataSet.get(listPosition).getLogMinute());
            else
                holder.tvTime.setText(dataSet.get(listPosition).getLogHour()+":"+dataSet.get(listPosition).getLogMinute());
            holder.tvTime.setText(hourPart);
            holder.tvPurpose.setText(dataSet.get(listPosition).getPurpose());
            holder.position =listPosition;
            final int status=dataSet.get(listPosition).getArrivedDeparted();
            if (status==1)
            {
                holder.tvAction.setText("ARRIVED");
            }
            else
                {

                String text="DEPARTED";
                SpannableString content = new SpannableString(text);
                content.setSpan(new UnderlineSpan(), 0, text.length(), 0);
                holder.tvAction.setText(content);
                holder.tvAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        VisitMeetingList.curVisUUID = dataSet.get(holder.position).getRecUUID();
                        VisitMeetingList.visLog = dataSet.get(holder.position);
                        holder.mainView.getContext().startActivity(new Intent(holder.mainView.getContext(), VisitMeetingList.class));
                       // this.mContext.startActivity(new Intent(getActivity(), VisitListActivity.class));
                    }
                });
                   // holder.tvAction.setTooltipText("Click here to add details of persons met");
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        final PopupMenu popup = new PopupMenu(view.getContext(), holder.itemView);
                        popup.getMenuInflater()
                                .inflate(R.menu.menu_vis_list, popup.getMenu());
                        if(status==1)//Arrived
                        {
                            popup.getMenu().getItem(0).setVisible(false);
                        }

                        popup.show();
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                if (menuItem.getTitle() == popup.getMenu().getItem(1).getTitle()) {
                                showVisitsInMap(holder);
                                }
                                if (menuItem.getTitle() == popup.getMenu().getItem(0).getTitle()) {
                                    VisitMeetingList.curVisUUID = dataSet.get(holder.position).getRecUUID();
                                    VisitMeetingList.visLog = dataSet.get(holder.position);
                                    holder.mainView.getContext().startActivity(new Intent(holder.mainView.getContext(), VisitMeetingList.class));
                                }
                                return false;
                            }
                        });

                    }catch(Exception exp){

                    }
                }
            });
            holder.tvLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                showVisitsInMap(holder);
                }
            });
//            holder.tvNameOfOffice.setText(dataSet.get(listPosition).getPlaceName());
//            holder.tvTimeIn.setText(dataSet.get(listPosition).getLogHour()+":"+dataSet.get(listPosition).getLogMinute());
//            holder.tvTimeOut.setText("NA");
//            holder.tvTimeWorked.setText(dataSet.get(listPosition).getPurpose());
        }




    }

    void showVisitsInMap(final  RawTypeViewHolder holder)
    {
        CommonDataArea.markers= new ArrayList<MarkerToDraw>();
        String selectedUser = holder.tvName.getText().toString();
        String selectedVisDate = holder.tvDate.getText().toString();
        for(int i=0;i<dataSet.size();++i) {
            if(!selectedUser.contains(dataSet.get(i).getUserName())) continue;;
            String curVisDateTime = dataSet.get(i).getLogDayOfMonth()+"/"+dataSet.get(i).getLogMonth()+"/"+dataSet.get(i).getLogYear();
            if(!selectedVisDate.contains(curVisDateTime)) continue;;
            MarkerToDraw marker = new MarkerToDraw();
            int arrived = dataSet.get(i).getArrivedDeparted();
            String timeStr = (dataSet.get(i).getLogHour() + ":" + dataSet.get(i).getLogMinute());
            location = dataSet.get(i).getPlaceName();
            Double lati = dataSet.get(i).getLati();
            Double longi = dataSet.get(i).getLongi();

            MarkerOptions markerOptions = new MarkerOptions();
            CommonDataArea.polylineOptions = new PolylineOptions();
            //  ArrayList<LatLng> arraylistLAti = new ArrayList<LatLng>();
            CommonDataArea.arraylistLAti.add(new LatLng(lati,longi));
            // CommonDataArea.polylineOptions.color(Color.RED);
            // CommonDataArea.polylineOptions.width(5);

            Location placeLoc = new Location("FromDB");
            placeLoc.setLatitude(lati);
            placeLoc.setLongitude(longi);


            String toolTip;
            if (arrived == 1) {
                marker.arrived = true;
                toolTip = "Arrived at " + timeStr + " " + location;
            } else
            {
                marker.arrived = false;
                toolTip = "Departed On" + timeStr + " " + location;
            }


            marker.placeName = toolTip;
            marker.placeLoc = placeLoc;
            if(i==holder.position) marker.centerMarker =true;
            CommonDataArea.markers.add(marker);
        }
        if(CommonDataArea.markers.size()>1) {
            int size = CommonDataArea.markers.size();
            CommonDataArea.markers.get(0).first = true;
            CommonDataArea.markers.get(size-1).last=true;
        }
        Intent intent = new Intent("MO_VIS_DISP");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

    }
    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class RawTypeViewHolder extends RecyclerView.ViewHolder  {

       TextView tvDate,tvName,tvBiz,tvLocation,tvTime,tvAction,tvPurpose;
       int position;
       View mainView;
//       CardView cardView;


        public RawTypeViewHolder(View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvName=(TextView)itemView.findViewById(R.id.tvName);
            tvBiz = (TextView)itemView.findViewById(R.id.tvBiz);
            tvLocation=(TextView)itemView.findViewById(R.id.tvLocation);
            tvTime=(TextView)itemView.findViewById(R.id.tvTime);
            tvAction=(TextView)itemView.findViewById(R.id.tvAction);
            tvPurpose = (TextView)itemView.findViewById(R.id.tvPurpose);
//            cardView=(CardView) itemView.findViewById(R.id.cardview);
            mainView=itemView;

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
//}
