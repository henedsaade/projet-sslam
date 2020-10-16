package com.example.servicenovigrad;

import com.google.firebase.firestore.FirebaseFirestore;

public class Account {
    public static final String firestoreUsersRoutes[] = new String[] {"allUsers/admins/adminUsers", "allUsers/employees/employeeUsers", "allUsers/clients/clientUsers"};
    private String userName;
    private String email;
    private String uid;
    private AccountType accountType;

    public Account(String userName, String email, String uid, AccountType accountType){
        this.userName = userName;
        this.email = email;
        this.uid = uid;
        this.accountType = accountType;
    }

    public abstract void saveAccountToFirestore(FirebaseFirestore mFirestore);
    
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

