package com.fullstackbootcamp.capstoneBackend.user.bo;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class CreateUserRequest {

    @NotNull(message = "The 'firstName' field is required and it's missing")
    private String firstName;

    @NotNull(message = "The 'lastName' field is required and it's missing")
    private String lastName;

    @NotNull(message = "The 'username' field is required and it's missing")
    private String username;

    @NotNull(message = "The 'password' field is required and it's missing")
    private String password;

    @NotNull(message = "The 'civilId' field is required and it's missing")
    @Pattern(regexp = "\\d{12}", message = "The 'civilId' field must contain exactly 12 digits")
    private String civilId;

    @NotNull(message = "The 'mobileNumber' field is required and it's missing")
    @Pattern(regexp = "\\d{8}", message = "The 'mobileNumber' field must contain exactly 8 digits")
    private String mobileNumber;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCivilId() {
        return civilId;
    }

    public void setCivilId(String civilId) {
        this.civilId = civilId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
