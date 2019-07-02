package com.microoffice.app.ui.adapters;

import android.content.Context;
import android.support.v7.app.AlertDialog;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import moffice.meta.com.molibrary.core.MOMain;
import moffice.meta.com.molibrary.core.MOVisitManager;
import moffice.meta.com.molibrary.models.dbmodels.VisitMeetLog;
import moffice.meta.com.molibrary.utility.CommonDataArea;


public class MeetingListRecyclerAdapter extends RecyclerView.Adapter<MeetingListRecyclerAdapter.ViewHolder> {

    Context context;


    List<VisitMeetLog> data;
    LayoutInflater inflater = null;

    public MeetingListRecyclerAdapter(Context context, List<VisitMeetLog> dataList) {
        data =dataList;
        this.context = context;
    }

    /**
     * This is called every time when we need a new ViewHolder and a new ViewHolder is required for every item in RecyclerView.
     * Then this ViewHolder is passed to onBindViewHolder to display items.
     */

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View itemLayout = layoutInflater.inflate(R.layout.list_item_meeting_list_data, null);
        itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(view.getContext(), "Test", Toast.LENGTH_SHORT).show();
                PopupMenu popup = new PopupMenu(view.getContext(), itemLayout);
                popup.getMenuInflater()
                        .inflate(R.menu.menu_vis_meet, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        return false;
                    }
                });
            }
        });
        return new ViewHolder(itemLayout, viewType, context);
    }

    /**
     * This method is called by RecyclerView.Adapter to display the data at the specified position. 
     * This method should update the contents of the itemView to reflect the item at the given position.
     * So here , if position!=0 it implies its a list_item_alphabet_row and we set the title and icon of the view.
     */

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.name.setText(data.get(position).getPersonMet());
        holder.title.setText(data.get(position).getTitle());
        holder.iPosition=position;

        String dateStr = getDate(data.get(position).getDateTimeStamp(),"dd-MM-yyyy:HH:mm");
        holder.date.setText(dateStr);
        holder.nextVisDate.setText(data.get(position).getNextVisDate());
        if(data.get(position).getStatus()==1) //if status reviewed set back ground as green
        {
            holder.itemView.setBackgroundColor(holder.itemView.getResources().getColor(R.color.color6ab344));
        }
        if(data.get(position).getStatus()==2) //if status deleted  set back ground as red
        {
            holder.itemView.setBackgroundColor(holder.itemView.getResources().getColor(R.color.coloredf1f4));
        }
        //holder.date.setText(data.get(position).getDateTime());
        String note = data.get(position).getNote();

        if(note.length()>5){
            holder.noteFull = note;
            note = note.substring(0,5)+"....";

            SpannableString content = new SpannableString(note);
            content.setSpan(new UnderlineSpan(), 0, note.length(), 0);
            holder.note.setText(content);
            holder.note.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(holder.note.getContext());
                    alertDialogBuilder.setMessage(holder.noteFull);
                    alertDialogBuilder.setPositiveButton("OK",null);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }
            });
        }else
        holder.note.setText(data.get(position).getNote());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final PopupMenu popup = new PopupMenu(view.getContext(), holder.itemView);
                    popup.getMenuInflater()
                            .inflate(R.menu.menu_vis_meet, popup.getMenu());
                    popup.getMenu().getItem(0).setVisible(true);   //ViewNote
                    popup.getMenu().getItem(1).setVisible(false);  //MarkAsReviewed
                    popup.getMenu().getItem(2).setVisible(false);  //edit
                    popup.getMenu().getItem(3).setVisible(false);  //delete
                    if ((CommonDataArea.getInstType() == MOMain.MO_INSTTYPE_ADMIN) || (CommonDataArea.getInstType() == MOMain.MO_INSTTYPE_ADMIN)){
                        if(data.get(holder.iPosition).getStatus()==0){
                            popup.getMenu().getItem(1).setVisible(true);
                        }
                    }
                    if(data.get(holder.iPosition).getStatus()==0){
                        //popup.getMenu().getItem(2).setVisible(true);
                        popup.getMenu().getItem(3).setVisible(true);
                    }
                    popup.show();
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            //If menu selected is view note
                            if (menuItem.getTitle() == popup.getMenu().getItem(0).getTitle()) {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(holder.note.getContext());
                                    alertDialogBuilder.setMessage(data.get(holder.iPosition).getNote());
                                    alertDialogBuilder.setPositiveButton("OK", null);
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                            }
                            //If menu selected is Mark As Reviewed
                            if (menuItem.getTitle() == popup.getMenu().getItem(1).getTitle()) {
                                if(MOVisitManager.setMeetingInfoReviewed(holder.itemView.getContext(),data.get(holder.iPosition))) {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(holder.note.getContext());
                                    alertDialogBuilder.setMessage("Meeting Marked as reviewed");
                                    alertDialogBuilder.setPositiveButton("OK", null);
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                    data.get(holder.iPosition).setStatus(1);//set status as reviewed
                                    MeetingListRecyclerAdapter.this.notifyDataSetChanged();
                                }else{
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(holder.note.getContext());
                                    alertDialogBuilder.setMessage("Failed to mark as reviewed");
                                    alertDialogBuilder.setPositiveButton("OK", null);
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            }
                            //if menu selected is deleted
                            if (menuItem.getTitle() == popup.getMenu().getItem(3).getTitle()) {
                                if(MOVisitManager.setMeetingDeleted(holder.itemView.getContext(),data.get(holder.iPosition))) {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(holder.note.getContext());
                                    alertDialogBuilder.setMessage("Meeting Deleted");
                                    alertDialogBuilder.setPositiveButton("OK", null);
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                    data.get(holder.iPosition).setStatus(2);//set status as reviewed
                                    MeetingListRecyclerAdapter.this.notifyDataSetChanged();
                                }else{
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(holder.note.getContext());
                                    alertDialogBuilder.setMessage("Failed to delete metting");
                                    alertDialogBuilder.setPositiveButton("OK", null);
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            }

                            return true;
                        }

                    });
                }catch(Exception exp){

                }
            }
            });
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
    /**
     * It returns the total no. of items . We +1 count to include the header view.
     * So , it the total count is 5 , the method returns 6.
     * This 6 implies that there are 5 row_items and 1 header view with header at position zero.
     */
    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * This methods returns 0 if the position of the item is '0'.
     * If the position is zero its a header view and if its anything else
     * its a list_item_alphabet_row with a title and icon.
     */

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    /**
     * Create custom view here.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView title;
        TextView date;
        TextView nextVisDate;
        TextView note;
        String noteFull;
        int iPosition;

        public ViewHolder(View drawerItem, int itemType, final Context context) {
            super(drawerItem);

            name = (TextView) itemView.findViewById(R.id.mlName);
            title = (TextView) itemView.findViewById(R.id.mlTitle);

            date = (TextView) itemView.findViewById(R.id.mlDate);

            note = (TextView)itemView.findViewById(R.id.mlNote);
            nextVisDate = (TextView)itemView.findViewById(R.id.mlNextVisDate);
        }
    }


}
