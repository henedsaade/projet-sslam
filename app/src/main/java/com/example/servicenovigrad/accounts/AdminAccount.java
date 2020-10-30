package com.example.servicenovigrad.accounts;

import android.util.Log;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class AdminAccount extends Account {
    public AdminAccount(String userName, String firstName, String lastName, String email, String uid) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.uid = uid;
    }

    public AccountType getAccountType() {
        return AccountType.ADMIN;
    }

    public String getRole() { return "admin"; }

    public String toString() {
        return "Username: " + userName + ", Email: " + email + ", " + "Account Type: ADMIN";
    }

    public void saveAccountToFirestore(FbWrapper fb, FieldValue timestamp) {
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put("email", this.email);
        dataToSave.put("createdAt", timestamp);
        dataToSave.put("firstName", this.firstName);
        dataToSave.put("lastName", this.lastName);
        dataToSave.put("role", "admin");
        String documentPath = firestoreUsersRoute + this.uid;

        try {
            fb.setDocument(documentPath, dataToSave);
        } catch (FirebaseFirestoreException e) {
            Log.d(TAG, e.getMessage());
        }
    }
}