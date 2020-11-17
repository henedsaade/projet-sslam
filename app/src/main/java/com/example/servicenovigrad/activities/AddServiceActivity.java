package com.example.servicenovigrad.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.services.Service;
import com.example.servicenovigrad.services.ServiceType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AddServiceActivity extends AppCompatActivity {

    private Button add;
    private EditText serviceType;
    private Service service;
    private List<String> formFields;
    private List <String> documentsNames;
    private String serviceName;
    private EditText documents;
    private EditText formulaire;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service2);
        add = (Button) findViewById(R.id.add);
        serviceType = (EditText) findViewById(R.id.serviceType);
        documents = (EditText) findViewById(R.id.documents);
        formulaire = (EditText) findViewById(R.id.formulaire);

        formFields = new ArrayList<>();

        documentsNames = new ArrayList<>();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((serviceType.getText().toString().length() > 1) & (formulaire.getText().toString().length() > 1) & (documents.getText().length() > 1)) {
                    serviceName = serviceType.getText().toString().trim();

                    String[] formsTab = formulaire.getText().toString().split(",");
                    for (int i = 0; i < formsTab.length; i++) {
                        formFields.add(formsTab[i].trim());
                    }
                    String[] documentsTab = documents.getText().toString().split(",");
                    for (int i = 0; i < documentsTab.length; i++) {
                        documentsNames.add(documentsTab[i].trim());
                    }
                    Toast.makeText(getApplicationContext(),"Le service a été ajouté",Toast.LENGTH_SHORT).show();
                    service = new Service(serviceName,formFields,documentsNames);
                    service.saveServiceBlueprint();
                    openOptionPage();
                }

                else {
                    error();
                }
            }
        });
    }

    private void openOptionPage () {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }
    
    private void error () {
        Toast.makeText(getApplicationContext(), "Vous devez remplir tous les champs", Toast.LENGTH_LONG).show();
    }
}
