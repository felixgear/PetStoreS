package com.felixgear.petstores.pet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.felixgear.petstores.Base;
import com.felixgear.petstores.GroupsEndpoints;
import com.felixgear.petstores.utils.Done;
import com.felixgear.petstores.utils.UnderConstruction;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

public class PetTests extends Base {
    private String[] petStatusList = { "available", "pending", "sold" };

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for successfully uploading an image for a pet", groups = { GroupsEndpoints.POSITIVE,
	    GroupsEndpoints.REGRESSION, GroupsEndpoints.SMOKE }, dependsOnMethods = { "testAddNewPetSuccess" })
    public void testUploadImageSuccess() {
	File petImageFile = new File("src/test/resources/pet.png");
	String additionalMetadata = "test metadata";
	Response response = postUrl(UPLOAD_IMAGE, pet.getId(), petImageFile, additionalMetadata);
	assertions.assertReturnCode(response, 200);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(enabled = false, description = "Test for failure when uploading an invalid image for a pet", groups = {
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
    public void testAddNewPetSuccess() throws JsonGenerationException, JsonMappingException, IOException {
	pet.setId(faker.number().numberBetween(1000000, 2000000));
	String petName = "Marshall";
	pet.setName(petName);
	int randStatus = faker.number().numberBetween(0, 2);
	String petStatus = petStatusList[randStatus];
	pet.setStatus(petStatus);
	Response response = postUrl(PLACE_ORDER, pet);
	assertions.assertReturnCode(response, 200);
    }

    @Done("But I couldn't make an error -----------------------------------------------------------")
    @Test(enabled = false, description = "Test for failure when adding a new pet with invalid data", groups = {
	    GroupsEndpoints.NEGATIVE, GroupsEndpoints.REGRESSION }, dependsOnMethods = { "testAddNewPetSuccess" })
    public void testAddNewPetFailure() throws JsonProcessingException {
	ObjectMapper mapper = new ObjectMapper();
	String jsonString = mapper.writeValueAsString(pet);
	String replaceable = String.format("\"id\":%d", pet.getId());
	jsonString = jsonString.replace(replaceable, "\"id\":\"invalid_id\"");
	Pet invalidPet = mapper.readValue(jsonString, Pet.class);
	Response response = postUrl(ADD_NEW_PET, invalidPet);
	assertions.assertReturnCode(response, 405);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for successfully updating a pet", groups = { GroupsEndpoints.POSITIVE,
	    GroupsEndpoints.BEFORE_DELETE, GroupsEndpoints.REGRESSION,
	    GroupsEndpoints.SMOKE }, dependsOnMethods = { "testAddNewPetSuccess" })
    public void testUpdatePetSuccess() {
	pet.setName("Klemi");
	int randStatus = faker.number().numberBetween(0, 2);
	pet.setStatus(petStatusList[randStatus]);
	Response response = putUrl(UPDATE_PET, pet);
	assertions.assertReturnCode(response, 200);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for failure when updating a pet with invalid data", groups = { GroupsEndpoints.NEGATIVE,
	    GroupsEndpoints.REGRESSION })
    public void testUpdatePetFailure() {
	int randStatus = faker.number().numberBetween(0, 2);
	pet.setName("Klemi");
	pet.setStatus(petStatusList[randStatus]);
	Response response = putUrl(UPDATE_PET, pet);
	assertions.assertReturnCode(response, 200);
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
	response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("responses/Pets.json"));
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
	Map<String, String> tags = new HashMap<>();
	String tagsInRowWithCommaSeparated = "tag1,tag2,tag3";
	tags.put("tag1", tagsInRowWithCommaSeparated);
	Response response = getUrl(FIND_PETS_BY_TAGS, tags);
	assertions.assertReturnCode(response, 200);
	response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("responses/Pets.json"));
    }

    /**
     * @deprecated As of version 2.0, replaced by
     *             {@link #testFindPetsByStatusFailure()}
     */
    @Done("----------------------------------------------------------------------------------------")
    @Test(enabled = false, description = "Test for failure when updating a pet with invalid form data", groups = {
	    GroupsEndpoints.NEGATIVE, GroupsEndpoints.REGRESSION }, dependsOnMethods = { "testAddNewPetSuccess" })
    public void testFindPetsByTagsFailure() {
	String tagsInRowWithCommaSeparated = faker.lorem().sentence(10000, 10000);
	Response response = getUrl(FIND_PETS_BY_TAGS, tagsInRowWithCommaSeparated);
	assertions.assertReturnCode(response, 414);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for successfully finding a pet by ID", groups = { GroupsEndpoints.POSITIVE,
	    GroupsEndpoints.BEFORE_DELETE, GroupsEndpoints.REGRESSION,
	    GroupsEndpoints.SMOKE }, dependsOnMethods = { "testUpdatePetSuccess" })
    public void testFindPetByIdAfterUpdateSuccess() {
	Response response = getUrl(FIND_PET_BY_ID, pet.getId());
	assertions.assertReturnCode(response, 200);
	String responsePetId = response.jsonPath().getString("id");
	String responsePetName = response.jsonPath().getString("name");
	String responsePetStatus = response.jsonPath().getString("status");
	String formData[] = { responsePetId, responsePetName, responsePetStatus };
	String expectedData[] = { Long.toString(pet.getId()), pet.getName(), pet.getStatus() };
	assertions.assertFormData(formData, expectedData);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for successfully finding a pet by ID", groups = { GroupsEndpoints.POSITIVE,
	    GroupsEndpoints.REGRESSION, GroupsEndpoints.SMOKE }, dependsOnMethods = { "testDeletePetSuccess" })
    public void testFindPetByIdAfterDeleteSuccess() {
	Response response = getUrl(FIND_PET_BY_ID, pet.getId());
	assertions.assertReturnCode(response, 404);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for failure when finding a pet by invalid ID", groups = { GroupsEndpoints.NEGATIVE,
	    GroupsEndpoints.REGRESSION }, dependsOnMethods = { "testAddNewPetSuccess" })
    public void testFindPetByIdFailure() {
	Response response = getUrl(FIND_PET_BY_ID, WRONG_ID);
	assertions.assertReturnCode(response, 404);
    }

    @UnderConstruction("")
    @Test(enabled=false, description = "Test for successfully updating a pet with form data", groups = { GroupsEndpoints.POSITIVE,
	    GroupsEndpoints.BEFORE_DELETE, GroupsEndpoints.REGRESSION,
	    GroupsEndpoints.SMOKE }, dependsOnMethods = { "testUpdatePetSuccess" })
    public void testUpdatePetWithFormSuccess() {
	pet.setName(faker.animal().name());
	pet.setStatus(petStatusList[faker.number().numberBetween(0, 2)]);
	Response response = postUrl(UPDATE_PET_WITH_FORM, pet.getId(), pet.getName(), pet.getStatus());
	assertions.assertReturnCode(response, 200);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for failure when updating a pet with invalid form data", groups = {
	    GroupsEndpoints.NEGATIVE, GroupsEndpoints.BEFORE_DELETE,
	    GroupsEndpoints.REGRESSION })
    public void testUpdatePetWithFormFailure() {
	pet.setName(faker.animal().name());
	pet.setStatus(petStatusList[faker.number().numberBetween(0, 2)]);
	Response response = postUrl(UPDATE_PET_WITH_FORM, WRONG_ID, pet.getName(), pet.getStatus());
	assertions.assertReturnCode(response, 500);
    }

    @Done("----------------------------------------------------------------------------------------")
    @Test(description = "Test for successfully deleting a pet", groups = { GroupsEndpoints.POSITIVE,
	    GroupsEndpoints.REGRESSION, GroupsEndpoints.SMOKE }, dependsOnGroups = { GroupsEndpoints.BEFORE_DELETE })
    public void testDeletePetSuccess() {
	Map<String, String> headers = new HashMap<>();
	String apiKey = "sepcial-key";
	headers.put("status", apiKey);
	Response response = deleteUrl(DELETE_PET, pet.getId(), headers);
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
