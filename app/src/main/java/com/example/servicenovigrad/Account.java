package com.example.servicenovigrad;

import com.google.firebase.firestore.FirebaseFirestore;

public abstract class Account {
    protected static final String TAG = "[CONSOLE]";

    public abstract void saveAccountToFirestore(FirebaseFirestore mFirestore, long timestamp);
    
    public abstract String getEmail();
    
    public abstract String getUserName();

    public abstract String getUid();
}

