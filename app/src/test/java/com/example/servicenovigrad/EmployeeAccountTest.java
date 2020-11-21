package com.example.servicenovigrad;

import com.example.servicenovigrad.accounts.AccountType;
import com.example.servicenovigrad.accounts.EmployeeAccount;

import org.junit.Test;

import static org.junit.Assert.*;

public class EmployeeAccountTest {

    EmployeeAccount test = new EmployeeAccount("testUsername", "testFirstName","testLastName", "testEmail", "testUID");

    @Test
    public void checkGetAccountType() {
        assertEquals(AccountType.EMPLOYEE, test.getAccountType());
    }

    @Test
    public void checkGetRole() {
        assertEquals("employé", test.getRole());
    }

    @Test
    public void checkToString() {
        assertEquals("Username: testUsername, Email: testEmail, Account Type: EMPLOYEE", test.toString());
    }
}

