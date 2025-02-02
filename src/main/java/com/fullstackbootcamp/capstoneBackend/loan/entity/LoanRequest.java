package com.fullstackbootcamp.capstoneBackend.loan.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fullstackbootcamp.capstoneBackend.business.entity.BusinessEntity;
import com.fullstackbootcamp.capstoneBackend.loan.enums.LoanRequestStatus;
import com.fullstackbootcamp.capstoneBackend.loan.enums.RejectionSource;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import com.fullstackbootcamp.capstoneBackend.user.enums.Bank;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "loan_request")
public class LoanRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* Note:
     *  - banker is intentionally left nullable
     *  - This is to account for the case before it is assigned to a banker user
     *  - Once the banker assigns it to himself, UserEntity is assigned to this loanRequest
     */

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "banker_user_id")
    @JsonIgnore
    private UserEntity banker;

    /* Note:
     *  - BusinessOwner User can be obtained from the business entity business
     *  - It would be redundant to include it here as well.
     */

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    @JsonIgnore
    private BusinessEntity business;

    /* Note:
     *  - bank field can be seen as the gateway that forwards the request to the specified bank
     *  - This field would be retrieved on the banker side, before banker user is assigned.
     */
    @NotNull
    private Bank bank;

    @Column(name = "request_analysis", nullable = false)
    private String requestAnalysis; // ai endpoint

    @NotNull
    private BigDecimal amount;

    /* Note: standard loanTerms in banks:
     *  - SIX_MONTHS, ONE_YEAR, TWO_YEARS, FIVE_YEARS
     */
    @Column(name = "loan_term", nullable = false)
    private String loanTerm; // expects

    @NotNull
    private LoanRequestStatus status;

    @Column(name = "rejection_source", nullable = false)
    private RejectionSource rejectionSource;

    private String reason;

    // Note: data of the last request status update
    @Column(name = "data_status", nullable = false)
    private LocalDate statusDate;

    // Note: keeping track on whether the request is viewed
    // Note: changes to False each status update to alert user
    @NotNull
    private Boolean viewed;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getBanker() {
        return banker;
    }

    public void setBanker(UserEntity banker) {
        this.banker = banker;
    }

    public BusinessEntity getBusiness() {
        return business;
    }

    public void setBusiness(BusinessEntity business) {
        this.business = business;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public String getRequestAnalysis() {
        return requestAnalysis;
    }

    public void setRequestAnalysis(String requestAnalysis) {
        this.requestAnalysis = requestAnalysis;
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

    public LoanRequestStatus getStatus() {
        return status;
    }

    public void setStatus(LoanRequestStatus status) {
        this.status = status;
    }

    public RejectionSource getRejectionSource() {
        return rejectionSource;
    }

    public void setRejectionSource(RejectionSource rejectionSource) {
        this.rejectionSource = rejectionSource;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDate getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(LocalDate statusDate) {
        this.statusDate = statusDate;
    }

    public Boolean getViewed() {
        return viewed;
    }

    public void setViewed(Boolean viewed) {
        this.viewed = viewed;
    }
}
