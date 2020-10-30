package com.example.servicenovigrad.services;

import android.net.Uri;

public class Document {
    // Uri indicates, where the image will be picked from
    private Uri storagePath;
    private String documentName;

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
