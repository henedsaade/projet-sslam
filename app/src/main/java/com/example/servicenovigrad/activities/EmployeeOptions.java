package com.example.servicenovigrad.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.servicenovigrad.R;

public class EmployeeOptions extends AppCompatActivity {

    private Button add;
    private Button view;
    private Button schedule;
    private Button request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_options);

        add = findViewById(R.id.add_service);
<<<<<<< HEAD
//        delete = findViewById(R.id.remove_service);
=======
        view = findViewById(R.id.serviceAvailable);
>>>>>>> a8f0fc29a94666a9d3904ed586ecbf9d0cd405c1
        schedule = findViewById(R.id.schedule);
        request = findViewById(R.id.request);


    }
}