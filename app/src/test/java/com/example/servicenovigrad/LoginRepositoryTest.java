package com.example.servicenovigrad;

import com.example.servicenovigrad.data.LoginRepository;

import org.junit.Test;

import static org.junit.Assert.*;

public class LoginRepositoryTest {
    @Test
    public void checkValidSignUpTest() {
        try {
            assertEquals(false, LoginRepository.isLoggedIn());
        } catch(NullPointerException e) {
        }
    }
}
