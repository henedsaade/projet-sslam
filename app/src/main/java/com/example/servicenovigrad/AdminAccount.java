package com.example.servicenovigrad;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminAccount extends Account {

    public AdminAccount(String userName, String email, String uid, AccountType accountType) {
        super(userName, email, uid, accountType);
    }
    
    public void saveAccountToFirestore(FirebaseFirestore mFirestore, long timestamp) {
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put("email", this.email);
        dataToSave.put("createdAt", timestamp);
        mFirestore.document(Account.firestoreUsersRoutes[this.accountType.ordinal()] + this.uid).set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Document has been saved!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Document was not savec!");
            }
        });
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
}