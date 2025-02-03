package com.fullstackbootcamp.capstoneBackend.loan.dto;

import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessAdditionStatus;
import com.fullstackbootcamp.capstoneBackend.loan.enums.CreateLoanRequestStatus;

public class LoanRequestDTO {
    private CreateLoanRequestStatus status; // SUCCESS, FAIL
    private String message;         // Description of the result (e.g., "User created successfully" or error details)

    public CreateLoanRequestStatus getStatus() {
        return status;
    }

    public void setStatus(CreateLoanRequestStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
