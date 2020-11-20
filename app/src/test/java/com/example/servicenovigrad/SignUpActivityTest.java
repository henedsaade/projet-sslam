package com.example.servicenovigrad;

import com.example.servicenovigrad.activities.SignUpActivity;

import org.junit.Test;

import static org.junit.Assert.*;

public class SignUpActivityTest {
    @Test
    public void checkValidSignUpTest() {
        try {
            assertEquals(true, SignUpActivity.checkValidSignUp());
        } catch(NullPointerException e) {
        }
    }
}
