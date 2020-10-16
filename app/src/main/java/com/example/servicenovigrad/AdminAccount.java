package com.example.servicenovigrad;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class AdminAccount extends Account {
    public static final AccountType accountType = AccountType.ADMIN;
    private static final String firestoreUsersRoute = "allUsers/admins/adminUsers/";
    private String userName;
    private String email;
    private String uid;

    public AdminAccount(String userName, String email, String uid) {
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

    public void saveAccountToFirestore(FirebaseFirestore mFirestore, long timestamp) {
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put("email", this.email);
        dataToSave.put("createdAt", timestamp);
        String documentPath = firestoreUsersRoute + this.uid;

        try {
            FirestoreWrapper.setDocument(documentPath, dataToSave);
        } catch (FirebaseFirestoreException e) {
            Log.d(TAG, e.getMessage());
        }
    }
}