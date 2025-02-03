package com.fullstackbootcamp.capstoneBackend.loan.bo;

import com.fullstackbootcamp.capstoneBackend.user.enums.Bank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreateLoanRequest {
    @NotNull(message = "The 'bank' field is required and it's missing")
    private Bank bank;

    @NotNull(message = "The 'amount' field is required and it's missing")
    private BigDecimal amount;

    @NotNull(message = "The 'loanTerm' field is required and it's missing")
    private String loanTerm;

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(String loanTerm) {
        this.loanTerm = loanTerm;
    }
}
