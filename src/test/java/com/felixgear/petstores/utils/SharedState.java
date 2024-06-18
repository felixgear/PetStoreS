package com.felixgear.petstores.utils;

public class SharedState {
    private static int petId;
    private static int storeId;

    public static int getPetId() {
        return petId;
    }

    public static void setPetId(int petId) {
        SharedState.petId = petId;
    }

    public static int getStoreId() {
	return storeId;
    }

    public static void setStoreId(int storeId) {
	SharedState.storeId = storeId;
    }
}