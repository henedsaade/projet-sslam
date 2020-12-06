package com.example.servicenovigrad.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.servicenovigrad.R;

import java.util.List;

public class HoursAdapter extends ArrayAdapter <String> {
    Activity context;
    List<String> hoursList;

    public HoursAdapter (Activity context,List<String> hoursList) {
        super (context, R.layout.hour_view,hoursList);
        this.context = context;
        this.hoursList = hoursList;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View viewRow = inflater.inflate(R.layout.hour_view,null,true);

        TextView hours = viewRow.findViewById(R.id.hours);


        hours.setText(hoursList.get(position));

        return viewRow;

    }
}
