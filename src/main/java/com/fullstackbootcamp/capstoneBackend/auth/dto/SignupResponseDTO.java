package com.fullstackbootcamp.capstoneBackend.auth.dto;

import com.fullstackbootcamp.capstoneBackend.user.enums.CreateUserStatus;

public class SignupResponseDTO {
    private CreateUserStatus status;// SUCCESS, FAIL, USER_ALREADY_EXISTS
    private String message;         // Description of the result (e.g., "User created successfully" or error details)
    private String accessToken;
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public CreateUserStatus getStatus() {
        return status;
    }

    public void setStatus(CreateUserStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
