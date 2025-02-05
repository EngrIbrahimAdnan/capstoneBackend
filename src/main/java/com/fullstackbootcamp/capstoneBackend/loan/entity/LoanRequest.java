package com.fullstackbootcamp.capstoneBackend.loan.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fullstackbootcamp.capstoneBackend.business.entity.BusinessEntity;
import com.fullstackbootcamp.capstoneBackend.loan.enums.LoanRequestStatus;
import com.fullstackbootcamp.capstoneBackend.loan.enums.LoanTerm;
import com.fullstackbootcamp.capstoneBackend.loan.enums.RejectionSource;
import com.fullstackbootcamp.capstoneBackend.loan.enums.RepaymentPlan;
import com.fullstackbootcamp.capstoneBackend.notification.entity.NotificationEntity;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import com.fullstackbootcamp.capstoneBackend.user.enums.Bank;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "loan_request")
public class LoanRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* Note:
     *  - loanResponses is intentionally left nullable
     *  - This is to account for the case upon entity creation
     *  - Once the banker sends the first loan response, loan request is updated
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_responses")
    private List<LoanResponse> loanResponses;



    /* Note:
     *  - BusinessOwner User can be obtained from the business entity business
     *  - It would be redundant to include it here as well.
     */

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "business_id", nullable = false)
    private BusinessEntity business;

    /* Note:
     *  - selectedBanks allows only the listed banks to see the request offer
     */
    @NotNull(message = "The 'selectedBanks' field is required and it's missing")
    private List<Bank> selectedBanks;

    @Column(name = "request_analysis", nullable = false)
    private String requestAnalysis; // ai endpoint

    @Column(name = "loan_title", nullable = false)
    private String loanTitle ; // expects

    @Column(name = "loan_purpose", nullable = false)
    private String loanPurpose; // expects

    @NotNull(message = "The 'amount' field is required and it's missing")
    private BigDecimal amount;

    /* Note: standard loanTerms in banks:
     *  - SIX_MONTHS, ONE_YEAR, TWO_YEARS, FIVE_YEARS
     */
    @Column(name = "loan_term", nullable = false)
    private LoanTerm loanTerm; // expects

    @Column(name = "repayment_plan", nullable = false)
    private RepaymentPlan repaymentPlan; // expects

    @NotNull(message = "The 'status' field is required and it's missing")
    private LoanRequestStatus status;

    @Column(name = "rejection_source", nullable = false)
    private RejectionSource rejectionSource;

    private String reason;

    // Note: data of the last request status update
    @Column(name = "data_status", nullable = false)
    private LocalDateTime statusDate;

    // Note: keeping track on whether the request is viewed by each user
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_request_notifications")
    private List<NotificationEntity> loanRequestNotifications;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<LoanResponse> getLoanResponses() {
        return loanResponses;
    }

    public void setLoanResponses(List<LoanResponse> loanResponses) {
        this.loanResponses = loanResponses;
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

    public RepaymentPlan getRepaymentPlan() {
        return repaymentPlan;
    }

    public void setRepaymentPlan(RepaymentPlan repaymentPlan) {
        this.repaymentPlan = repaymentPlan;
    }

    public BusinessEntity getBusiness() {
        return business;
    }

    public void setBusiness(BusinessEntity business) {
        this.business = business;
    }


    public  List<Bank> getSelectedBanks() {
        return selectedBanks;
    }

    public void setSelectedBanks(List<Bank> selectedBanks) {
        this.selectedBanks = selectedBanks;
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

    public LoanTerm getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(LoanTerm loanTerm) {
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

    public LocalDateTime getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(LocalDateTime statusDate) {
        this.statusDate = statusDate;
    }

    public List<NotificationEntity> getLoanRequestNotifications() {
        return loanRequestNotifications;
    }

    public void setLoanRequestNotifications(List<NotificationEntity> loanRequestNotifications) {
        this.loanRequestNotifications = loanRequestNotifications;
    }
}
