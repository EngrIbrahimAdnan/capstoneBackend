package com.fullstackbootcamp.capstoneBackend.loan.bo;

import com.fullstackbootcamp.capstoneBackend.user.enums.Bank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreateLoanRequest {
    @NotNull(message = "The 'bank' field is required and it's missing")
    private Bank bank;

    @NotNull(message = "The 'loanTitle' field is required and it's missing")
    private String loanTitle;

    @NotNull(message = "The 'loanPurpose' field is required and it's missing")
    private String loanPurpose;

    @NotNull(message = "The 'amount' field is required and it's missing")
    private BigDecimal amount;

    @NotNull(message = "The 'loanTerm' field is required and it's missing")
    private String loanTerm;

    @NotNull(message = "The 'repaymentPlan' field is required and it's missing")
    private String repaymentPlan;

    public Bank getBank() {
        return bank;
    }

    public String getLoanTitle() {
        return loanTitle;
    }

    public void setLoanTitle(String loanTitle) {
        this.loanTitle = loanTitle;
    }

    public String getLoanPurpose() {
        return loanPurpose;
    }

    public void setLoanPurpose(String loanPurpose) {
        this.loanPurpose = loanPurpose;
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

    public String getRepaymentPlan() {
        return repaymentPlan;
    }

    public void setRepaymentPlan(String repaymentPlan) {
        this.repaymentPlan = repaymentPlan;
    }
}
