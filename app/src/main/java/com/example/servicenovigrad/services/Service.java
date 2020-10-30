package com.example.servicenovigrad.services;

public class Service {

    private ServiceType type;
    private String [] informations;
    private String [] document;

    public Service(ServiceType aType){
        this.type = aType;

        if(type == ServiceType.DRIVER_LICENSE){
            informations = new String [5];
            informations[0] = ("prénom");
            informations[1] = ("nom");
            informations[2] = ("date de naissance");
            informations[3] = ("addresse");
            informations[4] = ("type de permis");

            document = new String[1];
            document [0] = "preuve de domicile ( une image d'un relevé bancaire ou d'une facture d'électricité indiquant l'adresse)";
        }
        else {
            informations = new String [4];
            informations[0] = ("prénom");
            informations[1] = ("nom");
            informations[2] = ("date de naissance");
            informations[3] = ("addresse");

            document = new String[2];
            document [0] = "preuve de domicile ( une image d'un relevé bancaire ou d'une facture d'électricité indiquant l'adresse)";
            if(type == ServiceType.HEALTH_CARD) {
                document [1] = "preuve de statut (Une image d'une carte de résidence permanente au Canada ou d'un passeport Canadien)";
            }
            else if (type == ServiceType.ID_WITH_PICTURE) {
                document [1] = "une photo du client";
            }
        }


    }

    public ServiceType getServiceType () {
        return this.type;
    }

    public String getAllInformations () {
        String res = "";
        for (int i = 0; i < informations.length; i++) {
            res = res + "/n" + informations[i];
        }

        return res;
    }

    public String getAllDocuments () {
        String res = "";
        for (int i = 0 ; i < document.length ; i++) {
            res= res+ "/n" + document [i];
        }

        return res;
    }
}
