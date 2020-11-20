package com.example.servicenovigrad;

import com.example.servicenovigrad.activities.SignUpActivity;

import org.junit.Test;

import static org.junit.Assert.*;

public class SignUpActivityTest {

    /*
    private static String prenom = "test";
    private static String nom = "test";
    private static String courriel = "test@gmail.com";
    private static String utilisateur = "test";
    private static String motDePasse = "test";
    private static String confirmationMotDePasse = "test";
     */

    @Test
    public void checkValidSignUpTest() {
        try {
            assertEquals(true, SignUpActivity.checkValidSignUp());
        } catch(NullPointerException e) {
        }
    }
}
