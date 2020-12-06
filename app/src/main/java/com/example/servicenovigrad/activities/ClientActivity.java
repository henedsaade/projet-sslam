package com.example.servicenovigrad.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.servicenovigrad.Adapter.SuccursaleAdapter;
import com.example.servicenovigrad.R;
import com.example.servicenovigrad.fb.FbWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ClientActivity extends AppCompatActivity {

    RecyclerView succ_list;
    EditText search;
    SuccursaleAdapter adapter;
    List<String[]> succ_infos;

    FbWrapper fb = FbWrapper.getInstance();
   // Map hours = new HashMap<>(7);


    SuccursaleAdapter.RecyclerViewClickListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        search = findViewById(R.id.search);
        succ_infos = new ArrayList<>();

        setOnclick();
        adapter = new SuccursaleAdapter(succ_infos, listener);
        succ_list = findViewById(R.id.succ_list);

        succ_list.setHasFixedSize(true);
        succ_list.setLayoutManager(new LinearLayoutManager(this));
        succ_list.setAdapter(adapter);



        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        //Initialistaion de succInfos

        fb.getCollection("succursales").addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for(QueryDocumentSnapshot doc : task.getResult()) {
                        if (! doc.getString("number").equals("null")){
                            final String number = doc.getString("number");
                            final String adress = doc.getString("adress");
                            final String id = doc.getString("Id");
                            List<DocumentReference> service = (List<DocumentReference>) doc.get("services");


                            for(DocumentReference ref : service) {
                                final List<String> names= new ArrayList<>();
                                ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot document = task.getResult();
                                            String name = document.getString("serviceName");
                                            names.add(name);
                                            String l = "";
                                            for(int i =0 ; i<names.size(); i++) {
                                                if(i == 0 )
                                                    l = names.get(i);
                                                else
                                                    l = l + ", " + names.get(i);
                                            }
                                            String [] infos = {number,adress,l,id};
                                            succ_infos.add(infos);
                                            adapter.notifyDataSetChanged();

                                        }
                                    }
                                });
                            }
                           // hours = (Map) doc.get("workHours");


                            Log.d("Succ Loading","success" );
                        }

                    }
                }
                else
                    Log.d("Succ Loading","failled");
            }
        });



    }

    private void setOnclick() {
        listener = new SuccursaleAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                openDialogBox(position);
            }
        };
    }


    private void filter (String toString) {
        ArrayList<String []> filteredList= new ArrayList<>();
        for(String[] succ : succ_infos ) {
            if(succ [0].toLowerCase().contains(toString.toLowerCase()) ) {
                Log.d("filterInfos", succ[0]);
                filteredList.add(succ);
            }
            else if (succ [1].toLowerCase().contains(toString.toLowerCase())) {
                filteredList.add(succ);
            }
            else if (succ [2].toLowerCase().contains(toString.toLowerCase())) {
                filteredList.add(succ);
            }
            else
                Log.d("FilterInfos", "Nothing");

            adapter.filterList(filteredList);
        }
    }

    public void openDialogBox (final int position) {

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.client_dialog_box, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog b = dialogBuilder.create();
        b.show();

        final Button hours = dialogView.findViewById(R.id.hours_3);
        final Button services = dialogView.findViewById(R.id.service_3);

        services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(new Intent(getApplicationContext(),ClientRequest.class));
                intent.putExtra("SuccursaleId", succ_infos.get(position)[3]);
                startActivity(intent);
            }
        });

        hours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(new Intent(getApplicationContext(),SuccursaleWorkHour.class));
                intent.putExtra("SuccursaleId", succ_infos.get(position)[3]);
                startActivity(intent);
            }
        });
    }


}