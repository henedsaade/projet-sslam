package com.example.servicenovigrad.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.servicenovigrad.Adapter.MyAdapterList;
import com.example.servicenovigrad.R;
import com.example.servicenovigrad.fb.FbWrapper;
import com.example.servicenovigrad.services.Service;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SuccursaleAvailableServices extends AppCompatActivity {

    ListView availableService;
    List<String> servicesName;
    List<String> forms;
    List<String> docs;
    FbWrapper fb = FbWrapper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succursale_available_services);
        availableService = findViewById(R.id.succursaleServices);

        servicesName = new ArrayList<>();
        forms = new ArrayList<>();
        docs = new ArrayList<>();

        availableService.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String serviceName = servicesName.get(position);
                showDialogPage(serviceName);
                loadServiceFromFireBase();
                return true;
            }
        });

        loadServiceFromFireBase();



    }

    private void loadServiceFromFireBase() {

        fb.getDocument("succursales/"+fb.getUserUid()+"- succursale")
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            servicesName.clear();
                            forms.clear();
                            docs.clear();
                            List<DocumentReference> services = (List<DocumentReference>) task.getResult()
                                    .get("services");
                            for (DocumentReference ref : services) {
                                ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            String name = (String) task.getResult().get("serviceName");
                                            List<String> myForms = (List<String>) task.getResult().get("formFields");
                                            List<String> myDocs = (List<String>) task.getResult().get("documentsNames");
                                            Service myService = new Service(name, myForms, myDocs);
                                            servicesName.add(name);
                                            forms.add(myService.getFormsFieldsInString());
                                            docs.add(myService.getDocsFieldsInString());

                                            MyAdapterList adapter = new MyAdapterList(SuccursaleAvailableServices.this,
                                                    servicesName, forms, docs);
                                            availableService.setAdapter(adapter);
                                        } else {
                                            Log.d("SetService", "error getting document: ", task.getException());
                                        }


                                    }
                                });
                            }
                        }
                        else
                            startActivity(new Intent(getApplicationContext(),EmployeeOptions.class));

                    }
                });

    }


    private void showDialogPage (final String serviceName) {

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_service_dialog_page, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle(serviceName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        final Button yes = dialogView.findViewById(R.id.yes_1);
        final Button no = dialogView.findViewById(R.id.no_1);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Add this service to the succursale

                fb.getDocument("succursales/" + fb.getUserUid()+"- succursale")
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentReference> services = (List<DocumentReference>) task.getResult()
                                    .get("services");

                            services.remove(fb.getDocumentRef("services/"+Service.toCamelCase(serviceName)));
                            fb.getDocumentRef("succursales/" + fb.getUserUid()+"- succursale")
                                    .update("services",services).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "service supprimé", Toast.LENGTH_SHORT).show();
                                        //loadServiceFromFireBase();
                                        b.dismiss();

                                    }
                                }
                            });

                        }
                    }

                });

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