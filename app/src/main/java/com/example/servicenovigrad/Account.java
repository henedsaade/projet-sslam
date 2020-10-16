package com.example.servicenovigrad;

import com.google.firebase.firestore.FirebaseFirestore;

public abstract class Account {
    protected static final String TAG = "[CONSOLE]";
    public static final String firestoreUsersRoutes[] = new String[] {"allUsers/admins/adminUsers/", "allUsers/employees/employeeUsers/", "allUsers/clients/clientUsers/"};
    protected String userName;
    protected String email;
    protected String uid;
    protected AccountType accountType;

    public Account(String userName, String email, String uid, AccountType accountType){
        this.userName = userName;
        this.email = email;
        this.uid = uid;
        this.accountType = accountType;
    }

    public abstract void saveAccountToFirestore(FirebaseFirestore mFirestore, long timestamp);
    
    public String getEmail(){
        return this.email;
    }
    
    public String getUserName() {
        return this.userName;
    }

    public String getUid() {
        return this.uid;
    }

    public AccountType getType() {
        return this.accountType;
    }
}

