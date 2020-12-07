package com.example.servicenovigrad;

import com.example.servicenovigrad.accounts.AccountType;
import com.example.servicenovigrad.accounts.ClientAccount;

import org.junit.Test;

import static org.junit.Assert.*;

public class ClientAccountTest {
    ClientAccount test = new ClientAccount("testUsername", "testFirstName","testLastName", "testEmail", "testUID");

    @Test
    public void checkRealGetAccountType() {
        assertEquals(AccountType.CLIENT, test.getAccountType());
    }

    @Test
    public void checkFakeGetAccountType() {
        assertNotEquals(AccountType.EMPLOYEE, test.getAccountType());
    }

    @Test
    public void checkRealGetRole() {
        assertEquals("client", test.getRole());
    }

    @Test
    public void checkFakeGetRole() {
        assertNotEquals("fakeClient", test.getRole());
    }

    @Test
    public void checkRealToString() {
        assertEquals("Username: testUsername, Email: testEmail, Account Type: CLIENT", test.toString());
    }

    @Test
    public void checkFakeToString() {
        assertNotEquals("Username: fakeUsername, Email: fakeEmail, Account Type: EMPLOYEE", test.toString());
    }
}