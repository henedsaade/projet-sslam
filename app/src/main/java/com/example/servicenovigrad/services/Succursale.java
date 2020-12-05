package com.example.servicenovigrad.services;

import com.example.servicenovigrad.fb.FbWrapper;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Succursale {
    String [] address ;
    DocumentReference employeeUIDS;
    List<DocumentReference> services;
    Map<String,String> worksHours;
    String succursaleUid;
    FirebaseFirestore mStore;
    FbWrapper fb = FbWrapper.getInstance();
    final String firestoreUsersRoute = "succursales/";

    public Succursale(String [] adress, Map<String,String> worksHours, String succursaleUid,String employeeUid) {
        this.address = adress;
        this.worksHours = worksHours;
        this.services = new ArrayList<>();
        this.employeeUIDS = fb.getDocumentRef("users/"+employeeUid);
        this.succursaleUid = succursaleUid;
    }

    public Map<String,String> getWorksHours() {
        return worksHours;
    }

    public String[] getAddress() {
        return address;
    }

    public DocumentReference getEmployeeUIDS() {
        return employeeUIDS;
    }

    public List<DocumentReference> getServices() {
        return services;
    }

    public void addService (DocumentReference service) {
        services.add(service);
    }

    public void removeService (DocumentReference service) {
        services.remove(service);
    }

    public void saveAccountToFirestore(FbWrapper fb, FieldValue timestamp) {
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put("adress", this.services);
        dataToSave.put("createdAt", timestamp);
        dataToSave.put("workHours", this.worksHours);
        dataToSave.put("services", this.services);
        dataToSave.put("employeeUIDS", this.employeeUIDS);
        String documentPath = firestoreUsersRoute + this.succursaleUid;

        fb.setDocument(documentPath, dataToSave);
    }


}
