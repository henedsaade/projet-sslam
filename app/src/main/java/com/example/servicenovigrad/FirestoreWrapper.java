package com.example.servicenovigrad;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Map;

public class FirestoreWrapper {
    private static final String TAG = "[FIRESTORE]";
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static int flag = 0;

    public static void setDocument(String documentPath, Map<String, Object> dataToSave) throws FirebaseFirestoreException {
        db.document(documentPath).set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Document has been saved!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Document was not saved!");
                flag = 1;
            }
        });

        if (flag == 1) {
            flag = 0;
            throw new FirebaseFirestoreException("Document at " + documentPath + " already exists.", FirebaseFirestoreException.Code.ALREADY_EXISTS);
        }
    }

//    public static Map<String, Object> getDocument(String documentPath) {
//        db.document(documentPath).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                        documentData = document.getData();
//                    } else {
//                        Log.d(TAG, "No such document");
//                        documentData = null;
//                    }
//                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
//                }
//            }
//        });
//
//        return documentData;
//    }

}

