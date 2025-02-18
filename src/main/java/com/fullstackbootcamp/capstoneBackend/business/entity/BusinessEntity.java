package com.fullstackbootcamp.capstoneBackend.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessState;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "businesses")
public class BusinessEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "business_Avatar_Image", nullable = false)
    private Long businessAvatarFileId;

    @OneToOne
    @JoinColumn(name = "business_owner_user_id", nullable = false)
    private UserEntity businessOwnerUser;

    @Column(name = "business_Nickname", nullable = false, length = 1000)
    private String businessNickname;

    @JoinColumn(name = "financial_statement_PDF", nullable = false)
    private Long financialStatementFileId;

    @JoinColumn(name = "business_License_Image", nullable = false)
    private Long businessLicenseImageFileId;

    /* Review:
        the following are related to the text extraction feature
        They are currently nullable. To adjust, Add "nullable = false"
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "financial_statement")
    private FinancialStatementEntity financialStatement;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "business_license")
    private BusinessLicenseEntity businessLicense;

    @Column(name = "financial_analysis", length = 1000)
    private String financialAnalysis;

    @Column(name = "business_state")
    private BusinessState businessState;

    @Column(name = "financial_score")
    private Double financialScore;


    public Long getBusinessAvatarFileId() {
        return businessAvatarFileId;
    }

    public void setBusinessAvatarFileId(Long businessAvatarFileId) {
        this.businessAvatarFileId = businessAvatarFileId;
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

    public String getBusinessNickname() {
        return businessNickname;
    }

    public void setBusinessNickname(String businessNickname) {
        this.businessNickname = businessNickname;
    }

    public Long getFinancialStatementFileId() {
        return financialStatementFileId;
    }

    public void setFinancialStatementFileId(Long financialStatementFileId) {
        this.financialStatementFileId = financialStatementFileId;
    }

    public Long getBusinessLicenseImageFileId() {
        return businessLicenseImageFileId;
    }

    public void setBusinessLicenseImageFileId(Long businessLicenseImageFileId) {
        this.businessLicenseImageFileId = businessLicenseImageFileId;
    }

    public FinancialStatementEntity getFinancialStatement() {
        return financialStatement;
    }

    public void setFinancialStatement(FinancialStatementEntity financialStatement) {
        this.financialStatement = financialStatement;
    }


    public BusinessLicenseEntity getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(BusinessLicenseEntity businessLicense) {
        this.businessLicense = businessLicense;
    }


    public String getFinancialAnalysis() {
        return financialAnalysis;
    }

    public void setFinancialAnalysis(String financialAnalysis) {
        this.financialAnalysis = financialAnalysis;
    }

    public BusinessState getBusinessState() {
        return businessState;
    }

    public void setBusinessState(BusinessState businessState) {
        this.businessState = businessState;
    }

    public Double getFinancialScore() {
        return financialScore;
    }

    public void setFinancialScore(Double financialScore) {
        this.financialScore = financialScore;
    }
}











