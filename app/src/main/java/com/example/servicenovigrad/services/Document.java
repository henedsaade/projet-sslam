package com.example.servicenovigrad.services;

import android.net.Uri;

// Image upload activity tutorial at https://www.geeksforgeeks.org/android-how-to-upload-an-image-on-firebase-storage/
public class Document {
    // Uri indicates, where the image will be picked from
    private String documentName;
    private Uri storagePath;

    public Document() {

    }

    public Document(String documentName, Uri storagePath) {
        this.documentName = documentName;
        this.storagePath = storagePath;
    }

    public void setDocumentName(String documentName) { this.documentName = documentName; }

    public void setStoragePath(Uri storagePath) { this.storagePath = storagePath; }

    public String getDocumentName() {
        return this.documentName;
    }

    public Uri getStoragePath() {
        return this.storagePath;
    }
}
