package com.fullstackbootcamp.capstoneBackend.loan.dto;

import com.fullstackbootcamp.capstoneBackend.loan.enums.OfferSubmissionStatus;

public class OfferResponseDTO {
    private OfferSubmissionStatus status;
    private String message;

    public OfferSubmissionStatus getStatus() {
        return status;
    }

    public void setStatus(OfferSubmissionStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
