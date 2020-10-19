package com.example.servicenovigrad;


import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class WelcomePage extends AppCompatActivity {
    private TextView welcomeMessage;
    private Button signoutButton;
    private FbWrapper fb;

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

        String username = fb.getCurrentUser().getUserName();
        String role = fb.getCurrentUser().getRole();

        welcomeMessage= (TextView) findViewById(R.id.welcome);
        welcomeMessage.setText("Bienvenue, " + username + ". Vous êtes connecté sous un compte " + role + ".");
    }

    public void openSignInPage() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
