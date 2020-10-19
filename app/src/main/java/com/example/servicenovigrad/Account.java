package com.example.servicenovigrad;

import com.google.firebase.firestore.FieldValue;

public abstract class Account extends Object {
    protected static final String TAG = "[CONSOLE]";
    protected static final String firestoreUsersRoute = "users/";

    public abstract void saveAccountToFirestore(FbWrapper fb, FieldValue timestamp);
    
    public abstract String getEmail();
    
    public abstract String getUserName();

    public abstract String getUid();

    public abstract AccountType getAccountType();

    /* Use this method ONLY for sign up!
     * */
    public static Account accountFromType(String userName, String email, String uid, AccountType accountType) {
        switch (accountType.ordinal()) {
            case 0:
                return new AdminAccount(userName, email, uid);
            case 1:
                return new EmployeeAccount(userName, email, uid);
            case 2:
                return new ClientAccount(userName, email, uid);
            default:
                return null;
        }
    }
}

