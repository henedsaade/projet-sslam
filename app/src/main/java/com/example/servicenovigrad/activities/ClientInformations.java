package com.example.servicenovigrad.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.fb.FbWrapper;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URI;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ClientInformations extends AppCompatActivity {


    private static final int PICK_IMAGE_CODE =1000  ;
    EditText firstName, lastName, adress, birthDate, other;
    Button upload, uploadFiles, submit;
    Map<String,Object> clientData;
    DatePickerDialog.OnDateSetListener setListener;

    Uri pdfUri;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    FbWrapper fb = FbWrapper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_informations);

        firstName = findViewById(R.id.clientFirstName);
        lastName = findViewById(R.id.clientLastName);
        adress = findViewById(R.id.clientAdress);
        birthDate = findViewById(R.id.clientBirthDate);
        other = findViewById(R.id.clientSuppInfo);

        upload = findViewById(R.id.upload);
        submit = findViewById(R.id.submit);
        uploadFiles = findViewById (R.id.uploadFiles);
        clientData = new HashMap<>();

        Calendar calendar = Calendar.getInstance();
        final int cYear = calendar.get(Calendar.YEAR);
        final int cMonth = calendar.get(Calendar.MONTH);
        final int cDay = calendar.get(Calendar.DAY_OF_MONTH);

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ClientInformations.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        if(year > cYear) {
                            Toast.makeText(getApplicationContext(),"Date invalide",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            month = month +1;
                            String date = year + "/" + month + "/" + cDay;
                            birthDate.setText(date);
                        }
                    }
                },cYear,cMonth,cDay);

                datePickerDialog.show();

            }
        });



        uploadFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pdfUri != null) {
                    uploadPdfFile(pdfUri);
                }
                else
                    Toast.makeText(getApplicationContext(),"Select a file", Toast.LENGTH_SHORT).show();
            }
        });

        String service= "";
        String succursale = "";
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            service = extras.getString("service");
            succursale = extras.getString("succursale");
        }

        final String finalService = service;
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName = firstName.getText().toString();
                String lName = lastName.getText().toString();
                String mAdress = adress.getText().toString();
                String mBirthDate = birthDate.getText().toString();
                String clientId = fb.getUserUid();

                clientData.put("firstName",fName);
                clientData.put("lastName",lName);
                clientData.put("adress",mAdress);
                clientData.put("birthDate",mBirthDate);
                clientData.put("clientId",clientId);
                clientData.put("service", finalService);

                if (checkValidInformations()) {
                    fb.setDocument("servicesRequests/" + clientId + " - request",clientData);
                }

            }
        });


    }

    private void uploadPdfFile(Uri pdfUri) {

        StorageReference storageReference = storage.getReference();
    }


    private void selectFile () {
        if(ContextCompat.checkSelfPermission(ClientInformations.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            selectPdf();
        }
        else
            ActivityCompat.requestPermissions(ClientInformations.this, new String [] {Manifest.permission.READ_EXTERNAL_STORAGE},9);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 9 && grantResults [0] == PackageManager.PERMISSION_GRANTED)
            selectPdf();
        else
            Toast.makeText(getApplicationContext(),"Vous devez donner la permission",Toast.LENGTH_SHORT).show();
    }

    private void selectPdf() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,86);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 86 && resultCode == RESULT_OK && data != null) {
            pdfUri = data.getData();
        } else
          Toast.makeText(getApplicationContext(), "Please select a file", Toast.LENGTH_SHORT).show();
    }

    private boolean checkValidInformations () {
        if(TextUtils.isEmpty(firstName.getText().toString()) || TextUtils.isEmpty(lastName.getText().toString()) ||
                TextUtils.isEmpty(adress.getText().toString())) {
            Toast.makeText(getApplicationContext(),"Vous devez remplir les champs", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }



}