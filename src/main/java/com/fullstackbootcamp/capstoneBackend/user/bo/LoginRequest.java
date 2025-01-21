package com.fullstackbootcamp.capstoneBackend.user.bo;

import jakarta.validation.constraints.NotNull;

public class LoginRequest {
    @NotNull(message = "The 'Civil ID' field is required and it's missing")
    private String civilId;

    @NotNull(message = "The 'password' field is required and it's missing")
    private String password;

    public String getCivilId() {
        return civilId;
    }

    public void setCivilId(String civilId) {
        this.civilId = civilId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
