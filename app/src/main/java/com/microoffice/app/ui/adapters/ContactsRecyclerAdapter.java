package com.microoffice.app.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.microoffice.app.R;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import moffice.meta.com.molibrary.core.MOMain;
import moffice.meta.com.molibrary.core.MOUserManager;
import moffice.meta.com.molibrary.models.Contacts;


public class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactsRecyclerAdapter.ViewHolder>
        implements Filterable {

    Context context;
    List<Contacts> contactsArrayList;
    List<Contacts> filteredContactsList;
    List<String> mInvitedContacts;
    private ContactFilter contactFilter;

    public ContactsRecyclerAdapter(Context context, List<Contacts> contactsArrayList, List<String> mInvitedContacts) {
        this.filteredContactsList = contactsArrayList;
        this.contactsArrayList = contactsArrayList;
        this.mInvitedContacts = mInvitedContacts;
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

        holder.tvContactName.setText(filteredContactsList.get(position).getStrName());
        holder.tvContactNo.setText(filteredContactsList.get(position).getStrContactNo());

        if(mInvitedContacts.contains(filteredContactsList.get(position).getStrContactNo())){
            holder.btInvite.setText("Invited");
            holder.btInvite.setTag("Invited");
            holder.btInvite.setBackgroundColor(context.getResources().getColor(R.color.color6ab344));
        }else{
            holder.btInvite.setText("Invite");
            holder.btInvite.setTag("Invite");
            holder.btInvite.setBackgroundColor(context.getResources().getColor(R.color.colorprimary));
        }
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

    @Override
    public Filter getFilter() {
        if (contactFilter == null)
            contactFilter = new ContactFilter(this, contactsArrayList);
        return contactFilter;
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
            btInvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(view.getTag().equals("Invite")) {
                        String strName = filteredContactsList.get(getAdapterPosition()).getStrName();
                        String strMobileNo = filteredContactsList.get(getAdapterPosition()).getStrContactNo();
                        int userLic = MOMain.getUserLicense();
                        if(userLic>0) {
                            MOUserManager.inviteAssociate(context, strName, strMobileNo);
                            Toast.makeText(context, "User invited successfully.", Toast.LENGTH_SHORT).show();
                            btInvite.setText("Invited");
                            view.setBackgroundColor(context.getResources().getColor(R.color.color6ab344));
                            --userLic;
                            MOMain.setUserLicense(userLic,"bmjo");
                        }else{
                            Toast.makeText(context, "User License not available. You can buy more licence. Goto Main Menu", Toast.LENGTH_SHORT).show();
                        }

                    }
                    if(view.getTag().equals("Invited")) {
                        String strName = filteredContactsList.get(getAdapterPosition()).getStrName();
                        String strMobileNo = filteredContactsList.get(getAdapterPosition()).getStrContactNo();

                        MOUserManager.inviteAssociate(context, strName, strMobileNo);

                        Toast.makeText(context,"User invitation resend successfully.",Toast.LENGTH_SHORT).show();
                        btInvite.setText("Invited");
                        view.setBackgroundColor(context.getResources().getColor(R.color.color6ab344));
                    }
                }
            });
        }
    }

    public void updateList(List<Contacts> contactsArrayList) {
        this.filteredContactsList = contactsArrayList;
        notifyDataSetChanged();
    }

    private static class ContactFilter extends Filter {

        private final ContactsRecyclerAdapter adapter;

        private final List<Contacts> originalList;
        private final List<Contacts> filteredList;

        private ContactFilter(ContactsRecyclerAdapter adapter, List<Contacts> originalList) {
            super();
            this.adapter = adapter;
            this.originalList = new LinkedList<>(originalList);
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final Contacts user : originalList) {
                    if (user.getStrName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(user);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.filteredContactsList.clear();
            adapter.filteredContactsList.addAll((List<Contacts>) results.values);
            adapter.notifyDataSetChanged();
        }
    }
}
