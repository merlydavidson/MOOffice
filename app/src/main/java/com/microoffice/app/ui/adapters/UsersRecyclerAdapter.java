package com.microoffice.app.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.microoffice.app.R;


import java.util.List;

import moffice.meta.com.molibrary.models.dbmodels.UserList;


public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder> {

    Context context;
    List<UserList> contactsArrayList;
    List<UserList> filteredContactsList;

    public UsersRecyclerAdapter(Context context, List<UserList> contactsArrayList) {
        this.filteredContactsList = contactsArrayList;
        this.contactsArrayList = contactsArrayList;
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

        View itemLayout = layoutInflater.inflate(R.layout.list_item_contact, null);
        return new ViewHolder(itemLayout, viewType, context);
    }

    /**
     * This method is called by RecyclerView.Adapter to display the data at the specified position. 
     * This method should update the contents of the itemView to reflect the item at the given position.
     * So here , if position!=0 it implies its a list_item_alphabet_row and we set the title and icon of the view.
     */

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tvContactName.setText(filteredContactsList.get(position).getName());
        holder.tvContactNo.setText(filteredContactsList.get(position).getMobile());
        holder.btInvite.setVisibility(View.GONE);
    }

    /**
     * It returns the total no. of items . We +1 count to include the header view.
     * So , it the total count is 5 , the method returns 6.
     * This 6 implies that there are 5 row_items and 1 header view with header at position zero.
     */
    @Override
    public int getItemCount() {
        return filteredContactsList.size();
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

        TextView tvContactName, tvContactNo;
        Button btInvite;

        public ViewHolder(View drawerItem, int itemType, final Context context) {
            super(drawerItem);

            tvContactName = (TextView) itemView.findViewById(R.id.tvContactName);
            tvContactNo = (TextView) itemView.findViewById(R.id.tvContactNo);

            btInvite = (Button) itemView.findViewById(R.id.btInvite);

        }
    }


}
