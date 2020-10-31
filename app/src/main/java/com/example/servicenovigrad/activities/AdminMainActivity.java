package com.example.servicenovigrad.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.servicenovigrad.R;

public class AdminMainActivity extends AppCompatActivity {
    private  Button addService;
    private Button setService;
    private Button deleteService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_options);

        addService = (Button) findViewById(R.id.addService);
        setService = (Button) findViewById(R.id.setService);
        deleteService = (Button) findViewById(R.id.deleteService);
        
        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddService();
            }
        });

    }

    private void openAddService() {
        Intent intent = new Intent(this, AddServiceActivity.class);
        startActivity(intent);
    }
}