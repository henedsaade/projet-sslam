package com.example.servicenovigrad.accounts;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Map;

public class FbWrapper {
    private static FbWrapper mInstance = null;
    private static final String TAG = "[FIREBASE]";
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private Account currentUser;

    protected FbWrapper() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public static synchronized FbWrapper getInstance() {
        if(null == mInstance){
            mInstance = new FbWrapper();
        }
        return mInstance;
    }

    public Task<DocumentSnapshot> initiateUser() {
        // Sign in success, update UI with the signed-in user's information
        final FirebaseUser fUser = auth.getCurrentUser();

        if (fUser != null) {
            // Get instance
            return db.document(Account.firestoreUsersRoute + fUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        if (document.exists()) {
                            String role = (String) document.get("role");
                            String firstName = (String) document.get("firstName");
                            String lastName = (String) document.get("lastName");

                            switch (role) {
                                case "admin":
                                    currentUser = new AdminAccount(fUser.getDisplayName(), firstName, lastName, fUser.getEmail(), fUser.getUid());
                                    break;
                                case "employee":
                                    currentUser = new EmployeeAccount(fUser.getDisplayName(), firstName, lastName, fUser.getEmail(), fUser.getUid());
                                    break;
                                case "client":
                                    currentUser = new ClientAccount(fUser.getDisplayName(), firstName, lastName, fUser.getEmail(), fUser.getUid());
                                    break;
                                default:
                                    handleSignOut();
                                    break;
                            }

                            // failed to find user in firestore
                        } else {
                            Log.d(TAG, "No such document");
                            handleSignOut();
                        }

                        Log.d(TAG, currentUser.toString());
                    } else {
                        // async task failed
                        Log.d(TAG, "get failed with ", task.getException());
                        handleSignOut();
                    }
                }
            });
        } else {
            return null;
        }
    }

    public Task<Void> setDocument(String documentPath, Map<String, Object> dataToSave) throws FirebaseFirestoreException {
        return db.document(documentPath).set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Document has been saved!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Document was not saved!");
            }
        });
    }

    public Task<AuthResult> handleSignUp(final String userName, final String firstName, final String lastName, final String email, String password, final AccountType accountType) {
        return auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser fUser = auth.getCurrentUser();

                            // Change display name of firebase user
                            UserProfileChangeRequest.Builder request = new UserProfileChangeRequest.Builder();
                            request = request.setDisplayName(userName);
                            UserProfileChangeRequest displayNameRequest = request.build();
                            fUser.updateProfile(displayNameRequest);

                            // Get instance
                            currentUser = Account.accountFromType(userName, firstName, lastName, email, fUser.getUid(), accountType);
//                            Log.d(TAG, currentUser.toString());

                            // Save user data to Firestore
                            currentUser.saveAccountToFirestore(mInstance, FieldValue.serverTimestamp());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "createUserWithEmail:failure");
                        }
                    }
                });
    }

    public Task<AuthResult> handleSignIn(String email, String password) {
        return auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, "signInWithEmail:failure");
                        }
                    }
                });
    }

    public void handleSignOut() {
        auth.signOut();
        currentUser = null;
    }

    public Account getCurrentUser() {
        return this.currentUser;
    }

    public boolean isUserLoggedIn() {
        return auth.getCurrentUser() != null;
    }

}

