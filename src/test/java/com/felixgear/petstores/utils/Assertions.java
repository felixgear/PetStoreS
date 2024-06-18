package com.felixgear.petstores.utils;

import org.testng.Assert;

import io.restassured.response.Response;

public class Assertions {
    public void assertReturnCode(Response response, Integer code) {
	Assert.assertEquals((Integer) response.getStatusCode(), code);
    }

    public void assertReturnCodeDifferentThanThis(Response response, Integer code) {
	Assert.assertNotEquals((Integer) response.getStatusCode(), code);
    }

    public void assertElementIdScheme(Integer id) {
	Boolean idIsInValidRange = (id > 0 && id < Integer.MAX_VALUE);
	Assert.assertTrue(idIsInValidRange, "Element IDs always should be greater than 0");
    }

    public void assertFormData(String[] formData, String[] expectedData) {
	Integer formDataLength = formData.length;
	Integer expectedDataLength = expectedData.length;
	Assert.assertEquals(formDataLength, expectedDataLength, String.format(
		"Form data size should be equal with expected data size: %d <> %d", formDataLength, expectedDataLength));
	for (int i = 0; i < expectedData.length; i++) {
	    Assert.assertEquals(formData[i], expectedData[i]);
	}
    }
}
