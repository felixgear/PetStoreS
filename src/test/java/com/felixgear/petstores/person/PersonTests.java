package com.felixgear.petstores.person;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.felixgear.petstores.Base;
import com.felixgear.petstores.GroupsEndpoints;
import com.felixgear.petstores.utils.Done;
import com.felixgear.petstores.utils.UnderConstruction;
import com.felixgear.petstores.utils.UnhandledError;

import io.restassured.response.Response;

public class PersonTests extends Base {
    private static final Logger logger = LoggerFactory.getLogger(PersonTests.class);

    @Done("----------------------------------------------------------------------------------------")
    @Test(enabled = false, description = "Test for successfully getting user by username", groups = {
	    GroupsEndpoints.POSITIVE, GroupsEndpoints.REGRESSION, GroupsEndpoints.SMOKE,
	    GroupsEndpoints.BEFORE_LOGOUT }, dependsOnMethods = "testCreatePersonSuccess")
    public void testGetUserByUsernameSuccess() {
	logger.info("Modified user is: " + person.getUserName());
	Response response = getUrl(GET_USER_BY_USERNAME, person.getUserName());
	assertions.assertReturnCode(response, 200);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for failure when getting user by invalid username", groups = { GroupsEndpoints.NEGATIVE,
	    GroupsEndpoints.REGRESSION })
    public void testGetUserByUsernameFailure() {
	String username = "invalid";
	Response response = getUrl(GET_USER_BY_USERNAME, username);
	assertions.assertReturnCode(response, 404);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for successfully creating an user", groups = { GroupsEndpoints.NEGATIVE,
	    GroupsEndpoints.REGRESSION, GroupsEndpoints.SMOKE, GroupsEndpoints.BEFORE_LOGOUT })
    public void testCreatePersonSuccess() {
	person.setId(faker.number().numberBetween(1000000, 2000000));
	person.setFirstName(faker.name().firstName());
	person.setLastName(faker.name().lastName());
	person.setUserName(person.getFirstName().toLowerCase() + "." + person.getLastName().toLowerCase());
	person.setEmail(person.getUserName() + "@example.eu");
	person.setPassword("zero");
	person.setPhone(faker.phoneNumber().cellPhone());
	person.setUserStatus(faker.number().numberBetween(1, 5));
	Response response = postUrl(CREATE_USER, person);
	assertions.assertReturnCode(response, 200);
    }

    @UnderConstruction("This method is not fully implemented yet.")
    @Test(enabled = false, dependsOnMethods = "testCreatePersonSuccess")
    public void testCreatePersonFailure() throws JsonProcessingException {
	ObjectMapper mapper = new ObjectMapper();
	String jsonString = mapper.writeValueAsString(person);
	String replaceable = String.format("\"id\":%d", person.getId());
	jsonString = jsonString.replace(replaceable, "\"id\":\"invalid_id\"");
	Person invalidPet = mapper.readValue(jsonString, Person.class);
	Response response = postUrl(CREATE_USER, invalidPet);
	assertions.assertReturnCode(response, 405);
    }

    @UnderConstruction("This method is not fully implemented yet.")
    @Test(description = "Test for successfully updating an user", dependsOnGroups = GroupsEndpoints.BEFORE_LOGOUT)
    public void testUpdatePersonSuccess() {
	person.setPhone(faker.phoneNumber().cellPhone());
	Response response = putUrl(UPDATE_USER, person, Integer.toString(person.getId()));
	assertions.assertReturnCode(response, 200);
    }

    @UnderConstruction("This method is not fully implemented yet.")
    @Test(enabled = false, dependsOnMethods = "testCreatePersonSuccess")
    public void testUpdatePersonFailure() {
	person.setLastName(faker.name().fullName());
	Response response = putUrl(UPDATE_USER, person, Integer.toString(person.getId()));
	assertions.assertReturnCode(response, 200);
    }

    @UnderConstruction("This method is not fully implemented yet.")
    @Test(enabled = false)
    public void testDeletePersonSuccess() {
	Response response = deleteUrl(DELETE_USER, "john");
	assertions.assertReturnCode(response, 200);
    }

    @UnderConstruction("This method is not fully implemented yet.")
    @Test(dependsOnMethods = "testCreatePersonSuccess")
    public void testDeletePersonFailure() {
	Response response = deleteUrl(DELETE_USER, "invalidUser");
	assertions.assertReturnCode(response, 404);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(enabled = false, description = "Test for successfully creating users with array", groups = {
	    GroupsEndpoints.POSITIVE, GroupsEndpoints.REGRESSION, GroupsEndpoints.SMOKE })
    public void testCreateUsersWithArraySuccess() {
	String users = "[{\"id\": 1, \"username\": \"user1\", \"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\", \"password\": \"password1\", \"phone\": \"1234567890\", \"userStatus\": 1}]";
	Response response = postUrl(CREATE_USER_WITH_ARRAY, users);
	assertions.assertReturnCode(response, 200);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(enabled = false, description = "Test for successfully logging in a user", groups = { GroupsEndpoints.POSITIVE,
	    GroupsEndpoints.REGRESSION, GroupsEndpoints.SMOKE }, dependsOnMethods = "testLogoutUserSuccess")
    public void testLoginUserSuccess() {
	Map<String, String> login = new HashMap<>();
	login.put("username", "test");
	login.put("password", "abc123");
	Response response = getUrl(LOGIN_USER, login);
	assertions.assertReturnCode(response, 200);
	Assert.assertTrue(response.getBody().asString().contains("logged in user session"));
    }

    @Done("----------------------------------------------------------------------------------------")
    @UnhandledError("This method doesn't throws an error when I try to login with any strings!")
    @Test(enabled = false, description = "Test for failure when logging in a user with invalid credentials", groups = {
	    GroupsEndpoints.NEGATIVE, GroupsEndpoints.REGRESSION })
    public void testLoginUserFailure() {
	String queryParams = "?username=invalid&password=invalid";
	Response response = getUrl(LOGIN_USER, queryParams);
	assertions.assertReturnCode(response, 200);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(enabled = false, description = "Test for successfully logging out a user", groups = {
	    GroupsEndpoints.POSITIVE, GroupsEndpoints.REGRESSION, GroupsEndpoints.SMOKE })
    public void testLogoutUserSuccess() {
	Response response = getUrl(LOGOUT_USER);
	assertions.assertReturnCode(response, 200);
    }

    // It doesn't make sense ////////////////////////
    @Test(description = "Test for failure when logging out a user with invalid session", groups = {
	    GroupsEndpoints.NEGATIVE, GroupsEndpoints.REGRESSION })
    // It doesn't make sense ////////////////////////
    public void testLogoutUserFailure() {
	// It doesn't make sense ////////////////////
    }
}
