package com.example.servicenovigrad.accounts;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.example.servicenovigrad.fb.FbWrapper;
import com.google.firebase.firestore.FieldValue;

public abstract class Account extends Object {
    protected static final String TAG = "[CONSOLE]";
    public static final String firestoreUsersRoute = "users/";
    protected String userName, firstName, lastName, email, uid;

    public Account(String userName, String firstName, String lastName, String email, String uid) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.uid = uid;
    } 

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

    public abstract void openMainUi(Context currentActivity);

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

