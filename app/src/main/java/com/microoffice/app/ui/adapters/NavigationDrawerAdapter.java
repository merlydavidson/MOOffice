package com.microoffice.app.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.microoffice.app.R;


import java.util.Collections;
import java.util.List;

import moffice.meta.com.molibrary.models.NavDrawerItem;


public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);
        holder.tvNavTitle.setText(current.getTitle());

        if(position == 4){
            holder.viewDivider.setVisibility(View.VISIBLE);
        }else{
            holder.viewDivider.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvNavTitle;
        View viewDivider;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvNavTitle = (TextView) itemView.findViewById(R.id.tvNavTitle);
            viewDivider = (View) itemView.findViewById(R.id.viewDivider);
        }
    }
}
