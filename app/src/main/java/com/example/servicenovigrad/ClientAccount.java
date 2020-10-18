package com.example.servicenovigrad;

import android.util.Log;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;


public class ClientAccount extends Account {
    public static final AccountType accountType = AccountType.CLIENT;
    private String userName;
    private String email;
    private String uid;

    public ClientAccount(String userName, String email, String uid) {
        this.userName = userName;
        this.email = email;
        this.uid = uid;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getUid() {
        return this.uid;
    }

    public AccountType getAccountType() {
        return AccountType.CLIENT;
    }

    public String toString() {
        return "Username: " + userName + ", Email: " + email + ", " + "Account Type: CLIENT";
    }

    public void saveAccountToFirestore(FieldValue timestamp) {
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put("email", this.email);
        dataToSave.put("createdAt", timestamp);
        dataToSave.put("role", "client");
        String documentPath = firestoreUsersRoute + this.uid;

        try {
            FirestoreWrapper.setDocument(documentPath, dataToSave);
        } catch (FirebaseFirestoreException e) {
            Log.d(TAG, e.getMessage());
        }
    }

}







