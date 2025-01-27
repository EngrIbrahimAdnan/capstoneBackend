package com.fullstackbootcamp.capstoneBackend.business.entity;

import com.fullstackbootcamp.capstoneBackend.file.entity.FileEntity;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "businesses")
public class BusinessEntityWithImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "business_owner_user_id", nullable = false)
    private UserEntity businessOwnerUser;

    @Column(name = "business_Nickname", nullable = false, length = 100)
    private String businessNickname;

    @OneToOne
    @JoinColumn(name = "financial_statement_PDF", nullable = false)
    private FileEntity financialStatementPDF;

    @OneToOne
    @JoinColumn(name = "business_License_image", nullable = false)
    private FileEntity businessLicenseImage;

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

    public FileEntity getFinancialStatementPDF() {
        return financialStatementPDF;
    }

    public void setFinancialStatementPDF(FileEntity financialStatementPDF) {
        this.financialStatementPDF = financialStatementPDF;
    }

    public FileEntity getBusinessLicenseImage() {
        return businessLicenseImage;
    }

    public void setBusinessLicenseImage(FileEntity businessLicenseImage) {
        this.businessLicenseImage = businessLicenseImage;
    }
}
