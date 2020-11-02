package com.example.servicenovigrad.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.fb.FbWrapper;
import com.example.servicenovigrad.services.Service;
import com.google.android.gms.common.config.GservicesValue;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SetServiceActivity extends AppCompatActivity {

    private TextView firstService;
    private TextView secondService;
    private TextView thirdService;

    private Button modifier1;
    private Button modifier2;
    private Button modifier3;

    static String serviceName;

    protected static final String firestoreServicesRoute = "services/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_service);

        firstService = (TextView) findViewById(R.id.firstService);
        secondService =  (TextView) findViewById(R.id.secondService);
        thirdService = (TextView) findViewById(R.id.thirdService);

        modifier1 = (Button) findViewById(R.id.button);
        modifier2 = (Button) findViewById(R.id.button2);
        modifier3 = (Button) findViewById(R.id.button3);

        loadServices();

        modifier1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceName = "carte de santé";
                if(firstService.getText().toString().equals("Carte de santé")){
                    openModificationsPage();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Ce service n'existe pas, impossible de modifier",Toast.LENGTH_LONG).show();
                }
            }
        });

        modifier2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceName = "permis de conduire";
                if(secondService.getText().toString().equals("Permis de conduire")){
                    openModificationsPage();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Ce service n'existe pas, impossible de modifier",Toast.LENGTH_LONG).show();
                }
            }
        });

        modifier3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceName = "pièce d'identité avec photo";
                if(thirdService.getText().toString().equals("Pièce de d'identité avec photo")){
                    openModificationsPage();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Ce service n'existe pas, impossible de modifier",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void loadServices(){
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

    private void openModificationsPage() {
        Intent intent = new Intent(this,Modifications.class);
        startActivity(intent);
    }



}