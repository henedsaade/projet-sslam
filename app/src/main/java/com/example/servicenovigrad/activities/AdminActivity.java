package com.example.servicenovigrad.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.servicenovigrad.R;

public class AdminActivity extends AppCompatActivity {

    private Button add;
    private Button set;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        add= (Button) findViewById(R.id.addService);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddPage();
            }
        });

        set = (Button) findViewById(R.id.setService);
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSetPage();
            }
        });


    }

    private void openAddPage() {
        Intent intent= new Intent(this, CreateServiceActivity.class);
        startActivity(intent);
    }

    private  void openSetPage() {
        Intent intent = new Intent( this, SetServiceActivity.class);
        startActivity(intent);
    }

}