package com.fullstackbootcamp.capstoneBackend.business.entity;

import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessState;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "businesses")
public class BusinessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "business_owner_user_id", nullable = false)
    private UserEntity businessOwnerUser;

    @Column(name = "business_name", nullable = false, length = 100)
    private String businessName;

    @Column(name = "business_license", nullable = false, length = 50)
    private String businessLicense;

    // TODO: Change nullable to true once logic for the business state is implemented.
    //  Same for financial statement and score
    @Column(name = "business_state", nullable = true, length = 50)
    private BusinessState businessState;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "financial_statement_id", nullable = true)
    private FinancialStatementEntity financialStatement;

    @Column(name = "financial_score", nullable = true)
    private Double financialScore;

    public BusinessEntity() {
    }

    public BusinessEntity(UserEntity businessOwnerUser, String businessName, String businessLicense) {
        this.businessOwnerUser = businessOwnerUser;
        this.businessName = businessName;
        this.businessLicense = businessLicense;
    }

    public Long getId() {
        return id;
    }

    public UserEntity getBusinessOwnerUser() {
        return businessOwnerUser;
    }

    public void setBusinessOwnerUser(UserEntity businessOwnerUser) {
        this.businessOwnerUser = businessOwnerUser;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public BusinessState getBusinessState() {
        return businessState;
    }

    public void setBusinessState(BusinessState businessState) {
        this.businessState = businessState;
    }

    public FinancialStatementEntity getFinancialStatement() {
        return financialStatement;
    }

    public void setFinancialStatement(FinancialStatementEntity financialStatement) {
        this.financialStatement = financialStatement;
    }

    public Double getFinancialScore() {
        return financialScore;
    }

    public void setFinancialScore(Double financialScore) {
        this.financialScore = financialScore;
    }
}
