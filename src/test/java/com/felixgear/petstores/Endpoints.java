package com.example.sandbox;

public class Endpoints {
    static final String baseUrl = "https://petstore.swagger.io/v2";

    // Pet Endpoints
    public static final String UPLOAD_IMAGE = "/pet/{petId}/uploadImage";
    public static final String ADD_NEW_PET = "/pet";
    public static final String UPDATE_PET = "/pet";
    public static final String FIND_PETS_BY_STATUS = "/pet/findByStatus";
    public static final String FIND_PET_BY_ID = "/pet/{petId}";
    public static final String UPDATE_PET_WITH_FORM = "/pet/{petId}";
    public static final String DELETE_PET = "/pet/{petId}";

    // Store Endpoints
    public static final String GET_INVENTORY = "/store/inventory";
    public static final String PLACE_ORDER = "/store/order";
    public static final String FIND_PURCHASE_ORDER_BY_ID = "/store/order/{orderId}";
    public static final String DELETE_PURCHASE_ORDER_BY_ID = "/store/order/{orderId}";

    // User Endpoints
    public static final String CREATE_USER_WITH_LIST = "/user/createWithList";
    public static final String GET_USER_BY_USERNAME = "/user/{username}";
    public static final String UPDATE_USER = "/user/{username}";
    public static final String DELETE_USER = "/user/{username}";
    public static final String LOGIN_USER = "/user/login";
    public static final String LOGOUT_USER = "/user/logout";
    public static final String CREATE_USER_WITH_ARRAY = "/user/createWithArray";
    public static final String CREATE_USER = "/user";
}
