package com.example.servicenovigrad.activities;

public class UploadPDF {

    public String name;
    public String url;

    public UploadPDF(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return  url;
    }
}
