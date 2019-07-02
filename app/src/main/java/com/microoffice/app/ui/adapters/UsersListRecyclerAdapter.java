package com.microoffice.app.ui.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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
import com.microoffice.app.ui.fragments.UsersListFragment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import moffice.meta.com.molibrary.core.MOMain;
import moffice.meta.com.molibrary.core.MOUserManager;
import moffice.meta.com.molibrary.models.dbmodels.UserList;
import moffice.meta.com.molibrary.utility.CommonDataArea;


public class UsersListRecyclerAdapter extends RecyclerView.Adapter<UsersListRecyclerAdapter.ViewHolder>
        implements Filterable {

    Context context;
    List<UserList> mUsersList;
    List<UserList> mFilterUsersList;
    public RecyclerView parentView;
    private ContactFilter contactFilter;
    public UsersListFragment fragment;

    public UsersListRecyclerAdapter(Context context, List<UserList> mUsersList) {
        this.context = context;
        this.mUsersList = mUsersList;
        this.mFilterUsersList = mUsersList;
    }

    /**
     * This is called every time when we need a new ViewHolder and a new ViewHolder is required for every item in RecyclerView.
     * Then this ViewHolder is passed to onBindViewHolder to display items.
     */

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemLayout = layoutInflater.inflate(R.layout.list_item_users, null);
        return new ViewHolder(itemLayout, viewType, context);
    }

    /**
     * This method is called by RecyclerView.Adapter to display the data at the specified position. 
     * This method should update the contents of the itemView to reflect the item at the given position.
     * So here , if position!=0 it implies its a list_item_alphabet_row and we set the title and icon of the view.
     */

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tvContactName.setText(mFilterUsersList.get(position).getName());
        holder.tvContactNo.setText(mFilterUsersList.get(position).getMobile());
        holder.userUUID = mFilterUsersList.get(position).getUserGUID();
        holder.position = position;
        if (mFilterUsersList.get(position).getStatus() == MOMain.MO_DBUSERLIST_INVITED) {
            holder.tvStatus.setText("Pending");
            holder.tvStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (CommonDataArea.getInstType() == MOMain.MO_INSTTYPE_ADMIN) {
                            LayoutInflater factory = LayoutInflater.from(context);
                            final View customView = factory.inflate(R.layout.custom_popup, null);
                            final AlertDialog alertDialogBuilder = new AlertDialog.Builder(context).create();
                            alertDialogBuilder.setView(customView);
                            final Button btnDesg = (Button) customView.findViewById(R.id.desg);
                            Button btnDelete = (Button) customView.findViewById(R.id.delete);
                            final Button btnCancel = (Button) customView.findViewById(R.id.cancel);
                            alertDialogBuilder.setTitle("Actions");
                            btnDesg.setVisibility(View.INVISIBLE);
                            btnDesg.setText("Set as Admin");

                            btnDelete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteAlert(holder.userUUID,position);
                                    alertDialogBuilder.dismiss();

                                }
                            });
                            btnCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(holder.itemView.getContext(), "Cancelled", Toast.LENGTH_LONG).show();
                                    alertDialogBuilder.dismiss();
                                }
                            });

                            alertDialogBuilder.show();

                        }
                    } catch (Exception exp) {

                    }
                }

            });
        }
        else if (mFilterUsersList.get(position).getStatus() == MOMain.MO_DBUSERLIST_ASSOS_REGISTERED) {
            holder.tvStatus.setText("Registered");
            holder.tvStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (CommonDataArea.getInstType() == MOMain.MO_INSTTYPE_ADMIN) {
                            LayoutInflater factory = LayoutInflater.from(context);
                            final View customView = factory.inflate(R.layout.custom_popup, null);
                            final AlertDialog alertDialogBuilder = new AlertDialog.Builder(context).create();
                            alertDialogBuilder.setView(customView);
                            final Button btnDesg = (Button) customView.findViewById(R.id.desg);
                            Button btnDelete = (Button) customView.findViewById(R.id.delete);
                            final Button btnCancel = (Button) customView.findViewById(R.id.cancel);
                            alertDialogBuilder.setTitle("Actions");
                            btnDesg.setText("Set as Admin");
                            //   alertDialogBuilder.setMessage("Do you want to set this user as Admin");
                            btnDesg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mFilterUsersList.get(position).setStatus(MOMain.MO_DBUSERLIST_ASSOS_REGISTERED_ADMIN);
                                    MOUserManager.setUserAsAdmin(holder.itemView.getContext(), holder.userUUID, true);
                                    MOUserManager.sendAdminEnabled(holder.itemView.getContext(), holder.userUUID, true, true);
                                    Toast.makeText(holder.itemView.getContext(), holder.tvContactName.getText().toString() + " Marked as Admin", Toast.LENGTH_LONG).show();
                                    alertDialogBuilder.dismiss();
                                }
                            });
                            btnDelete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteAlert(holder.userUUID,position);
                                    alertDialogBuilder.dismiss();
                                }
                            });
                            btnCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(holder.itemView.getContext(), "Cancelled", Toast.LENGTH_LONG).show();
                                    alertDialogBuilder.dismiss();
                                }
                            });


                            alertDialogBuilder.show();


                        }
                    } catch (Exception exp) {

                    }
                }

            });
        } else if (mFilterUsersList.get(position).getStatus() == MOMain.MO_DBUSERLIST_ASSOS_REGISTERED_ADMIN) {
            holder.tvStatus.setText("Admin");
            holder.tvStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (CommonDataArea.getInstType() == MOMain.MO_INSTTYPE_ADMIN) {


                            LayoutInflater factory = LayoutInflater.from(context);
                            final View customView = factory.inflate(R.layout.custom_popup, null);
                            final AlertDialog alertDialogBuilder = new AlertDialog.Builder(context).create();
                            alertDialogBuilder.setView(customView);
                            final Button btnDesg = (Button) customView.findViewById(R.id.desg);
                            Button btnDelete = (Button) customView.findViewById(R.id.delete);
                            final Button btnCancel = (Button) customView.findViewById(R.id.cancel);
                            alertDialogBuilder.setTitle("Actions");
                            btnDesg.setText("Set as Employee");
//                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(holder.itemView.getContext());
                            //       alertDialogBuilder.setMessage("Do you want to set this user as Admin");
                            btnDesg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mFilterUsersList.get(position).setStatus(MOMain.MO_DBUSERLIST_ASSOS_REGISTERED);
                                    MOUserManager.setUserAsAdmin(holder.itemView.getContext(), holder.userUUID, false);
                                    MOUserManager.sendAdminEnabled(holder.itemView.getContext(), holder.userUUID, false, true);
                                    Toast.makeText(holder.itemView.getContext(), holder.tvContactName.getText().toString() + " Marked as Employee", Toast.LENGTH_LONG).show();
                                    alertDialogBuilder.dismiss();
                                    notifyDataSetChanged();
                                }
                            });
                            btnDelete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleteAlert(holder.userUUID,position);
                                    alertDialogBuilder.dismiss();
                                }
                            });
                            btnCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(holder.itemView.getContext(), "Cancelled", Toast.LENGTH_LONG).show();
                                    alertDialogBuilder.dismiss();
                                }
                            });

                            alertDialogBuilder.show();


                        }
                    } catch (Exception exp) {

                    }
                }
            });
        }
    }

    /**
     * It returns the total no. of items . We +1 count to include the header view.
     * So , it the total count is 5 , the method returns 6.
     * This 6 implies that there are 5 row_items and 1 header view with header at position zero.
     */
    @Override
    public int getItemCount() {
        return mFilterUsersList.size();
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
            contactFilter = new ContactFilter(this, mUsersList);
        return contactFilter;
    }

    /**
     * Create custom view here.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvContactName, tvContactNo;
        TextView tvStatus;
        String userUUID;
        Button btInvite;
        int position;

        public ViewHolder(View drawerItem, int itemType, final Context context) {
            super(drawerItem);

            tvContactName = (TextView) itemView.findViewById(R.id.tvContactName);
            tvContactNo = (TextView) itemView.findViewById(R.id.tvContactNo);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
        }
    }

    public void updateList(List<UserList> contactsArrayList) {
        this.mFilterUsersList = contactsArrayList;
        notifyDataSetChanged();
    }

    private static class ContactFilter extends Filter {

        private final UsersListRecyclerAdapter adapter;

        private final List<UserList> originalList;
        private final List<UserList> filteredList;

        private ContactFilter(UsersListRecyclerAdapter adapter, List<UserList> originalList) {
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

                for (final UserList user : originalList) {
                    if (user.getName().toLowerCase().contains(filterPattern)) {
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
            adapter.mFilterUsersList.clear();
            adapter.mFilterUsersList.addAll((List<UserList>) results.values);
            adapter.notifyDataSetChanged();
        }
    }

    public void deleteAlert(final String UUIDDelete,final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Delete");
        builder.setMessage("Do you want to delete this person?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                try {
                    MOUserManager.deleteUser(UUIDDelete, context);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                dialog.dismiss();
                fragment.reLoadList();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }
}
