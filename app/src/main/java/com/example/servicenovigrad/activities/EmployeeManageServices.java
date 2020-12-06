package com.example.servicenovigrad.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EmployeeManageServices extends AppCompatActivity {

    ListView availableService;
    List<String> servicesName;
    List<String> forms;
    List<String> docs;
    FbWrapper fb = FbWrapper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_adding_service);
        availableService = findViewById(R.id.employeeListView);

        servicesName = new ArrayList<>();
        forms = new ArrayList<>();
        docs = new ArrayList<>();

        availableService.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String serviceName = servicesName.get(position);
                showDialogPage(serviceName);
                return true;
            }
        });

        loadServiceFromFireBase();



    }

    private void loadServiceFromFireBase() {

        fb.getCollection("services").addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    servicesName.clear();
                    forms.clear();
                    docs.clear();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String name = (String) doc.get("serviceName");
                        List<String> myForms = (List<String>) doc.get("formFields");
                        List<String> myDocs = (List<String>) doc.get("documentsNames");
                        Service myService = new Service(name, myForms, myDocs);
                        servicesName.add(name);
                        forms.add(myService.getFormsFieldsInString());
                        docs.add(myService.getDocsFieldsInString());

                        MyAdapterList adapter = new MyAdapterList(EmployeeManageServices.this,
                                servicesName,forms,docs);
                        availableService.setAdapter(adapter);

                    }
                } else
                    Log.d("SetService", "error getting document: ", task.getException());
            }
        });

    }

    private void showDialogPage (final String serviceName) {

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.employee_adding_service_dialog, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle(serviceName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        final Button yes = dialogView.findViewById(R.id.Yes);
        final Button no = dialogView.findViewById(R.id.No);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Add this service to the succursale

                fb.getDocument("users/" + fb.getUserUid()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            final DocumentReference succursaleRef = (DocumentReference) task.getResult().get("succursale");


                            //If employee succursale reference is "template" he should create one to continue
                            if (succursaleRef.equals(fb.getDocumentRef("succursales/template"))) {

                                Toast.makeText(getApplicationContext(), "Vous devez d'abord creer une succursale", Toast.LENGTH_SHORT).show();
                                b.dismiss();

                            } else {


                                succursaleRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            List<DocumentReference> services = (List<DocumentReference>) task.getResult().get("services");
                                            services.add(fb.getDocumentRef("services/" + Service.toCamelCase(serviceName)));
                                            succursaleRef.update("services", services).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getApplicationContext(), "Service ajouté", Toast.LENGTH_SHORT).show();
                                                        Log.d("SAVE NEW SERVICE", "successul");
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), "Service non ajouté", Toast.LENGTH_SHORT).show();
                                                        Log.d("SAVE NEW SERVICE", "fail");
                                                    }
                                                }
                                            });
                                            b.dismiss();
                                        }
                                    }
                                });




                            }


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