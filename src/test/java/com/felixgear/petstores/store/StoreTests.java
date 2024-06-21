package com.felixgear.petstores.store;

import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.felixgear.petstores.Base;
import com.felixgear.petstores.GroupsEndpoints;
import com.felixgear.petstores.utils.Done;
import com.felixgear.petstores.utils.UnderConstruction;
import com.felixgear.petstores.utils.UnhandledError;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class StoreTests extends Base {
    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for successfully placing an order", groups = { GroupsEndpoints.POSITIVE, GroupsEndpoints.REGRESSION, GroupsEndpoints.SMOKE })
    public void testPlaceOrderSuccess() {
	order.setId(100666);
	order.setPetId(10);
	order.setQuantity(26);
	String shipDate = "2024-06-20T22:33:05.204Z";
	order.setShipDate(shipDate);
	order.setStatus("placed");
	order.setComplete(false);
	Response response = postUrl(PLACE_ORDER, order);
	assertions.assertReturnCode(response, 200);
	response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("responses/Order.json"));
    }

    @Done("----------------------------------------------------------------------------------------")
    @UnhandledError("This method doesn't throws an error when I change the date into the future")
    @Test(description = "Test for failure when placing an order with invalid data", groups = { GroupsEndpoints.NEGATIVE,
	    GroupsEndpoints.REGRESSION }, dependsOnMethods = "testPlaceOrderSuccess")
    public void testPlaceOrderFailure() throws JsonProcessingException {
	order.setId(100666);
	order.setPetId(10);
	String shipDate = faker.date().toString();
	order.setShipDate(shipDate);
	order.setStatus("available");
	order.setComplete(false);
	Response response = postUrl(PLACE_ORDER, order);
	assertions.assertReturnCode(response, 500);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for successfully retrieving an order by ID", groups = { GroupsEndpoints.POSITIVE,
	    GroupsEndpoints.REGRESSION, GroupsEndpoints.SMOKE }, dependsOnMethods = { "testPlaceOrderSuccess" })
    public void testGetOrderByIdSuccess() {
	Response response = getUrl(FIND_PURCHASE_ORDER_BY_ID, order.getId());
	assertions.assertReturnCode(response, 200);
	response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("responses/Order.json"));
	order = response.as(Order.class);
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
	response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("responses/Inventory.json"));
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for successfully deleting an order by ID", groups = { GroupsEndpoints.POSITIVE,
	    GroupsEndpoints.REGRESSION, GroupsEndpoints.SMOKE }, dependsOnMethods = { "testGetOrderByIdSuccess" })
    public void testDeleteOrderByIdSuccess() {
	Response response = deleteUrl(DELETE_PURCHASE_ORDER_BY_ID, order.getId());
	assertions.assertReturnCode(response, 200);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for failure when deleting an order by invalid ID", groups = { GroupsEndpoints.NEGATIVE,
	    GroupsEndpoints.REGRESSION })
    public void testDeleteOrderByIdFailure() {
	String badOrderId = "1111111111111111111111";
	Response response = deleteUrl(DELETE_PURCHASE_ORDER_BY_ID, badOrderId);
	assertions.assertReturnCode(response, 404);
    }
}
