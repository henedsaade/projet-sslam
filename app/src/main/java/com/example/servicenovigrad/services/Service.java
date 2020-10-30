package com.example.servicenovigrad.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.servicenovigrad.fb.FbWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;

public class Service {
    private static final String TAG = "[SERVICE]";
    public static final String firestoreServicesRoute = "services/";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();;

    private String[] formFields;
    private String[] documentsNames;
    private String serviceName;

    private String uid;
    private HashMap<String, Document> documents;
    private Form form;

    // constructor for a service stored in firestore
    public Service(String serviceUid) throws FirebaseFirestoreException {
        this.uid = serviceUid;
        fetchServiceInformations();
    }

    public Service(String serviceName, String[] formFields, String[] documentsNames) {
        this.serviceName = serviceName;
        this.formFields = formFields;
        this.documentsNames = documentsNames;

        this.form = new Form(formFields);
        this.documents =
    }

    public void initializeForm()

    // Method to save the service blueprint to firebase.
    public void saveServiceInformations() throws Exception {
        HashMap<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put("formFields", formFields);
        dataToSave.put("documentsNames", documentsNames);
        dataToSave.put("serviceName", serviceName);

        try {
            FbWrapper.getInstance().setDocument(firestoreServicesRoute, dataToSave);
        } catch (FirebaseFirestoreException e) {
            Log.d(TAG, e.getMessage());
            throw new Exception("Error while trying to save the service to firestore.");
        }
    }

    public Task<DocumentSnapshot> fetchServiceInformations() throws FirebaseFirestoreException {
        return db.document(firestoreServicesRoute + uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        formFields = (String[]) document.get("formFields");
                        documentsNames = (String[]) document.get("documentsNames");
                        serviceName = (String) document.get("serviceName");

                    } else {
                        // failed to find service in firestore
                        Log.d(TAG, "No such service");
                    }
                } else {
                    // async task failed
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

}
