package com.felixgear.petstores;

public class GroupsEndpoints {
    // End points ---------------------------------------------------------
    // Pet Endpoints
    protected static final String UPLOAD_IMAGE = "/pet/{petId}/uploadImage";
    protected static final String ADD_NEW_PET = "/pet";
    protected static final String UPDATE_PET = "/pet";
    protected static final String FIND_PETS_BY_STATUS = "/pet/findByStatus";
    @Deprecated
    protected static final String FIND_PETS_BY_TAGS = "/pet/findByTags";
    protected static final String FIND_PET_BY_ID = "/pet/{petId}";
    protected static final String UPDATE_PET_WITH_FORM = "/pet/{petId}";
    protected static final String DELETE_PET = "/pet/{petId}";

    // Store Endpoints
    protected static final String GET_INVENTORY = "/store/inventory";
    protected static final String PLACE_ORDER = "/store/order";
    protected static final String FIND_PURCHASE_ORDER_BY_ID = "/store/order/{orderId}";
    protected static final String DELETE_PURCHASE_ORDER_BY_ID = "/store/order/{orderId}";

    // User Endpoints
    protected static final String CREATE_USER_WITH_LIST = "/user/createWithList";
    protected static final String GET_USER_BY_USERNAME = "/user/{username}";
    protected static final String UPDATE_USER = "/user/{username}";
    protected static final String DELETE_USER = "/user/{username}";
    protected static final String LOGIN_USER = "/user/login";
    protected static final String LOGOUT_USER = "/user/logout";
    protected static final String CREATE_USER_WITH_ARRAY = "/user/createWithArray";
    protected static final String CREATE_USER = "/user";
    
    // Test groups --------------------------------------------------------
    public static final String POSITIVE = "positive";
    public static final String NEGATIVE = "negative";
    public static final String BEFORE_DELETE = "before_delete";
    public static final String REGRESSION = "regression";
    public static final String SMOKE = "smoke";
}