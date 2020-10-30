package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddService extends AppCompatActivity {

    private Button add;
    private EditText serviceType;
    private Service service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service2);
        add = (Button) findViewById(R.id.add);
        serviceType = (EditText) findViewById(R.id.serviceType);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serviceType.getText().toString().contains("permis") || serviceType.getText().toString().contains("conduire") ){
                    service= new Service(ServiceType.DRIVER_LICENSE);
                }
                else if (serviceType.getText().toString().contains("carte") || serviceType.getText().toString().contains("sante") ) {
                    service = new Service(ServiceType.HEALTH_CARD);
                }
                else if (serviceType.getText().toString().contains("identite") || serviceType.getText().toString().contains("photo") || serviceType.getText().toString().contains("piece") ) {
                    service = new Service(ServiceType.ID_WITH_PICTURE);
                }
                else {
                    error();
                }
            }
        });
    }

    private void error() {
        Toast.makeText(getApplicationContext(),"Choisir 'Permis de conduire' ou 'Carte de sante' ou 'Pièce d'identité avec photo' ",Toast.LENGTH_SHORT).show();
    }
}
