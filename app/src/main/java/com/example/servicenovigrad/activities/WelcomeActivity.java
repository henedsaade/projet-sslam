package com.example.servicenovigrad.activities;


import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servicenovigrad.fb.FbWrapper;
import com.example.servicenovigrad.R;
import com.example.servicenovigrad.services.Document;
import com.example.servicenovigrad.services.Service;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import io.grpc.internal.SerializingExecutor;

public class WelcomeActivity extends AppCompatActivity {
    private TextView welcomeMessage;
    private Button signoutButton;
    private FbWrapper fb;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fb = FbWrapper.getInstance();
        setContentView(R.layout.activity_welcome_page);

        signoutButton = (Button) findViewById(R.id.signout);
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fb.handleSignOut();
                openSignInPage();
            }
        });
        continueButton = (Button) findViewById(R.id.go);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openServicePage();
            }
        });

        String prenom = fb.getCurrentUser().getFirstName();
        String role = fb.getCurrentUser().getRole();

        welcomeMessage= (TextView) findViewById(R.id.welcome);
        welcomeMessage.setText("Bienvenue, " + prenom + ". Vous êtes connecté sous un compte " + role + ".");
    }

    public void openSignInPage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void openServicePage() {
        Intent intent = new Intent(this,AdminActivity.class);
        startActivity(intent);
    }
}
