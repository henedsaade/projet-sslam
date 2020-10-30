package com.example.servicenovigrad.services;

import java.util.HashMap;

public class Form {
    private HashMap<String, String> map;

    public Form(String[] fields) {
        this.map = new HashMap<String, String>();

        for (int i=0; i<fields.length; i++) {
            this.map.put(fields[i], null);
        }
    }

    public Form(HashMap<String, String> map) {
        this.map = map;
    }

    public void setEntry(String key, String value) {
        this.map.put(key, value);
    }

    public String getValue(String key) {
        return this.map.get(key);
    }

    public HashMap<String, String> getMap() {
        return this.map;
    }
}
