package com.felixgear.petstores.person;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.felixgear.petstores.Base;
import com.felixgear.petstores.GroupsEndpoints;
import com.felixgear.petstores.utils.Done;
import com.felixgear.petstores.utils.UnderConstruction;
import com.felixgear.petstores.utils.UnhandledError;

import io.restassured.response.Response;

public class PersonTests extends Base {
    private static final String contentType = "application/json; charset=UTF-8";

    @Done("----------------------------------------------------------------------------------------")
    @Test(enabled = false, description = "Test for successfully getting user by username", groups = { GroupsEndpoints.POSITIVE,
	    GroupsEndpoints.REGRESSION, GroupsEndpoints.SMOKE })
    public void testGetUserByUsernameSuccess() {
	String username = "user";
	Response response = getUrl(GET_USER_BY_USERNAME, username);
	assertions.assertReturnCode(response, 200);
	Assert.assertEquals(response.jsonPath().getString("username"), username);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for failure when getting user by invalid username", groups = {
	    GroupsEndpoints.NEGATIVE, GroupsEndpoints.REGRESSION })
    public void testGetUserByUsernameFailure() {
	String username = "invalid";
	Response response = getUrl(GET_USER_BY_USERNAME, username);
	assertions.assertReturnCode(response, 404);
    }

    @UnderConstruction("This method is not fully implemented yet.")
    @Test(enabled = false)
    public void testCreatePersonSuccess() {
	String body = "{\"id\":1,\"username\":\"john\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"password\":\"password123\",\"phone\":\"1234567890\",\"userStatus\":0}";
	Response response = postUrl(CREATE_USER, contentType, body);
	assertions.assertReturnCode(response, 200);
    }

    @UnderConstruction("This method is not fully implemented yet.")
    @Test(enabled = false)
    public void testCreatePersonFailure() {
	String body = "{\"id\":\"invalidId\",\"username\":\"john\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"password\":\"password123\",\"phone\":\"1234567890\",\"userStatus\":0}";
	Response response = postUrl(CREATE_USER, contentType, body);
	Assert.assertNotEquals(response.getStatusCode(), 200);
    }

    @UnderConstruction("This method is not fully implemented yet.")
    @Test(enabled = false)
    public void testUpdatePersonSuccess() {
	String body = "{\"id\":1,\"username\":\"john_updated\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"password\":\"password123\",\"phone\":\"1234567890\",\"userStatus\":0}";
	Response response = putUrl(UPDATE_USER.replace("{username}", "john"), contentType, body);
	assertions.assertReturnCode(response, 200);
    }

    @UnderConstruction("This method is not fully implemented yet.")
    @Test(enabled = false)
    public void testUpdatePersonFailure() {
	String body = "{\"id\":\"invalidId\",\"username\":\"john_updated\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john.doe@example.com\",\"password\":\"password123\",\"phone\":\"1234567890\",\"userStatus\":0}";
	Response response = putUrl(UPDATE_USER.replace("{username}", "invalidUser"), contentType, body);
	assertions.assertReturnCode(response, 200);
    }

    @UnderConstruction("This method is not fully implemented yet.")
    @Test(enabled = false)
    public void testDeletePersonSuccess() {
	Response response = deleteUrl(DELETE_USER, "john");
	assertions.assertReturnCode(response, 200);
    }

    @UnderConstruction("This method is not fully implemented yet.")
    @Test(enabled = false)
    public void testDeletePersonFailure() {
	Response response = deleteUrl(DELETE_USER, "invalidUser");
	assertions.assertReturnCode(response, 200);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(enabled = false, description = "Test for successfully creating users with array", groups = { GroupsEndpoints.POSITIVE,
	    GroupsEndpoints.REGRESSION, GroupsEndpoints.SMOKE })
    public void testCreateUsersWithArraySuccess() {
	String users = "[{\"id\": 1, \"username\": \"user1\", \"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\", \"password\": \"password1\", \"phone\": \"1234567890\", \"userStatus\": 1}]";
	Response response = postUrl(CREATE_USER_WITH_ARRAY, contentType, users);
	assertions.assertReturnCode(response, 200);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for failure when creating users with array with invalid data", groups = {
	    GroupsEndpoints.NEGATIVE, GroupsEndpoints.REGRESSION })
    public void testCreateUsersWithArrayFailure() {
	String users = "[{\"id\": \"invalid\", \"username\": \"\", \"firstName\": \"\", \"lastName\": \"\", \"email\": \"\", \"password\": \"\", \"phone\": \"\", \"userStatus\": \"invalid\"}]";
	Response response = postUrl(CREATE_USER_WITH_ARRAY, contentType, users);
	assertions.assertReturnCode(response, 405);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(enabled = false, description = "Test for successfully creating users with list", groups = { GroupsEndpoints.POSITIVE,
	    GroupsEndpoints.REGRESSION, GroupsEndpoints.SMOKE })
    public void testCreateUsersWithListSuccess() {
	String users = "[{\"id\": 2, \"username\": \"user2\", \"firstName\": \"Jane\", \"lastName\": \"Doe\", \"email\": \"jane.doe@example.com\", \"password\": \"password2\", \"phone\": \"0987654321\", \"userStatus\": 1}]";
	Response response = postUrl(CREATE_USER_WITH_LIST, contentType, users);
	assertions.assertReturnCode(response, 200);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(enabled = false, description = "Test for failure when creating users with list with invalid data", groups = {
	    GroupsEndpoints.NEGATIVE, GroupsEndpoints.REGRESSION })
    public void testCreateUsersWithListFailure() {
	String users = "[{\"id\": \"invalid\", \"username\": \"\", \"firstName\": \"\", \"lastName\": \"\", \"email\": \"\", \"password\": \"\", \"phone\": \"\", \"userStatus\": \"invalid\"}]";
	Response response = postUrl(CREATE_USER_WITH_LIST, contentType, users);
	assertions.assertReturnCode(response, 200);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(enabled = false, description = "Test for successfully logging in a user", groups = { GroupsEndpoints.POSITIVE,
	    GroupsEndpoints.REGRESSION, GroupsEndpoints.SMOKE })
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
    @Test(enabled = false, description = "Test for failure when logging in a user with invalid credentials", groups = { GroupsEndpoints.NEGATIVE,
	    GroupsEndpoints.REGRESSION })
    public void testLoginUserFailure() {
	String queryParams = "?username=invalid&password=invalid";
	Response response = getUrl(LOGIN_USER, queryParams);
	assertions.assertReturnCode(response, 200);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(enabled = false, description = "Test for successfully logging out a user", groups = { GroupsEndpoints.POSITIVE,
	    GroupsEndpoints.REGRESSION, GroupsEndpoints.SMOKE })
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
