package com.example.servicenovigrad.accounts;

import android.content.Context;
import android.util.Log;
import android.content.Intent;

import com.example.servicenovigrad.activities.AdminActivity;
import com.example.servicenovigrad.activities.EmployeeOptions;
import com.example.servicenovigrad.fb.FbWrapper;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.core.content.ContextCompat.startActivity;

public class EmployeeAccount extends Account {

    public EmployeeAccount(String userName, String firstName, String lastName, String email, String uid) {
        super(userName, firstName, lastName, email, uid);

    }

    public AccountType getAccountType() {
        return AccountType.EMPLOYEE;
    }

    public String getRole() { return "employé"; }

    public String toString() {
        return "Username: " + userName + ", Email: " + email + ", " + "Account Type: EMPLOYEE";
    }






    public void saveAccountToFirestore(FbWrapper fb, FieldValue timestamp) {


        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put("email", this.email);
        dataToSave.put("createdAt", timestamp);
        dataToSave.put("firstName", this.firstName);
        dataToSave.put("lastName", this.lastName);
        dataToSave.put("role", "employee");
        dataToSave.put("succursale",fb.getDocumentRef("succursales/template"));
        String documentPath = firestoreUsersRoute + this.uid;

        fb.setDocument(documentPath, dataToSave);
    }

    public void openMainUi(Context currentActivity) {
        Intent intent = new Intent(currentActivity, EmployeeOptions.class);
        currentActivity.startActivity(intent);
    }

}
