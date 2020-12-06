package com.example.servicenovigrad.activities;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.servicenovigrad.fb.FbWrapper;
import com.example.servicenovigrad.R;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    private TextView welcomeMessage;
    private Button signoutButton;
    private FbWrapper fb;
    private Button continueButton;
    private AppCompatActivity activityRef = this;

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
                fb.getCurrentUser().openMainUi(activityRef);



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
    public void openClientPage() {
        Intent intent = new Intent(this, ClientActivity.class);
        startActivity(intent);
    }
}
