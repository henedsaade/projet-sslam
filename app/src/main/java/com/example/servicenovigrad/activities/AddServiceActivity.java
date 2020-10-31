package com.example.servicenovigrad.activities;

import androidx.appcompat.app.AppCompatActivity;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service2);
        add = (Button) findViewById(R.id.add);
        serviceType = (EditText) findViewById(R.id.serviceType);

        formFields= new ArrayList<>();
        formFields.add("Nom");
        formFields.add("Prénom");
        formFields.add("Date de naissance");
        formFields.add("Adresse");

        documentsNames = new ArrayList<>();
        formFields.add("Preuve de domicile (Une image de relevé bancaire ou une facture d'électricité indiquant l'adresse)");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( serviceType.getText().toString().contains("conduire")){
                    addDriverLicense();
                    Toast.makeText(getApplicationContext(),"Le service a été ajouté",Toast.LENGTH_SHORT).show();
                }
                else if(serviceType.getText().toString().contains("sante") || serviceType.getText().toString().contains("santé")){
                    addHealthCard();
                    Toast.makeText(getApplicationContext(),"Le service a été ajouté",Toast.LENGTH_SHORT).show();
                }
                else if(serviceType.getText().toString().contains("identite") || serviceType.getText().toString().contains("identité")){
                    addID();
                    Toast.makeText(getApplicationContext(),"Le service a été ajouté",Toast.LENGTH_SHORT).show();
                }
                else {
                    error();
                }
            }
        });
    }

    public void addDriverLicense(){
        serviceName="Permis de conduire";
        formFields.add("Type de permis");
        Service service = new Service(serviceName,formFields,documentsNames);
        service.saveServiceBlueprint();

    }

    public void  addHealthCard() {
        serviceName= "Carte de santé";
        documentsNames.add("Preuve de statut (Image d'une carte de résident permanent ou d'un passeport canadien)");
        Service service = new Service(serviceName,formFields,documentsNames);
        service.saveServiceBlueprint();
    }

    public void addID(){
        serviceName="Pièce d'identité avec photo";
        documentsNames.add("Une photo");
        Service service = new Service(serviceName,formFields,documentsNames);
        service.saveServiceBlueprint();
    }
    private void error() {
        Toast.makeText(getApplicationContext(),"Choisir 'permis de conduire' ou 'carte de sante' ou 'pièce d'identité avec photo' ",Toast.LENGTH_SHORT).show();
    }
}
