package com.felixgear.petstores.person;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "userName", "firstName", "lastName", "phone", "password", "email", "userStatus" })

public class Person {
    @JsonProperty("id")
    private int id;

    @JsonProperty("userName")
    private String userName;
    
    @JsonProperty("firstName")
    private String firstName;
    
    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("phone")
    private String phone;
    
    @JsonProperty("password")
    private String password;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("userStatus")
    private int userStatus;

    // Getters and Setters
    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public String getUserName() {
	return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getUserStatus() {
	return userStatus;
    }

    public void setUserStatus(int userStatus) {
	this.userStatus = userStatus;
    }
}
