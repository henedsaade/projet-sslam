package com.example.servicenovigrad.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.servicenovigrad.R;

public class EmployeeOptions extends AppCompatActivity {

    private Button add;
    private Button view;
    private Button schedule;
    private Button request;
    private Button succursale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_options);

        add = findViewById(R.id.add_service);
        view = findViewById(R.id.serviceAvailable);
        schedule = findViewById(R.id.schedule);
        request = findViewById(R.id.request);
        succursale = findViewById(R.id.succursale);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmployeeOptions.this,EmployeeManageServices.class));
            }
        });

       succursale.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(EmployeeOptions.this, CreateSuccursale.class));
           }
       });

       view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(EmployeeOptions.this,SuccursaleAvailableServices.class));
           }
       });

       schedule.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(EmployeeOptions.this, setHourActivity.class));
           }
       });


    }
}