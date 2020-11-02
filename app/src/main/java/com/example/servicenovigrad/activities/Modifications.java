package com.example.servicenovigrad.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.fb.FbWrapper;
import com.example.servicenovigrad.services.Service;

import java.util.ArrayList;
import java.util.HashMap;

public class Modifications extends AppCompatActivity {
    private EditText forms;
    private EditText docs;
    private Button finish;
    protected static final String firestoreServicesRoute = "services/";

    private ArrayList<String> formsfield;
    private ArrayList <String>docsField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifications);

        forms = (EditText) findViewById(R.id.forms);
        docs = (EditText) findViewById (R.id.docs);
        finish = (Button) findViewById (R.id.modifier);

        formsfield = new ArrayList <> ();
        docsField = new ArrayList <> ();

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setForms();
                setDocs();

                HashMap <String,Object> dataToSave =  new HashMap<>();
                dataToSave.put("formsField",formsfield);
                dataToSave.put("documentsNames",docsField);
                dataToSave.put("serviceName",SetServiceActivity.serviceName);

                FbWrapper.getInstance().setDocument(firestoreServicesRoute + Service.toCamelCase(SetServiceActivity.serviceName),dataToSave);
                Toast.makeText(getApplicationContext(),"Service modifié",Toast.LENGTH_SHORT).show();

                openSetService();
            }
        });

    }

    private void setForms(){
        String [] form = forms.getText().toString().split(",");
        for(int i = 0; i<form.length; i++){
            formsfield.add(form[i]);
        }
    }

    private void setDocs() {
        String [] doc = docs.getText().toString().split(",");
        for (int i =0 ; i<doc.length; i++) {
            docsField.add(doc[i]);
        }
    }

    private void openSetService() {
        Intent intent = new Intent(this,SetServiceActivity.class);
        startActivity(intent);
    }
}