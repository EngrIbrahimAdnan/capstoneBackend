package com.fullstackbootcamp.capstoneBackend.business.dto;

import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessAdditionStatus;

public class AddBusinessDTO {
    private BusinessAdditionStatus status; // SUCCESS, FAIL, BUSINESS_ALREADY_EXISTS
    private String message;         // Description of the result (e.g., "User created successfully" or error details)

    public BusinessAdditionStatus getStatus() {
        return status;
    }

    public void setStatus(BusinessAdditionStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
