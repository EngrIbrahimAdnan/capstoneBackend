package com.fullstackbootcamp.capstoneBackend.auth.dto;

import com.fullstackbootcamp.capstoneBackend.user.enums.CreateUserStatus;

public class LoadUsersResponseDTO {
    private CreateUserStatus status;// SUCCESS, FAIL, USER_ALREADY_EXISTS
    private String message;         // Description of the result (e.g., "User created successfully" or error details)

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

