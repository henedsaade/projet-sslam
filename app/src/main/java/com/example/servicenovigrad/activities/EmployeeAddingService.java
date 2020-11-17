package com.example.servicenovigrad.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.fb.FbWrapper;

import java.util.List;

public class EmployeeAddingService extends AppCompatActivity {

    ListView availableService;
    List<String> services;
    FbWrapper fb = FbWrapper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_adding_service);
        availableService = findViewById(R.id.employeeListView);

        services
    }
}