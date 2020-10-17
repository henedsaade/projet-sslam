package com.example.servicenovigrad;

import com.google.firebase.firestore.FieldValue;

public abstract class Account {
    protected static final String TAG = "[CONSOLE]";
    protected static final String firestoreUsersRoute = "users/";

    public abstract void saveAccountToFirestore(FieldValue timestamp);
    
    public abstract String getEmail();
    
    public abstract String getUserName();

    public abstract String getUid();

    public abstract AccountType getAccountType();
}

