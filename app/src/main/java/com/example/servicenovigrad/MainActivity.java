package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.servicenovigrad.ui.login.SignUpPage;

public class MainActivity extends AppCompatActivity {
    private Button loginButton;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (Button) findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWelcomePage();
            }
        });

        signupButton = (Button) findViewById(R.id.signup);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpPage();
            }
        });
    }

    public void openWelcomePage() {
        Intent intent = new Intent(this, WelcomePage.class);
        startActivity(intent);
    }

    public void openSignUpPage() {
        Intent intent = new Intent(this, SignUpPage.class);
        startActivity(intent);
    }
}