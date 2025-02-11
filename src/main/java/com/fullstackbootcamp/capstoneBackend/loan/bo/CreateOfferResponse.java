package com.fullstackbootcamp.capstoneBackend.loan.bo;

import org.springframework.web.bind.annotation.PathVariable;

public class CreateOfferResponse {
    private Long loanRequestId;
    private Long loanResponseId;

    public Long getLoanRequestId() {
        return loanRequestId;
    }

    public void setLoanRequestId(Long loanRequestId) {
        this.loanRequestId = loanRequestId;
    }

    public Long getLoanResponseId() {
        return loanResponseId;
    }

    public void setLoanResponseId(Long loanResponseId) {
        this.loanResponseId = loanResponseId;
    }
}
