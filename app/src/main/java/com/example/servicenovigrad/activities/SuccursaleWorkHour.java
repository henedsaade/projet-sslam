package com.example.servicenovigrad.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.servicenovigrad.Adapter.HoursAdapter;
import com.example.servicenovigrad.R;
import com.example.servicenovigrad.fb.FbWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuccursaleWorkHour extends AppCompatActivity {

    Map<String,Object> workHours;
    FbWrapper fb = FbWrapper.getInstance();
    ListView worksHours;
    List<String> hours ;
    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succursale_work_hour);

        workHours = new HashMap<>();
        worksHours = findViewById(R.id.client_view_hours);
        hours = new ArrayList<>();



        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            id = extras.getString("SuccursaleId");
        }

        loadHours();


    }

    public void loadHours() {

        fb.getDocument("succursales/" + id)
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {

                            hours.clear();

                            DocumentSnapshot document = task.getResult();

                            workHours = (Map<String, Object>) document.get("workHours");

                            hours.add("lundi - " + (String) workHours.get("lundi"));
                            hours.add("mardi - "  + (String) workHours.get("mardi"));
                            hours.add("mercredi - "  + (String) workHours.get("mercredi"));
                            hours.add("jeudi - "  + (String) workHours.get("jeudi"));
                            hours.add("vendredi - "  + (String) workHours.get("vendredi"));
                            hours.add("samedi - "  + (String) workHours.get("samedi"));
                            hours.add("dimanche - "  + (String) workHours.get("dimanche"));



                            HoursAdapter adapter = new HoursAdapter(SuccursaleWorkHour.this,hours);
                            worksHours.setAdapter(adapter);

                        }
                    }
                });
    }

}