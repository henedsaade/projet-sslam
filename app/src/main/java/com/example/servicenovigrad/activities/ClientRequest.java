package com.example.servicenovigrad.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.servicenovigrad.Adapter.HoursAdapter;
import com.example.servicenovigrad.R;
import com.example.servicenovigrad.fb.FbWrapper;
import com.example.servicenovigrad.services.Document;
import com.example.servicenovigrad.services.Service;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientRequest extends AppCompatActivity {

    TextView number;
    ListView  services;

    List<String>  serviceAvailable;
    HoursAdapter servicesAdapter;
    FbWrapper fb = FbWrapper.getInstance();
    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_request);

        serviceAvailable = new ArrayList<>();
        number = findViewById(R.id.succ_number_2);
        services = findViewById(R.id.succ_services_2);



        servicesAdapter = new HoursAdapter(this,serviceAvailable );
        services.setAdapter(servicesAdapter);



        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            id = extras.getString("SuccursaleId");
        }
        final String finalId = id;

        services.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ClientInformations.class);
                String n = serviceAvailable.get(position);
                intent.putExtra("service", Service.toCamelCase(n));
                intent.putExtra("succursale",finalId);
                startActivity(intent);
            }
        });


        fb.getDocument("succursales/"+id).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    String number_database = doc.getString("number");
                    Map hours = (Map) doc.get("workHours");
                    List<DocumentReference> myServices = (List<DocumentReference>) doc.get("services");
                   // Log.d("Number",number_database);
                    Log.d("Id", "succursales/"+ finalId);



                    for(DocumentReference ref : myServices) {
                        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()) {
                                    DocumentSnapshot ser = task.getResult();

                                    String name = ser.getString("serviceName");

                                    serviceAvailable.add(name);
                                    servicesAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }







                    number.setText("Numero de succursale: " + number_database);


                }
            }
        });

    }

}