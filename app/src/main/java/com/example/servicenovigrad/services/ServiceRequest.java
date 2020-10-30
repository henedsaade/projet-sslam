package com.example.servicenovigrad.services;

import java.util.HashMap;

public class ServiceRequest extends Service {
    private Form form;
    private HashMap<String, Document> documents= new HashMap<String, Document>();
    private String clientUid;

    public ServiceRequest(String serviceName, String[] formFields, String[] documentsNames, String clientUid) {
        super(serviceName, formFields, documentsNames);

        this.clientUid = clientUid;

        // init form
        form = new Form(formFields);

        // init documents
        for (int i=0; i<documentsNames.length; i++) {
            Document docToPut = new Document(documentsNames[i], null);
            documents.put(documentsNames[i], docToPut);
        }
    }

    public void setFormValue(String formField, String value) {
        form.put(formField, value);
    }

    public String getFormValue(String formField) {
        return form.get(formField);
    }

    public String getClientUid() {
        return clientUid;
    }
}
