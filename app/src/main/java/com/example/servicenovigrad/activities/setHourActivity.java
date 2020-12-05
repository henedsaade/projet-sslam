package com.example.servicenovigrad.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servicenovigrad.R;
import com.example.servicenovigrad.fb.FbWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.BiMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class setHourActivity extends AppCompatActivity {

    Map<String,Object> workHours;
    FbWrapper fb = FbWrapper.getInstance();
    ListView worksHours;
    List<String> hours ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_hour);

        workHours = new HashMap<>();
        worksHours = findViewById(R.id.horraire);
        hours = new ArrayList<>();

        worksHours.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String [] field = hours.get(position).split("-");
                String day = field [0].trim();
                showDialogPage(day);
                loadHours();
            }
        });

        loadHours();



    }

    public void loadHours() {

        fb.getDocument("succursales/" + fb.getUserUid()+"- succursale")
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



                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplication(),R.layout.simple_list_item_1,hours);
                            worksHours.setAdapter(adapter);

                        }
                    }
                });
    }


    private void showDialogPage (final String day) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.set_hour_dialog_page,null);
        dialogBuilder.setView(dialogView);

        final EditText hour_1= (EditText) dialogView.findViewById(R.id.hour_1);
        final EditText hour_2 = (EditText) dialogView.findViewById(R.id.hour_2);

        final Button set = (Button) dialogView.findViewById(R.id.set_1);

        final Button cancel = (Button) dialogView.findViewById(R.id.cancelHour);
        final TextView error = (TextView) dialogView.findViewById(R.id.error);

        dialogBuilder.setTitle(day);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String start= hour_1.getText().toString().trim();
                String end = hour_2.getText().toString().trim();
                String hourFormat = start + "-" + end;
                if (!TextUtils.isEmpty(start) & !TextUtils.isEmpty(end)) {

                    fb.getDocumentRef("succursales/" + fb.getUserUid()+"- succursale")
                            .update("workHours."+day,hourFormat).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.d("Day Update","successfull");
                                b.dismiss();
                                loadHours();
                                Toast.makeText(getApplicationContext(),"Mise à jour reussie",Toast.LENGTH_SHORT).show();
                            }
                            else
                                Log.d("Day Update","FireBase error");

                        }
                    });


                }
                else {
                    error.setError("Vous devez remplir tous les champs");
                    Log.d("Day Update","failled");
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });
    }
}