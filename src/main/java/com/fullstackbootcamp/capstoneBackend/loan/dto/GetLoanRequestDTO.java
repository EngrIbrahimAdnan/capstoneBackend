package com.fullstackbootcamp.capstoneBackend.loan.dto;

import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanRequestEntity;
import com.fullstackbootcamp.capstoneBackend.loan.enums.LoanRequestRetrievalStatus;

public class GetLoanRequestDTO {
    private LoanRequestRetrievalStatus status; // SUCCESS, FAIL
    private String message;         // Description of the result (e.g., "Successfully retrieved loan request entity" or error details)
    private LoanRequestEntity entity;

    public LoanRequestRetrievalStatus getStatus() {
        return status;
    }

    public void setStatus(LoanRequestRetrievalStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoanRequestEntity getEntity() {
        return entity;
    }

    public void setEntity(LoanRequestEntity entity) {
        this.entity = entity;
    }
}
