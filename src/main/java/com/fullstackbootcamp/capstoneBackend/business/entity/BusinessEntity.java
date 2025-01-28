package com.fullstackbootcamp.capstoneBackend.business.entity;

import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessState;
import com.fullstackbootcamp.capstoneBackend.file.entity.FileEntity;
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

    @Column(name = "business_Nickname", nullable = false, length = 100)
    private String businessNickname;

    @JoinColumn(name = "financial_statement_PDF", nullable = false)
    private Long financialStatementId;

    @JoinColumn(name = "business_License_PDF", nullable = false)
    private Long businessLicenseImageId;

    @Column(name = "business_state", length = 50)
    private BusinessState businessState;

//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "financial_statement_id")// add "nullable = false" once the text extraction is implemented
//    private FinancialStatementEntity financialStatement;

    @Column(name = "financial_score", nullable = false)
    private Double financialScore;


    public String getBusinessNickname() {
        return businessNickname;
    }

    public void setBusinessNickname(String businessNickname) {
        this.businessNickname = businessNickname;
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


    public BusinessState getBusinessState() {
        return businessState;
    }

    public void setBusinessState(BusinessState businessState) {
        this.businessState = businessState;
    }

//    public FinancialStatementEntity getFinancialStatement() {
//        return financialStatement;
//    }

//    public void setFinancialStatement(FinancialStatementEntity financialStatement) {
//        this.financialStatement = financialStatement;
//    }

    public Double getFinancialScore() {
        return financialScore;
    }

    public void setFinancialScore(Double financialScore) {
        this.financialScore = financialScore;
    }

    public Long getFinancialStatementId() {
        return financialStatementId;
    }

    public void setFinancialStatementId(Long financialStatementId) {
        this.financialStatementId = financialStatementId;
    }

    public Long getBusinessLicenseImageId() {
        return businessLicenseImageId;
    }

    public void setBusinessLicenseImageId(Long businessLicenseImageId) {
        this.businessLicenseImageId = businessLicenseImageId;
    }
}











