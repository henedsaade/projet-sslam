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
    private FbWrapper fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fb = FbWrapper.getInstance();
        String username = fb.getUserName();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        welcomeMessage= (TextView) findViewById(R.id.welcome);
        welcomeMessage.setText("Bienvenue, " +username);

    }
}