package com.example.servicenovigrad.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.fb.FbWrapper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ClientInformations extends AppCompatActivity {


    private static final int PICK_IMAGE_CODE =1000  ;
    EditText firstName, lastName, adress, birthDate, other;
    Button  uploadFiles, submit;
    Map<String,Object> clientData;
    String service= "";
    String succursale = "";

    String rateValue = "";

    int increment = 0;

    Uri pdfUri;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

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

        Bundle extras = getIntent().getExtras();
        service = extras.getString("service");
        succursale = extras.getString("succursale");



        uploadFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPdfFile();

            }
        });







    }

    private void uploadPdfFile() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"PDF FILE SELECT"),12);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 12 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    uploadPDFFileFirebase(data.getData());
                }
            });
        }
    }

    private boolean checkValidInformations () {
        if(TextUtils.isEmpty(firstName.getText().toString()) || TextUtils.isEmpty(lastName.getText().toString()) ||
                TextUtils.isEmpty(adress.getText().toString())) {
            Toast.makeText(getApplicationContext(),"Vous devez remplir les champs", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void uploadPDFFileFirebase ( Uri data) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("File is loading");

        final String finalService = service;
        final String finalSucc = succursale;
        progressDialog.show();
        increment++;


        StorageReference reference = storageReference.child("upload" + System.currentTimeMillis() + ".PDF");

        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                        while (! uriTask.isComplete());
                        Uri uri = uriTask.getResult();



                        rateValue = openRateDialogBox();
                        clientData.put("file Url " + increment, uri.toString());
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
                        clientData.put("succursaleId",finalSucc);
                        clientData.put("myRate",rateValue);

                        DocumentReference serviceRequestRef = fb.getDocumentRef("servicesRequests/" + clientId + " - request");

                        if (checkValidInformations()) {
                            fb.setDocument("servicesRequests/" + clientId + " - request",clientData);
                            Log.d("Succursale Id",finalSucc);
                            fb.getDocumentRef("succursales/"+succursale).update("servicesRequests", FieldValue.arrayUnion(serviceRequestRef));
                        }

                        Toast.makeText(ClientInformations.this,"File is uploaded",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();


                        }

                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                double progress = (100 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                progressDialog.setMessage("File uploaded..." + (int) progress + "%");

            }
        });

    }

    public String openRateDialogBox() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.rate_page, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("Note de la succursale");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        final RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);



        final Button next = dialogView.findViewById(R.id.next);
        final Button submit = dialogView.findViewById(R.id.valid_rate);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = String.valueOf(ratingBar.getRating());
                Toast.makeText(getApplicationContext(), rateValue,Toast.LENGTH_SHORT).show();
                b.dismiss();
            }
        });


    return rateValue;

    }



}