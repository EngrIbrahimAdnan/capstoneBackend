package com.fullstackbootcamp.capstoneBackend.loan.dto;

import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanRequestEntity;
import com.fullstackbootcamp.capstoneBackend.loan.enums.LoanRequestRetrievalStatus;

import java.util.List;

public class GetAllLoanRequestsDTO {
    private LoanRequestRetrievalStatus status; // SUCCESS, FAIL
    private String message;         // Description of the result (e.g., "Successfully retrieved loan request entity" or error details)
    private List<LoanRequestEntity> allRequests;

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

    public List<LoanRequestEntity> getAllRequests() {
        return allRequests;
    }

    public void setAllRequests(List<LoanRequestEntity> allRequests) {
        this.allRequests = allRequests;
    }
}
