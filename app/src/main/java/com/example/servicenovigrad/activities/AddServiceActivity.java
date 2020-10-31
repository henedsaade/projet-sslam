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

public class AddServiceActivity extends AppCompatActivity {

    private Button add;
    private EditText serviceType;
    private Service service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service2);
        add = (Button) findViewById(R.id.add);
        serviceType = (EditText) findViewById(R.id.serviceType);
    }

    private void error() {
        Toast.makeText(getApplicationContext(),"Choisir 'Permis de conduire' ou 'Carte de sante' ou 'Pièce d'identité avec photo' ",Toast.LENGTH_SHORT).show();
    }
}
