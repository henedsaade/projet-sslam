package com.example.servicenovigrad.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.servicenovigrad.accounts.AccountType;
import com.example.servicenovigrad.fb.FbWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class Service {
    protected static final String TAG = "[SERVICE]";
    protected static final String firestoreServicesRoute = "services/";

    protected String[] formFields;
    protected String[] documentsNames;
    protected String serviceName;

    protected String uid;

    // constructor for a service stored in firestore
    public Service(String serviceUid) {
        this.uid = serviceUid;

        // fetch service blueprint from uid
        FbWrapper.getInstance().getDocument(firestoreServicesRoute + uid).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

    public static Task<QuerySnapshot> getAllServices() {
        return FbWrapper.getInstance().getCollection(firestoreServicesRoute);

//        Call this method like this in an activity:

//        Service.getAllServices().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//              @Override
//              public void onSuccess(QuerySnapshot documentSnapshots) {
//                  if (documentSnapshots.isEmpty()) {
//                      Log.d(TAG, "onSuccess: LIST EMPTY");
//                      return;
//                  } else {
//                      // Convert the whole Query Snapshot to a list
//                      // of objects directly! No need to fetch each
//                      // document.
//                      List<Service> services = documentSnapshots.toObjects(Service.class);
//
//                      // Add all to your list
//                      mArrayList.addAll(types);
//                      Log.d(TAG, "onSuccess: " + mArrayList);
//                  }
//              })
//                      .addOnFailureListener(new OnFailureListener() {
//                  @Override
//                  public void onFailure(@NonNull Exception e) {
//                      Toast.makeText(getApplicationContext(), "Error getting data!!!", Toast.LENGTH_LONG).show();
//                  }
//              });
    }

    // Method to create a new service
    public void saveServiceBlueprint() {
        if (FbWrapper.getInstance().getCurrentUser().getAccountType() == AccountType.ADMIN && formFields != null && documentsNames != null && serviceName != null) {
            HashMap<String, Object> dataToSave = new HashMap<String, Object>();
            dataToSave.put("formFields", formFields);
            dataToSave.put("documentsNames", documentsNames);
            dataToSave.put("serviceName", serviceName);

            FbWrapper.getInstance().setDocument(firestoreServicesRoute, dataToSave);
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
