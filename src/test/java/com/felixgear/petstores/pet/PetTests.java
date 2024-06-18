package com.felixgear.petstores.pet;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.felixgear.petstores.Base;
import com.felixgear.petstores.GroupsEndpoints;
import com.felixgear.petstores.utils.Done;
import com.felixgear.petstores.utils.SharedState;
import com.github.javafaker.Faker;

import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class PetTests extends Base {
    private static final String contentType = "application/json; charset=UTF-8";
    private static Faker faker = new Faker();
    private String petName;
    private String[] petStatusList = { "available", "pending", "sold" };
    private String petStatus = petStatusList[0];

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for successfully uploading an image for a pet", groups = { GroupsEndpoints.POSITIVE,
	    GroupsEndpoints.REGRESSION, GroupsEndpoints.SMOKE }, dependsOnMethods = { "testAddNewPetSuccess" })
    public void testUploadImageSuccess() {
	File petImageFile = new File("pet.png");
	String additionalMetadata = "test metadata";
	Response response = postUrl(UPLOAD_IMAGE, SharedState.getPetId(), petImageFile, additionalMetadata);
	assertions.assertReturnCode(response, 500);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(enabled=false, description = "Test for failure when uploading an invalid image for a pet", groups = {
	    GroupsEndpoints.NEGATIVE, GroupsEndpoints.REGRESSION })
    public void testUploadImageFailure() {
	String filePath = "empty.txt";
	File petImageFile = new File(filePath);
	String additionalMetadata = "test metadata";
	Response response = postUrl(UPLOAD_IMAGE, WRONG_ID, petImageFile, additionalMetadata);
	assertions.assertReturnCode(response, 404);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for successfully adding a new pet", groups = { GroupsEndpoints.POSITIVE,
	    GroupsEndpoints.BEFORE_DELETE, GroupsEndpoints.REGRESSION, GroupsEndpoints.SMOKE })
    public void testAddNewPetSuccess() {
	petName = "Marshall";
	int randStatus = faker.number().numberBetween(0, 2);
	petStatus = petStatusList[randStatus];
	Pet pet = RestAssured.get("resources/forms/Pet.json").as(Pet.class);
	pet.setName(petName);
	pet.setStatus(petStatus);
	Response response = postUrl(PLACE_ORDER, contentType, pet);
	assertions.assertReturnCode(response, 200);
	Integer petId = response.jsonPath().getInt("id");
	assertions.assertElementIdScheme(petId);
	SharedState.setPetId(petId);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for failure when adding a new pet with invalid data", groups = { GroupsEndpoints.NEGATIVE,
	    GroupsEndpoints.REGRESSION })
    public void testAddNewPetFailure() {
	String body = "{\"Pet\":{\"id\":0,\"category\":{\"id\":0,\"name\":\"string\"},\"name\":\"doggie\",\"photoUrls\":[\"string\"],\"tags\":[{\"id\":0,\"name\":\"string\"}],\"status\":\"available\"}}";
	Response response = postUrl(ADD_NEW_PET, contentType, body);
	assertions.assertReturnCode(response, 405);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for successfully updating a pet", groups = { GroupsEndpoints.POSITIVE,
	    GroupsEndpoints.BEFORE_DELETE, GroupsEndpoints.REGRESSION,
	    GroupsEndpoints.SMOKE }, dependsOnMethods = { "testAddNewPetSuccess" })
    public void testUpdatePetSuccess() {
	petName = "Klemi";
	int randStatus = faker.number().numberBetween(0, 2);
	petStatus = petStatusList[randStatus];
	Pet pet = RestAssured.get("resources/forms/Pet.json").as(Pet.class);
	pet.setName(petName);
	pet.setStatus(petStatus);
	Response response = putUrl(UPDATE_PET, contentType, pet);
	assertions.assertReturnCode(response, 200);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for failure when updating a pet with invalid data", groups = { GroupsEndpoints.NEGATIVE,
	    GroupsEndpoints.REGRESSION })
    public void testUpdatePetFailure() {
	String body = String.format("<Pet><id>%d</id><name>doggie</name><status>available</status></Pet>", WRONG_ID);
	Response response = putUrl(UPDATE_PET, contentType, body);
	assertions.assertReturnCode(response, 405);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for successfully finding pets by status", groups = { GroupsEndpoints.POSITIVE,
	    GroupsEndpoints.BEFORE_DELETE, GroupsEndpoints.REGRESSION,
	    GroupsEndpoints.SMOKE }, dependsOnMethods = { "testAddNewPetSuccess" })
    public void testFindPetsByStatusSuccess() {
	Map<String, String> statuses = new HashMap<>();
	String status = Integer.toString(faker.number().numberBetween(0, 2));
	statuses.put("status", status);
	Response response = getUrl(FIND_PETS_BY_STATUS, statuses);
	assertions.assertReturnCode(response, 200);
	String responsePetId = response.jsonPath().getString("id");
	String responsePetName = response.jsonPath().getString("name");
	String responsePetStatus = response.jsonPath().getString("status");
	String formData[] = { responsePetId, responsePetName, responsePetStatus };
	String expectedData[] = { Integer.toString(SharedState.getPetId()), petName, petStatus };
	assertions.assertFormData(formData, expectedData);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for failure when finding pets by invalid status", groups = { GroupsEndpoints.NEGATIVE,
	    GroupsEndpoints.REGRESSION })
    public void testFindPetsByStatusFailure() {
	Map<String, String> statuses = new HashMap<>();
	String status = Integer.toString(WRONG_ID);
	statuses.put("status", status);
	Response response = getUrl(FIND_PETS_BY_STATUS, statuses);
	assertions.assertReturnCode(response, 200);
    }

    /**
     * @deprecated As of version 2.0, replaced by
     *             {@link #testFindPetsByStatusSuccess()}
     */
    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for successfully finding pets by tags", groups = { GroupsEndpoints.POSITIVE,
	    GroupsEndpoints.BEFORE_DELETE, GroupsEndpoints.REGRESSION,
	    GroupsEndpoints.SMOKE }, dependsOnMethods = { "testAddNewPetSuccess" })
    public void testFindPetsByTagsSuccess() {
	String[] tagsInRowWithCommaSeparated = { "tag1", "tag2", "tag3" };
	Response response = getUrl(FIND_PETS_BY_TAGS, tagsInRowWithCommaSeparated);
	assertions.assertReturnCode(response, 200);
	response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema("responses/Pets.json"));
    }

    /**
     * @deprecated As of version 2.0, replaced by
     *             {@link #testFindPetsByStatusFailure()}
     */
    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for failure when updating a pet with invalid form data", groups = {
	    GroupsEndpoints.NEGATIVE, GroupsEndpoints.REGRESSION }, dependsOnMethods = { "testAddNewPetSuccess" })
    public void testFindPetsByTagsFailure() {
	String[] tagsInRowWithCommaSeparated = { faker.lorem().sentence(100000, 100000) };
	Response response = getUrl(FIND_PETS_BY_TAGS, tagsInRowWithCommaSeparated);
	assertions.assertReturnCode(response, 414);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for successfully finding a pet by ID", groups = { GroupsEndpoints.POSITIVE,
	    GroupsEndpoints.BEFORE_DELETE, GroupsEndpoints.REGRESSION,
	    GroupsEndpoints.SMOKE }, dependsOnMethods = { "testUpdatePetSuccess" })
    public void testFindPetByIdAfterUpdateSuccess() {
	Response response = getUrl(FIND_PET_BY_ID, SharedState.getPetId());
	assertions.assertReturnCode(response, 200);
	String responsePetId = response.jsonPath().getString("id");
	String responsePetName = response.jsonPath().getString("name");
	String responsePetStatus = response.jsonPath().getString("status");
	String formData[] = { responsePetId, responsePetName, responsePetStatus };
	String expectedData[] = { Integer.toString(SharedState.getPetId()), petName, petStatus };
	assertions.assertFormData(formData, expectedData);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for successfully finding a pet by ID", groups = { GroupsEndpoints.POSITIVE,
	    GroupsEndpoints.REGRESSION, GroupsEndpoints.SMOKE }, dependsOnMethods = { "testDeletePetSuccess" })
    public void testFindPetByIdAfterDeleteSuccess() {
	Response response = getUrl(FIND_PET_BY_ID, SharedState.getPetId());
	assertions.assertReturnCode(response, 200);
	String responsePetId = response.jsonPath().getString("id");
	String responsePetName = response.jsonPath().getString("name");
	String responsePetStatus = response.jsonPath().getString("status");
	String formData[] = { responsePetId, responsePetName, responsePetStatus };
	String expectedData[] = { Integer.toString(SharedState.getPetId()), petName, petStatus };
	assertions.assertFormData(formData, expectedData);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for failure when finding a pet by invalid ID", groups = { GroupsEndpoints.NEGATIVE,
	    GroupsEndpoints.REGRESSION }, dependsOnMethods = { "testAddNewPetSuccess" })
    public void testFindPetByIdFailure() {
	Response response = getUrl(FIND_PET_BY_ID, WRONG_ID);
	assertions.assertReturnCode(response, 404);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for successfully updating a pet with form data", groups = { GroupsEndpoints.POSITIVE,
	    GroupsEndpoints.BEFORE_DELETE, GroupsEndpoints.REGRESSION,
	    GroupsEndpoints.SMOKE }, dependsOnMethods = { "testAddNewPetSuccess" })
    public void testUpdatePetWithFormSuccess() {
	Map<String, String> formParams = new HashMap<>();
	String petId = Integer.toString(faker.number().numberBetween(0, 2));
	formParams.put("name", faker.animal().name());
	Integer randStatus = faker.number().numberBetween(0, 2);
	formParams.put("status", petStatusList[randStatus]);
	Response response = postUrl(UPDATE_PET_WITH_FORM, contentType, petId, formParams);
	assertions.assertReturnCode(response, 200);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for failure when updating a pet with invalid form data", groups = {
	    GroupsEndpoints.NEGATIVE, GroupsEndpoints.BEFORE_DELETE,
	    GroupsEndpoints.REGRESSION }, dependsOnMethods = { "testAddNewPetSuccess" })
    public void testUpdatePetWithFormFailure() {
	Map<String, String> formParams = new HashMap<>();
	/*
	 * String petId = Integer.toString(faker.number().numberBetween(0, 2)); Another
	 * wrong input is: String petName = faker.lorem().sentence(1000000);
	 */
	String petName = faker.animal().name();
	formParams.put("name", petName);
	Integer randStatus = faker.number().numberBetween(0, 2);
	formParams.put("status", petStatusList[randStatus]);
	Response response = postUrl(UPDATE_PET_WITH_FORM, contentType, Integer.toString(WRONG_ID), formParams);
	assertions.assertReturnCodeDifferentThanThis(response, 200);
	response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchema("responses/ErrorResponse.xml"));
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for successfully deleting a pet", groups = { GroupsEndpoints.POSITIVE,
	    GroupsEndpoints.REGRESSION, GroupsEndpoints.SMOKE }, dependsOnGroups = { GroupsEndpoints.BEFORE_DELETE })
    public void testDeletePetSuccess() {
	Map<String, String> headers = new HashMap<>();
	String apiKey = "sepcial-key";
	headers.put("status", apiKey);
	Response response = deleteUrl(DELETE_PET, SharedState.getPetId(), headers);
	assertions.assertReturnCode(response, 200);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for failure when deleting a pet with invalid ID", groups = { GroupsEndpoints.NEGATIVE,
	    GroupsEndpoints.REGRESSION }, dependsOnMethods = { "testAddNewPetSuccess" })
    public void testDeletePetFailure() {
	Map<String, String> headers = new HashMap<>();
	String apiKey = "sepcial-key";
	headers.put("status", apiKey);
	Response response = deleteUrl(DELETE_PET, -467, headers);
	assertions.assertReturnCode(response, 404);
    }
}
