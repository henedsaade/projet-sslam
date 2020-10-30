package com.example.servicenovigrad.accounts;

import com.google.firebase.firestore.FieldValue;

public abstract class Account extends Object {
    protected static final String TAG = "[CONSOLE]";
    protected static final String firestoreUsersRoute = "users/";
    protected String userName, firstName, lastName, email, uid;

    public abstract void saveAccountToFirestore(FbWrapper fb, FieldValue timestamp);
    
    public String getEmail()  {
        return this.email;
    };
    
    public String getUserName()  {
        return this.userName;
    };

    public String getUid()  {
        return this.uid;
    };

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public abstract AccountType getAccountType();

    public abstract String getRole();

    /* Use this method ONLY for sign up!
     * */
    public static Account accountFromType(String userName, String firstName, String lastName, String email, String uid, AccountType accountType) {
        switch (accountType.ordinal()) {
            case 0:
                return new AdminAccount(userName, firstName, lastName, email, uid);
            case 1:
                return new EmployeeAccount(userName, firstName, lastName, email, uid);
            case 2:
                return new ClientAccount(userName, firstName, lastName, email, uid);
            default:
                return null;
        }
    }
}

