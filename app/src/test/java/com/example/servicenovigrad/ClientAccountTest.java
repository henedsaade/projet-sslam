package com.example.servicenovigrad;

import com.example.servicenovigrad.accounts.AccountType;
import com.example.servicenovigrad.accounts.ClientAccount;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClientAccountTest {
    ClientAccount test = new ClientAccount("testUsername", "testFirstName","testLastName", "testEmail", "testUID");

    @Test
    public void checkGetAccountType() {
        assertEquals(AccountType.CLIENT, test.getAccountType());
    }

    @Test
    public void checkGetRole() {
        assertEquals("client", test.getRole());
    }

    @Test
    public void checkToString() {
        assertEquals("Username: testUsername, Email: testEmail, Account Type: CLIENT", test.toString());
    }
}
