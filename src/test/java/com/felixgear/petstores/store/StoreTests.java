package com.felixgear.petstores.store;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.felixgear.petstores.Base;
import com.felixgear.petstores.GroupsEndpoints;
import com.felixgear.petstores.utils.Done;
import com.felixgear.petstores.utils.SharedState;
import com.felixgear.petstores.utils.UnderConstruction;
import com.felixgear.petstores.utils.UnhandledError;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class StoreTests extends Base {
    private static final String contentType = "application/json; charset=UTF-8";

    @UnderConstruction("Error msg: java.lang.IllegalStateException: Cannot parse object because no XML deserializer found in classpath.")
    @Test(description = "Test for successfully placing an order", groups = { GroupsEndpoints.POSITIVE, GroupsEndpoints.REGRESSION, GroupsEndpoints.SMOKE })
    public void testPlaceOrderSuccess() {
	Order order = RestAssured.get("resources/forms/Order.json").as(Order.class);
	order.setPetId(5);
	order.setStatus("available");
	order.setComplete(false);
	Response response = postUrl(PLACE_ORDER, contentType, order);
	assertions.assertReturnCode(response, 200);
	int orderId = response.jsonPath().getInt("id");
	assertions.assertElementIdScheme(orderId);
	SharedState.setStoreId(orderId);
	response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema("responses/Order.json"));
    }

    @Done("----------------------------------------------------------------------------------------")
    @UnhandledError("This method doesn't throws an error when I change the date into the future")
    @Test(description = "Test for failure when placing an order with invalid data", groups = { GroupsEndpoints.NEGATIVE,
	    GroupsEndpoints.REGRESSION })
    public void testPlaceOrderFailure() {
	String order = "{\"id\":\"invalid\",\"petId\":\"invalid\",\"quantity\":-1,\"shipDate\":\"2200-06-18T00:00:00.000Z\",\"status\":\"\",\"complete\":null}";
	Response response = postUrl(PLACE_ORDER, contentType, order);
	assertions.assertReturnCode(response, 405);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for successfully retrieving an order by ID", groups = { GroupsEndpoints.POSITIVE,
	    GroupsEndpoints.REGRESSION, GroupsEndpoints.SMOKE }, dependsOnMethods = { "testPlaceOrderSuccess" })
    public void testGetOrderByIdSuccess() {
	Response response = getUrl(FIND_PURCHASE_ORDER_BY_ID, SharedState.getStoreId());
	assertions.assertReturnCode(response, 200);
	int orderId = response.jsonPath().getInt("id");
	Assert.assertEquals(orderId, SharedState.getStoreId());
    }

    @Done("----------------------------------------------------------------------------------------")
    @UnhandledError("This method throws an error but that isn't defining a correct interval (1-10)")
    @Test(description = "Test for failure when retrieving an order by invalid ID", groups = { GroupsEndpoints.NEGATIVE,
	    GroupsEndpoints.REGRESSION })
    public void testGetOrderByIdFailure() {
	Response response = getUrl(FIND_PURCHASE_ORDER_BY_ID, "invalid");
	assertions.assertReturnCode(response, 404);
    }

    @UnderConstruction("See below - Error message what I get: 'java.lang.IllegalArgumentException: Schema to use cannot be null'")
    @Test(description = "Test for successfully retrieving the inventory", groups = { GroupsEndpoints.POSITIVE,
	    GroupsEndpoints.REGRESSION, GroupsEndpoints.SMOKE })
    public void testGetInventorySuccess() {
	Response response = getUrl(GET_INVENTORY);
	assertions.assertReturnCode(response, 200);
	// response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("src/test/resources/responses/Inventory.json"));
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for successfully deleting an order by ID", groups = { GroupsEndpoints.POSITIVE,
	    GroupsEndpoints.REGRESSION, GroupsEndpoints.SMOKE }, dependsOnMethods = { "testPlaceOrderSuccess" })
    public void testDeleteOrderByIdSuccess() {
	Response response = deleteUrl(DELETE_PURCHASE_ORDER_BY_ID, SharedState.getStoreId());
	assertions.assertReturnCode(response, 200);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for failure when deleting an order by invalid ID", groups = { GroupsEndpoints.NEGATIVE,
	    GroupsEndpoints.REGRESSION })
    public void testDeleteOrderByIdFailure() {
	String badOrderId = "1111111111111111111111";
	Response response = deleteUrl(DELETE_PURCHASE_ORDER_BY_ID, badOrderId);
	assertions.assertReturnCode(response, 405);
    }
}
