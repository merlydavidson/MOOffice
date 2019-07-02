package com.microoffice.app.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.microoffice.app.R;

import java.util.List;

/**
 * Created by com.moffice.com.microoffice.app on 03-08-2017.
 */

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    private Activity activity;
    private  List<String> data;
    public Resources res;
    LayoutInflater inflater;
    String strSpinnerPrompt;

    /*************  CustomSpinnerAdapter Constructor *****************/
    public CustomSpinnerAdapter(
            Activity activity,
            int textViewResourceId,
            List<String> objects,
            Resources resLocal,
            String strSpinnerPrompt
    )
    {
        super(activity, textViewResourceId, objects);

        /********** Take passed values **********/
        this.activity = activity;
        data     = objects;
        res      = resLocal;
        this.strSpinnerPrompt = strSpinnerPrompt;

        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.custom_spinner_row, parent, false);

        /***** Get each Model object from Arraylist ********/


        TextView tvTitle        = (TextView)row.findViewById(R.id.tvTitle);

//        if(position==0){
//
//            // Default selected Spinner item
//            tvTitle.setText(strSpinnerPrompt);
//        }
//        else {
            // Set values for spinner each row
            tvTitle.setText(data.get(position));
//        }

        return row;
    }
}
