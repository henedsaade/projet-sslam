package com.example.servicenovigrad.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.fb.FbWrapper;
import com.example.servicenovigrad.services.Service;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EmployeeManageServices extends AppCompatActivity {

    ListView availableService;
    List<String> services;
    FbWrapper fb = FbWrapper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_adding_service);
        availableService = findViewById(R.id.employeeListView);

        services = new ArrayList<>();

        fb.getCollection("services").addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    services.clear();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String name = (String) doc.get("serviceName");
                        List<String> myForms = (List<String>) doc.get("formFields");
                        List<String> myDocs = (List<String>) doc.get("documentsNames");
                        Service myService = new Service(name, myForms, myDocs);
                        services.add(name);

                        ListAdapter adapter = new ArrayAdapter<>(EmployeeManageServices.this,
                                R.layout.activity_list_service, services);
                        availableService.setAdapter(adapter);

                    }
                } else
                    Log.d("SetService", "error getting document: ", task.getException());
            }
        });
    }

    private void showDialogPage (final String serviceName) {

        final String userId  = FirebaseAuth.getInstance().getCurrentUser().getUid();

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.employee_adding_service_dialog,null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle(serviceName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        final Button yes = dialogView.findViewById(R.id.Yes);
        final Button no =  dialogView.findViewById(R.id.No);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TO DO: Implements method to save service in the succursale services

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
    }

}