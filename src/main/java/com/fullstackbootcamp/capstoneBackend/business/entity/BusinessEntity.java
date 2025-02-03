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

    @OneToOne
    @JoinColumn(name = "business_owner_user_id", nullable = false)
    private UserEntity businessOwnerUser;

    @Column(name = "business_Nickname", nullable = false, length = 100)
    private String businessNickname;

    @JoinColumn(name = "financial_statement_PDF", nullable = false)
    private Long financialStatementFileId;

    @JoinColumn(name = "business_License_Image", nullable = false)
    private Long businessLicenseImageFileId;

    /* Review:
        the following are related to the text extraction feature
        They are currently nullable. To adjust, Add "nullable = false"
     */

    /*
     * NOTE: @JsonIgnore for financialStatement & businessLicense
     *  - Prevents serialization issues caused by Hibernate's lazy loading proxy.
     *  - Ensures that Jackson does not attempt to serialize an uninitialized entity.
     *  - Without this annotation, you may encounter the following error:
     *   {@code [simple type, class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor]}
     */
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "financial_statement")
    @JsonIgnore
    private FinancialStatementEntity financialStatement;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "business_license")
    @JsonIgnore
    private BusinessLicenseEntity businessLicense;

    @Column(name = "financial_analysis")
    private String financialAnalysis;

    @Column(name = "business_state")
    private BusinessState businessState;

    @Column(name = "financial_score")
    private Double financialScore;

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











