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


    @JoinColumn(name = "business_License_Image", nullable = false)
    private Long businessLicenseImageId;

    /* Review:
        the following are related to the text extraction feature
        They are currently nullable. To adjust, Add "nullable = false"
     */

    /* TODO:
        Uncomment financialStatement
     */
//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "financial_statement_id")
//    private FinancialStatementEntity financialStatement;

    // TODO: Add business license entity relationship here once its created
    @Column(name = "business_state", length = 50)
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

    //    public FinancialStatementEntity getFinancialStatement() {
//        return financialStatement;
//    }
//
//    public void setFinancialStatement(FinancialStatementEntity financialStatement) {
//        this.financialStatement = financialStatement;
//    }


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











