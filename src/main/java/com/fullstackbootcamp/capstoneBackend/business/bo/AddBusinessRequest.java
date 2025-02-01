package com.fullstackbootcamp.capstoneBackend.business.bo;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class AddBusinessRequest {

    @NotNull(message = "The 'businessNickname' field is required and it's missing")
    private String businessNickname;

    @NotNull(message = "The 'financialStatementPDF' file is required and it's missing")
    private MultipartFile financialStatementPDF;

    @NotNull(message = "The 'businessLicenseImage' file is required and it's missing")
    private MultipartFile businessLicenseImage;

    // @Nullable NOTE: Nullable can be changed later to not null once the text scan works perfectly
    private String financialStatementText;

    // @Nullable NOTE: Nullable can be changed later to not null once the text scan works perfectly
    private String businessLicenseText;

    public String getBusinessNickname() {
        return businessNickname;
    }

    public void setBusinessNickname(String businessNickname) {
        this.businessNickname = businessNickname;
    }

    public MultipartFile getFinancialStatementPDF() {
        return financialStatementPDF;
    }

    public void setFinancialStatementPDF(MultipartFile financialStatementPDF) {
        this.financialStatementPDF = financialStatementPDF;
    }

    public MultipartFile getBusinessLicenseImage() {
        return businessLicenseImage;
    }

    public void setBusinessLicenseImage(MultipartFile businessLicenseImage) {
        this.businessLicenseImage = businessLicenseImage;
    }

    public String getFinancialStatementText() {
        return financialStatementText;
    }

    public void setFinancialStatementText(String financialStatementText) {
        this.financialStatementText = financialStatementText;
    }

    public String getBusinessLicenseText() {
        return businessLicenseText;
    }

    public void setBusinessLicenseText(String businessLicenseText) {
        this.businessLicenseText = businessLicenseText;
    }
}
