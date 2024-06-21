package com.felixgear.petstores;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.util.Map;
import java.util.Optional;

import com.felixgear.petstores.person.Person;
import com.felixgear.petstores.pet.Pet;
import com.felixgear.petstores.store.Order;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class CommonFunctions extends GroupsEndpoints {
    private static final String baseUrl = "https://petstore.swagger.io/v2";

    private Optional<Object> pathParam = Optional.ofNullable(null);
    private RequestSpecification requestSpecification;

    // ----------------------------------GET----------------------------------
    // GET_INVENTORY, LOGOUT_USER
    protected Response getUrl(String endpoint) {
	requestSpecification = given().relaxedHTTPSValidation();
	return getLoggedAndExtractedResponse(requestSpecification, endpoint, pathParam);
    }

    // FIND_PET_BY_ID, FIND_PURCHASE_ORDER_BY_ID
    protected Response getUrl(String endpoint, Integer pathParam) {
	requestSpecification = given().relaxedHTTPSValidation();
	return getLoggedAndExtractedResponse(requestSpecification, endpoint, pathParam);
    }

    // GET_USER_BY_USERNAME
    protected Response getUrl(String endpoint, String pathParam) {
	requestSpecification = given().relaxedHTTPSValidation();
	return getLoggedAndExtractedResponse(requestSpecification, endpoint, pathParam);
    }

    // FIND_PETS_BY_TAGS, FIND_PETS_BY_STATUS, LOGIN_USER
    protected Response getUrl(String endpoint, Map<String, String> queryParam) {
	requestSpecification = given().relaxedHTTPSValidation().params(queryParam);
	return getLoggedAndExtractedResponse(requestSpecification, endpoint, pathParam);
    }

    // ----------------------------------POST---------------------------------
    // UPLOAD_IMAGE
    protected Response postUrl(String endpoint, Integer pathParam, File imageFile, String additionalMetadata) {
	requestSpecification = given().relaxedHTTPSValidation().contentType("multipart/form-data; charset=UTF-8")
		.accept("application/json; charset=UTF-8").multiPart("file", imageFile)
		.formParam("additionalMetadata", additionalMetadata);
	return postLoggedAndExtractedResponse(requestSpecification, endpoint, pathParam);
    }

    // CREATE_USER_WITH_LIST, CREATE_USER_WITH_ARRAY
    protected Response postUrl(String endpoint, String body) {
	requestSpecification = given().relaxedHTTPSValidation().contentType("application/json; charset=UTF-8")
		.accept("application/json; charset=UTF-8").body(body);
	return postLoggedAndExtractedResponse(requestSpecification, endpoint, pathParam);
    }

    // CREATE_USER
    protected Response postUrl(String endpoint, Person body) {
	requestSpecification = given().relaxedHTTPSValidation().contentType("application/json; charset=UTF-8")
		.accept("application/json; charset=UTF-8").body(body);
	return postLoggedAndExtractedResponse(requestSpecification, endpoint, pathParam);
    }

    // ADD_NEW_PET
    protected Response postUrl(String endpoint, Pet body) {
	requestSpecification = given().relaxedHTTPSValidation().contentType("application/json; charset=UTF-8")
		.accept("application/json; charset=UTF-8").body(body);
	return postLoggedAndExtractedResponse(requestSpecification, endpoint, pathParam);
    }

    // PLACE_ORDER
    protected Response postUrl(String endpoint, Order body) {
	requestSpecification = given().relaxedHTTPSValidation().contentType("application/json; charset=UTF-8")
		.accept("application/json; charset=UTF-8").body(body);
	return postLoggedAndExtractedResponse(requestSpecification, endpoint, pathParam);
    }

    // UPDATE_PET_WITH_FORM
    protected Response postUrl(String endpoint, Integer pathParam, String petName, String petStatus) {
	EncoderConfig encoderConfig = new EncoderConfig();
	encoderConfig.encodeContentTypeAs("multipart/form-data", ContentType.TEXT);

	requestSpecification = RestAssured.given().relaxedHTTPSValidation()
		.config(RestAssured.config().encoderConfig(encoderConfig))
		.contentType("multipart/form-data; charset=UTF-8").accept("multipart/form-data; charset=UTF-8")
		.multiPart("name", petName).multiPart("status", petStatus);
	return postLoggedAndExtractedResponse(requestSpecification, endpoint, pathParam);
    }

    // ----------------------------------PUT----------------------------------
    // UPDATE_USER
    protected Response putUrl(String endpoint, Person body, String pathParam) {
	requestSpecification = given().relaxedHTTPSValidation().contentType("application/json; charset=UTF-8")
		.accept("application/json; charset=UTF-8").body(body);
	return putLoggedAndExtractedResponse(requestSpecification, endpoint, pathParam);
    }

    // UPDATE_PET
    protected Response putUrl(String endpoint, Pet body) {
	requestSpecification = given().relaxedHTTPSValidation().contentType("application/json; charset=UTF-8")
		.accept("application/json; charset=UTF-8").body(body);
	return putLoggedAndExtractedResponse(requestSpecification, endpoint, pathParam);
    }

    // UPDATE_USER
    protected Response putUrl(String endpoint, String body, String pathParam) {
	requestSpecification = given().relaxedHTTPSValidation().contentType("application/json; charset=UTF-8")
		.accept("application/json; charset=UTF-8").body(body);
	return putLoggedAndExtractedResponse(requestSpecification, endpoint, pathParam);
    }

    // ----------------------------------DELETE-------------------------------
    // DELETE_PURCHASE_ORDER_BY_ID
    protected Response deleteUrl(String endpoint, Integer pathParam) {
	requestSpecification = given().relaxedHTTPSValidation();
	return deleteLoggedAndExtractedResponse(requestSpecification, endpoint, pathParam);
    }

    // DELETE_USER
    protected Response deleteUrl(String endpoint, String pathParam) {
	requestSpecification = given().relaxedHTTPSValidation();
	return deleteLoggedAndExtractedResponse(requestSpecification, endpoint, pathParam);
    }

    // DELETE_PET
    protected Response deleteUrl(String endpoint, Integer pathParam, Map<String, String> headers) {
	requestSpecification = given().relaxedHTTPSValidation();
	return deleteLoggedAndExtractedResponse(requestSpecification, endpoint, pathParam);
    }

    // ----------------------------------HELPERS------------------------------
    protected Response getLoggedAndExtractedResponse(RequestSpecification requestSpecification, String endpoint,
	    Object pathParam) {
	if (pathParam == Optional.empty()) {
	    return requestSpecification.when().get(baseUrl + endpoint).then().log().ifValidationFails().extract()
		    .response();
	}
	return requestSpecification.when().get(baseUrl + endpoint, pathParam).then().log().ifValidationFails().extract()
		.response();
    }

    protected Response postLoggedAndExtractedResponse(RequestSpecification requestSpecification, String endpoint,
	    Object pathParam) {
	if (pathParam == Optional.empty()) {
	    return requestSpecification.when().post(baseUrl + endpoint).then().log().ifValidationFails().extract()
		    .response();
	}
	return requestSpecification.when().post(baseUrl + endpoint, pathParam).then().log().ifValidationFails()
		.extract().response();
    }

    protected Response putLoggedAndExtractedResponse(RequestSpecification requestSpecification, String endpoint,
	    Object pathParam) {
	if (pathParam == Optional.empty()) {
	    return requestSpecification.when().put(baseUrl + endpoint).then().log().ifValidationFails().extract()
		    .response();
	}
	return requestSpecification.when().put(baseUrl + endpoint, pathParam).then().log().ifValidationFails().extract()
		.response();
    }

    protected Response deleteLoggedAndExtractedResponse(RequestSpecification requestSpecification, String endpoint,
	    Object pathParam) {
	if (pathParam == Optional.empty()) {
	    return requestSpecification.when().delete(baseUrl + endpoint).then().log().ifValidationFails().extract()
		    .response();
	}
	return requestSpecification.when().delete(baseUrl + endpoint, pathParam).then().log().ifValidationFails()
		.extract().response();
    }

    public Boolean checkSelectedGroups(String selectedGroups) {
	String selectedGroupsEnv = System.getenv("selected.groups");
	if (selectedGroupsEnv != null && !selectedGroupsEnv.isEmpty()) {
	    return selectedGroupsEnv.contains(selectedGroups);
	} else {
	    return true;
	}
    }

    protected static String getEnv(String variableName, String defaultValue) {
	String value = System.getenv(variableName);
	if (value == null || value.trim().isEmpty()) {
	    return defaultValue;
	}
	return value;
    }

    protected static String getEnv(String variableName) {
	return getEnv(variableName, null);
    }
}
