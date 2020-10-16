package com.example.servicenovigrad;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
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
            System.out.println(currentUser.getDisplayName());
        }

//        handleSignUp("John Doe","test@gmail.com", "abcdef12345", AccountType.EMPLOYEE);
        //saveAccountToFirestore(new Account("name", "email@gmail.com", "123124124121", AccountType.CLIENT));
        handleSignIn("test@gmail.com", "abcdef12345", AccountType.EMPLOYEE);

        //updateUI(currentUser);
    }

    public void saveAccountToFirestore(FirebaseFirestore mFirestore) {
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put()
//        mFirestore.document("services/template").addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
//                if (documentSnapshot.exists()) {
//                    // Map<String, Object> data = documentSnapshot.getData();
//                    ArrayList<String> docs = (ArrayList<String>) documentSnapshot.get("docs");
//                    System.out.println("Required document: " + docs.get(0));
//                }
//            }
//        });
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

                    UserProfileChangeRequest.Builder request = new UserProfileChangeRequest.Builder();
                    request = request.setDisplayName(userName);
                    UserProfileChangeRequest displayNameRequest = request.build();
                    user.updateProfile(displayNameRequest);

                    currentUser = new Account(userName, email, user.getUid(), accountType);
                    System.out.println("Username: " + currentUser.getUserName() + " Email: " + currentUser.getEmail() + " UID: " + currentUser.getUid());
                    saveAccountToFirestore(currentUser);
                    //updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    System.out.println("createUserWithEmail:failure");
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
                    currentUser = new Account(user.getDisplayName(), user.getEmail(), user.getUid(), accountType);
                    System.out.println("Username: " + currentUser.getUserName() + " Email: " + currentUser.getEmail() + " UID: " + currentUser.getUid());
                    //updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    System.out.println("signInWithEmail:failure");
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
//                System.out.println("Successfully signed out.");
//                currentUser = null;
//            }
//        });
//    }
}