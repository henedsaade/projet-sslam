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
    protected static final String TAG = "[SERVICE]";
    protected static final String firestoreServicesRoute = "services/";
    protected FirebaseFirestore db = FirebaseFirestore.getInstance();;

    protected String[] formFields;
    protected String[] documentsNames;
    protected String serviceName;

    protected String uid;

    // constructor for a service stored in firestore
    public Service(String serviceUid) {
        this.uid = serviceUid;

        // fetch service blueprint from uid
        db.document(firestoreServicesRoute + uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

    public Service(String serviceName, String[] formFields, String[] documentsNames) throws NullPointerException {
        if (serviceName == null || formFields == null || documentsNames == null) {
            throw new NullPointerException();
        }
        this.serviceName = serviceName;
        this.formFields = formFields;
        this.documentsNames = documentsNames;
    }

    // Method to save the service blueprint to firebase.
    public void saveServiceBlueprint() throws Exception {
        if (formFields != null && documentsNames != null && serviceName != null) {
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
    }

    public String[] getFormFields() {
        return formFields;
    }

    public String[] getDocumentsNames() {
        return documentsNames;
    }

    public String getServiceName() {
        return serviceName;
    }
}
