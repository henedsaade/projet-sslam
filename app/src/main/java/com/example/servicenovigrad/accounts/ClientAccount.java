package com.example.servicenovigrad.accounts;

import android.content.Context;
import android.util.Log;
import android.content.Intent;

import com.example.servicenovigrad.activities.AdminActivity;
import com.example.servicenovigrad.fb.FbWrapper;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;


public class ClientAccount extends Account {
    public ClientAccount(String userName, String firstName, String lastName, String email, String uid) {
        super(userName, firstName, lastName, email, uid);
    }

    public AccountType getAccountType() {
        return AccountType.CLIENT;
    }

    public String getRole() { return "client"; }

    public String toString() {
        return "Username: " + userName + ", Email: " + email + ", " + "Account Type: CLIENT";
    }

    public void saveAccountToFirestore(FbWrapper fb, FieldValue timestamp) {
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put("email", this.email);
        dataToSave.put("createdAt", timestamp);
        dataToSave.put("firstName", this.firstName);
        dataToSave.put("lastName", this.lastName);
        dataToSave.put("role", "client");
        String documentPath = firestoreUsersRoute + this.uid;

        fb.setDocument(documentPath, dataToSave);
    }

    public void openMainUi(Context currentActivity) {
        Intent intent = new Intent(currentActivity, AdminActivity.class);
        currentActivity.startActivity(intent);
    }

}








