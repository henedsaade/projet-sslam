package com.example.servicenovigrad;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "[CONSOLE]";
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private Account currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d(TAG, currentUser.getDisplayName());
        }

        //handleSignUp("John Doe","test@gmail.com", "abcdef12345", AccountType.EMPLOYEE);
        //saveAccountToFirestore(new Account("name", "email@gmail.com", "123124124121", AccountType.CLIENT));
         handleSignIn("test@gmail.com", "abcdef12345", AccountType.EMPLOYEE);

        //updateUI(currentUser);
    }

    public Account newAccountFromType(String userName, String email, String uid, AccountType accountType) {
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

    public void handleSignUp(final String userName, final String email, String password, final AccountType accountType) {
        // check if email and password are valid

        mAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();

                    // Change display name of firebase user
                    UserProfileChangeRequest.Builder request = new UserProfileChangeRequest.Builder();
                    request = request.setDisplayName(userName);
                    UserProfileChangeRequest displayNameRequest = request.build();
                    user.updateProfile(displayNameRequest);

                    currentUser = newAccountFromType(userName, email, user.getUid(), accountType);
                    Log.d(TAG, "Username: " + currentUser.getUserName() + " Email: " + currentUser.getEmail() + " UID: " + currentUser.getUid());
                    currentUser.saveAccountToFirestore(mFirestore, user.getMetadata().getCreationTimestamp());
                    //updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.d(TAG, "createUserWithEmail:failure");
                    // Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                    //         Toast.LENGTH_SHORT).show();
                    //updateUI(null);
                }
            }
                // ...
        });
    }

    public void handleSignIn(String email, String password, final AccountType accountType) {
        // check if email and password are valid

        mAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information

                    FirebaseUser user = mAuth.getCurrentUser();
                    currentUser = newAccountFromType(user.getDisplayName(), user.getEmail(), user.getUid(), accountType);

                    Log.d(TAG, "Username: " + currentUser.getUserName() + " Email: " + currentUser.getEmail() + " UID: " + currentUser.getUid());
                    //updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.d(TAG, "signInWithEmail:failure");
                    // Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                    //         Toast.LENGTH_SHORT).show();
                    // updateUI(null);
                    // ...
                }

                // ...
            }
        });
    }

//    public void handleSignOut() {
//        mAuth.getCurrentUser().signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
//            public void onComplete(@NonNull Task<Void> task) {
//                Log.d(TAG, "Successfully signed out.");
//                currentUser = null;
//            }
//        });
//    }
}