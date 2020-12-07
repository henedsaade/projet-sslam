package com.example.servicenovigrad;

import com.example.servicenovigrad.activities.UploadPDF;

import org.junit.Test;

import static org.junit.Assert.*;

public class UploadPDFTest {
    UploadPDF test = new UploadPDF("testName", "testURL");

    @Test
    public void getRealName() {
        assertEquals("testName", test.getName());
    }

    @Test
    public void getFakeName() {
        assertNotEquals("fakeName", test.getName());
    }

    @Test
    public void getRealUrl() {
        assertEquals("testURL", test.getUrl());
    }

    @Test
    public void getFakeUrl() {
        assertNotEquals("fakeURL", test.getUrl());
    }
}
