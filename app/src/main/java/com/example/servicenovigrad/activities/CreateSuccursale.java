package com.example.servicenovigrad.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.fb.FbWrapper;
import com.example.servicenovigrad.services.Succursale;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateSuccursale extends AppCompatActivity {

    EditText street, buildingNumber, monday,tuesday,wednesday,thursday,friday,saturday,sunday;
    FbWrapper fb = FbWrapper.getInstance();
    Button terminate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_succursale);

        street = findViewById(R.id.street);
        buildingNumber = findViewById(R.id.buildingNo);
        monday = findViewById(R.id.monday);
        tuesday = findViewById(R.id.tuesday);
        wednesday = findViewById(R.id.wednesday);
        thursday = findViewById(R.id.thursday);
        friday = findViewById(R.id.friday);
        saturday = findViewById(R.id.saturday);
        sunday = findViewById(R.id.sunday);
        terminate = findViewById(R.id.createSuccursale);

        terminate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String myStreet = street.getText().toString();
                String myBuilding = buildingNumber.getText().toString();
                String [] adress = new String [] {myStreet,myBuilding};

                String day1 = monday.getText().toString();
                String day2 = tuesday.getText().toString();
                String day3 = wednesday.getText().toString();
                String day4 = thursday.getText().toString();
                String day5 = friday.getText().toString();
                String day6 = saturday.getText().toString();
                String day7 = sunday.getText().toString();

                if(!(checkError(myStreet,myBuilding,day1,day2,day3,day4,day5,day6,day7))){

                    Map<String,String> worksHours = new HashMap<>();
                    worksHours.put("monday",day1);
                    worksHours.put("tuesday",day2);
                    worksHours.put("wednesday",day3);
                    worksHours.put("thursday",day4);
                    worksHours.put("friday",day5);
                    worksHours.put("saturday",day6);
                    worksHours.put("sunday",day7);

                    //Getting employee Uid;
                    String employeUid = fb.getUserUid();

                    //Create Uid for succursale from employee Uid
                    String succursaleUid = employeUid + "- succursale";


                    Succursale mySuccursale = new Succursale(adress,worksHours,succursaleUid,employeUid);
                    mySuccursale.saveAccountToFirestore(fb, FieldValue.serverTimestamp());

                    fb.getDocumentRef("users/"+employeUid).update("succursale",
                            fb.getDocumentRef("succursales/"+succursaleUid));

                    Toast.makeText(getApplicationContext(),"Succursale created",Toast.LENGTH_SHORT).show();



                    startActivity(new Intent(CreateSuccursale.this,EmployeeOptions.class));
                }


            }
        });


    }

    public boolean checkError(String street, String number, String day1, String day2, String day3, String day4,
                          String day5,String day6, String day7) {

        boolean error  = false;

        if(TextUtils.isEmpty(street)) {
            this.street.setError("Remplir ce champ");
            error = true;
        }
        if(TextUtils.isEmpty(number)) {
            this.buildingNumber.setError("Remplir ce champ");
            error = true;
        }
        if(TextUtils.isEmpty(day1)) {
            this.monday.setError("Remplir ce champ");
            error = true;
        }
        if(TextUtils.isEmpty(day2)) {
            this.tuesday.setError("Remplir ce champ");
            error = true;

        }
        if(TextUtils.isEmpty(day3)) {
            this.wednesday.setError("Remplir ce champ");
            error = true;
        }
        if(TextUtils.isEmpty(day4)) {
            this.thursday.setError("Remplir ce champ");
            error = true;
        }
        if(TextUtils.isEmpty(day5)) {
            this.friday.setError("Remplir ce champ");
            error = true;
        }

        if(TextUtils.isEmpty(day6)) {
            this.saturday.setError("Remplir ce champ");
            error = true;
        }

        if(TextUtils.isEmpty(day7)) {
            this.sunday.setError("Remplir ce champ");
            error = true;
        }
        return error;

    }


}