package com.fullstackbootcamp.capstoneBackend.loan.dto;


import com.fullstackbootcamp.capstoneBackend.loan.enums.CheckNotificationStatus;

public class CheckNotificationDTO {
    private CheckNotificationStatus status; // SUCCESS, FAIL
    private String message;         // Description of the result (e.g., "Successfully retrieved loan request entity" or error details)

    public CheckNotificationStatus getStatus() {
        return status;
    }

    public void setStatus(CheckNotificationStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
