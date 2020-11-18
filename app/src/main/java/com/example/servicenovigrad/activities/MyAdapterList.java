package com.example.servicenovigrad.activities;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.services.Service;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MyAdapterList extends ArrayAdapter<String> {
    Activity context;
    List<String> servicesName;
    List<String> forms;
    List<String> docs;

    public MyAdapterList(@NonNull Activity context, List<String> services,List<String> forms,List<String> docs) {
        super(context,R.layout.activity_list_service,services);
        this.context=context;
        this.servicesName = services;
        this.forms = forms;
        this.docs = docs ;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View viewRow = inflater.inflate(R.layout.activity_list_service,null,true);

        TextView name = viewRow.findViewById(R.id.serviceName);
        TextView forms = viewRow.findViewById(R.id.forns);
        TextView docs = viewRow.findViewById(R.id.docs);

        name.setText(servicesName.get(position));
        forms.setText(this.forms.get(position));
        docs.setText(this.docs.get(position));

        return viewRow;

    }
}
