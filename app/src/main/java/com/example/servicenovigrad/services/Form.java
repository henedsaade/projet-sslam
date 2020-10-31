package com.example.servicenovigrad.services;

import java.util.HashMap;
import java.util.List;

public class Form extends HashMap<String, String> {
    public Form(List<String> fields) {
        super();

        for (int i=0; i<fields.size(); i++) {
            put(fields.get(i), null);
        }
    }
}
