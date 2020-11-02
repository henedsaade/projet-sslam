package com.example.servicenovigrad.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.fb.FbWrapper;
import com.example.servicenovigrad.services.Service;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class DeleteService extends AppCompatActivity {

    private TextView firstService;
    private TextView secondService;
    private TextView thirdService;

    private Button supprimer1;
    private Button supprimer2;
    private Button supprimer3;

    private String serviceName;

    protected static final String firestoreServicesRoute = "services/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_service);

        firstService = (TextView) findViewById(R.id.firstService1);
        secondService =  (TextView) findViewById(R.id.secondService2);
        thirdService = (TextView) findViewById(R.id.thirdService3);

        supprimer1 = (Button) findViewById(R.id.button_1);
        supprimer2 = (Button) findViewById(R.id.button_2);
        supprimer3 = (Button) findViewById(R.id.button_3);

        loadServices();

        supprimer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceName="carte de santé";

                if(firstService.getText().toString().equals("Carte de santé")){
                   delete();
                    Toast.makeText(getApplicationContext(),"Service supprimé",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Ce service n'existe pas, impossible de supprimer",Toast.LENGTH_LONG).show();
                }
                openSetService();
            }
        });

        supprimer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceName="permis de conduire";

                if(secondService.getText().toString().equals("Permis de conduire")){
                    delete();
                    Toast.makeText(getApplicationContext(),"Service supprimé",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Ce service n'existe pas, impossible de supprimer",Toast.LENGTH_LONG).show();
                }
                openSetService();
            }
        });

        supprimer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceName="pièce d'identité avec photo";

                if(thirdService.getText().toString().equals("Pièce de d'identité avec photo")){
                    delete();
                    Toast.makeText(getApplicationContext(),"Service supprimé",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Ce service n'existe pas, impossible de supprimer",Toast.LENGTH_LONG).show();
                }
                openSetService();
            }
        });



    }

    private void loadServices(){
        FbWrapper.getInstance().getDocument(firestoreServicesRoute +"carteDeSanté")
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            firstService.setText("Carte de santé");
                        }
                        else {
                            firstService.setText("Pas de service de carte de santé");
                        }
                    }
                });


        FbWrapper.getInstance().getDocument(firestoreServicesRoute + "permisDeConduire" )
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            secondService.setText("Permis de conduire");
                        }
                        else
                            secondService.setText("Pas de service de permis de conduire");
                    }
                });

        FbWrapper.getInstance().getDocument(firestoreServicesRoute+ "pièceD'identitéAvecPhoto")
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            thirdService.setText("Pièce de d'identité avec photo");
                        }
                        else
                            thirdService.setText("Pas de service d'ID avec photo");
                    }
                });
    }

    private void delete(){
        FbWrapper.getInstance().deleteDocument(firestoreServicesRoute + Service.toCamelCase(serviceName));

    }

    private void openSetService() {
        Intent intent = new Intent(this,AdminActivity.class);
        startActivity(intent);
    }
}