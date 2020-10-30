package com.example.servicenovigrad.services;

import java.util.HashMap;

public class Form extends HashMap<String, String> {
    public Form(String[] fields) {
        super();

        for (int i=0; i<fields.length; i++) {
            put(fields[i], null);
        }
    }
}
