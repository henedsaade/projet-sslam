package com.example.servicenovigrad;

import android.util.Log;


import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class EmployeeAccount extends Account {
    public static final AccountType accountType = AccountType.EMPLOYEE;
    private String userName;
    private String email;
    private String uid;

    public EmployeeAccount(String userName, String email, String uid) {
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
        return AccountType.EMPLOYEE;
    }

    public String toString() {
        return "Username: " + userName + ", Email: " + email + ", " + "Account Type: EMPLOYEE";
    }

    public void saveAccountToFirestore(FbWrapper fb, FieldValue timestamp) {
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put("email", this.email);
        dataToSave.put("createdAt", timestamp);
        dataToSave.put("role", "employee");
        String documentPath = firestoreUsersRoute + this.uid;

        try {
            fb.setDocument(documentPath, dataToSave);
        } catch (FirebaseFirestoreException e) {
            Log.d(TAG, e.getMessage());
        }
    }

}
