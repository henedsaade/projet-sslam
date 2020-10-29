package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminOptions extends AppCompatActivity {
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
        Intent intent = new Intent(this,AddService.class);
        startActivity(intent);
    }
}