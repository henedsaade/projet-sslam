package com.example.servicenovigrad.accounts;

import android.content.Context;
import android.content.Intent;

import com.example.servicenovigrad.activities.AdminActivity;
import com.example.servicenovigrad.fb.FbWrapper;
import com.google.firebase.firestore.FieldValue;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import static androidx.core.content.ContextCompat.startActivity;

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

        fb.setDocument(documentPath, dataToSave);
    }

    public void openMainUi(Context currentActivity) {
        Intent intent = new Intent(currentActivity, AdminActivity.class);
        currentActivity.startActivity(intent);
    }

}