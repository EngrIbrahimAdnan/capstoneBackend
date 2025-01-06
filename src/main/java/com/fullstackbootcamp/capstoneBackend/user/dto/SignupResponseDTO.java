package com.fullstackbootcamp.capstoneBackend.user.dto;

import com.fullstackbootcamp.capstoneBackend.user.enums.CreateUserStatus;

public class SignupResponseDTO {
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
