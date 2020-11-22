package com.example.servicenovigrad.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.fb.FbWrapper;
import com.example.servicenovigrad.services.Service;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetServiceActivity extends AppCompatActivity {
    ListView listViewServices;
    List<String> servicesName;
    List<String> forms;
    List<String> docs;
    FbWrapper fb = FbWrapper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_service);
        listViewServices = (ListView) findViewById(R.id.listview);

        servicesName = new ArrayList<>();
        forms = new ArrayList<>();
        docs = new ArrayList<>();

        listViewServices.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String serviceName = servicesName.get(position);
                showDialogUpdate(serviceName);
                loadFromFireBase();
                return true;
            }
        });

        loadFromFireBase();



    }

    public void loadFromFireBase() {

        fb.getCollection("services").addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    servicesName.clear();
                    docs.clear();
                    forms.clear();
                    for(QueryDocumentSnapshot doc : task.getResult()) {
                        String name = (String) doc.get("serviceName");
                        List<String> myForms = (List<String>) doc.get("formFields");
                        List<String> myDocs = (List<String>) doc.get("documentsNames");
                        Service myService = new Service(name,myForms,myDocs);
                        servicesName.add(myService.getServiceName());
                        forms.add(myService.getFormsFieldsInString());
                        docs.add(myService.getDocsFieldsInString());

                        Log.d("SetService ","getting document is sucessfull " + "=>" + name);
                    }
                    MyAdapterList adapter = new MyAdapterList (SetServiceActivity.this,servicesName,forms,docs);
                    listViewServices.setAdapter(adapter);

                }

                else
                    Log.d("SetService","error getting document: ",task.getException());
            }
        });

    }

    private void update (String oldName, String name , String forms, String docs) {

        List<String> myForms = new ArrayList<>();
        for ( String info : forms.split(",")) {
            myForms.add(info.trim());
        }
        List<String> myDocs = new ArrayList<>();

        for(String info : docs.split(",")) {
            myDocs.add(info.trim());
        }
        Map newData = new HashMap();
        newData.put("serviceName",name);
        newData.put("formFields", myForms);
        newData.put("documentsNames",myDocs);

        fb.deleteDocument("services/"+Service.toCamelCase(oldName));
        fb.setDocument("services/" + Service.toCamelCase(name),newData);
        loadFromFireBase();

        Toast.makeText(this,"Service mis à jour",Toast.LENGTH_SHORT).show();
    }

    private void delete (String name) {
        fb.deleteDocument("services/" + Service.toCamelCase(name));
       loadFromFireBase();
        Toast.makeText(this,"Service supprimé",Toast.LENGTH_SHORT).show();
    }

    private void showDialogUpdate (final String serviceName) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.set_service_dialog,null);
        dialogBuilder.setView(dialogView);

        final EditText newName = (EditText) dialogView.findViewById(R.id.newName);
        final EditText newForms = (EditText) dialogView.findViewById(R.id.newForm);
        final EditText newDocs = (EditText) dialogView.findViewById(R.id.newDoc);
        final Button update = (Button) dialogView.findViewById(R.id.modifier_1);
        final Button delete = (Button) dialogView.findViewById(R.id.delete_1);
        final Button cancel = (Button) dialogView.findViewById(R.id.cancel);
        final TextView error = (TextView) dialogView.findViewById(R.id.error);

        dialogBuilder.setTitle(serviceName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = newName.getText().toString().trim();
                String forms = newForms.getText().toString().trim();
                String docs = newDocs.getText().toString().trim();
                if (!TextUtils.isEmpty(name) & !TextUtils.isEmpty(forms) & !TextUtils.isEmpty(docs)) {
                    update(serviceName,name,forms,docs);
                    b.dismiss();
                }
                else {
                    error.setError("Vous devez remplir tous les champs");
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(serviceName);
                b.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
    }
}