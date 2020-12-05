package com.example.servicenovigrad.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.servicenovigrad.accounts.AccountType;
import com.example.servicenovigrad.fb.FbWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Service {
    protected static final String TAG = "[SERVICE]";
    protected static final String firestoreServicesRoute = "services/";

    protected List<String> formFields;
    protected List<String> documentsNames;
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
                        formFields = (List<String>) document.get("formFields");
                        documentsNames = (List<String>) document.get("documentsNames");
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

    public Service(String serviceName, List<String> formFields, List<String> documentsNames) throws NullPointerException {
        if (serviceName == null || formFields == null || documentsNames == null) {
            throw new NullPointerException();
        }
        this.serviceName = serviceName;
        this.formFields = formFields;
        this.documentsNames = documentsNames;
    }

//    Method to convert a service document to an instance of Service
    public static Service firebaseDocumentToService(DocumentSnapshot doc) {
        ArrayList<String> formFields = (ArrayList<String>) doc.get("formFields");
        ArrayList<String> documentsNames = (ArrayList<String>) doc.get("documentsNames");
        String serviceName = (String) doc.get("serviceName");

        return new Service(serviceName, formFields, documentsNames);
    }

    public static Task<QuerySnapshot> getAllServices() {
        return FbWrapper.getInstance().getCollection(firestoreServicesRoute);

//        Call this method like this in an activity:

//                Service.getAllServices().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot documentSnapshots) {
//                      if (documentSnapshots.isEmpty()) {
//                          Log.d("[CONSOLE]", "onSuccess: LIST EMPTY");
//                          return;
//                      } else {
//                          // Convert the whole Query Snapshot to a list
//                          // of objects directly! No need to fetch each
//                          // document.
//                          List<DocumentSnapshot> snapshots = documentSnapshots.getDocuments();
//
//                          ArrayList<Service> services = new ArrayList<>();
//
//                          for (DocumentSnapshot doc : snapshots) {
//                              services.add(Service.firebaseDocumentToService(doc));
//                          }
//                          Log.d("[CONSOLE]", "onSuccess: " + services.get(0).getDocumentsNames());
//                        }
//                }});
    }

    public static String toCamelCase(String s) {
        if (s != null) {
            String[] splitted = s.split(" ");
            if (splitted.length > 1) {
                String result = splitted[0].toLowerCase();

                for (int i=1; i<splitted.length; i++) {
                    result += splitted[i].substring(0, 1).toUpperCase() + splitted[i].substring(1);
                }

                return result;
            } else {
                return s.toLowerCase();
            }
        }

        return null;
    }

    // Method to save a new service to firestore
    public void saveServiceBlueprint() {
        if (FbWrapper.getInstance().getCurrentUser().getAccountType() == AccountType.ADMIN && formFields != null && documentsNames != null && serviceName != null) {
            HashMap<String, Object> dataToSave = new HashMap<String, Object>();
            dataToSave.put("formFields", formFields);
            dataToSave.put("documentsNames", documentsNames);
            dataToSave.put("serviceName", serviceName);

            FbWrapper.getInstance().setDocument(firestoreServicesRoute + toCamelCase(serviceName), dataToSave).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Successfully saved service");
                    } else {
                        Log.d(TAG, "Error while saving service");
                    }
                }
            });
        }

//        Call this method like this in an activity:

//                ArrayList<String> testFormFields = new ArrayList<>();
//                testFormFields.add("nom");
//                testFormFields.add("prénom");
//                testFormFields.add("date de naissance");
//                ArrayList<String> testDocumentsNames = new ArrayList<>();
//                testDocumentsNames.add("preuve de domicile");
//                testDocumentsNames.add("preuve de statut");
//
//                Service testService = new Service("Test Service", testFormFields, testDocumentsNames);
//                testService.saveServiceBlueprint();
    }

    public List<String> getFormFields() {
        return formFields;
    }

    public String getFormsFieldsInString () {
        String forms = "Formulaire à remplir:";
        for(String info : this.formFields) {
            forms = forms + " " + info;
        }
        return  forms;
    }
    public String getDocsFieldsInString () {
        String docs = "Documents requis:";
        for(String info : this.documentsNames) {
            docs = docs + " " + info;
        }
        return  docs;
    }

    public List<String> getDocumentsNames() {
        return documentsNames;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String toString () {
        String name = this.serviceName;
        String formFields = "Données du formualire : ";
        for( String info : this.formFields) {
            formFields = formFields + " " + info;
        }
        String documentsNames = "Documents requis : ";
        for (String info : this.documentsNames) {
            documentsNames = documentsNames + " " + info;
        }

        return "Nom du service : " + name +"\n" + formFields + "\n" + documentsNames ;

    }
}
