package com.example.servicenovigrad.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.servicenovigrad.R;

import java.util.ArrayList;

public class Modifications extends AppCompatActivity {
    private EditText forms;
    private EditText docs;
    private Button finish;

    private ArrayList formsfield;
    private ArrayList docsField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifications);

        forms = (EditText) findViewById(R.id.forms);
        docs = (EditText) findViewById (R.id.docs);
        finish = (Button) findViewById (R.id.modifier);

    }

    private void setForms(){
        String [] form = forms.getText().toString().split(",");
        for(int i = 0; i<form.length; i++){

        }
    }
}