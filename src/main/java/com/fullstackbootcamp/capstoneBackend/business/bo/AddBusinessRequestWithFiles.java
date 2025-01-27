package com.fullstackbootcamp.capstoneBackend.business.bo;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class AddBusinessRequestWithFiles {
    @NotNull(message = "The 'businessNickname' field is required and it's missing")
    private String businessNickname;

    @NotNull(message = "The 'financialStatementPDF' file is required and it's missing")
    private MultipartFile financialStatementPDF;

    @NotNull(message = "The 'businessLicenseImage' file is required and it's missing")
    private MultipartFile businessLicenseImage;

    public @NotNull(message = "The 'businessNickname' field is required and it's missing") String getBusinessNickname() {
        return businessNickname;
    }

    public void setBusinessNickname(@NotNull(message = "The 'businessNickname' field is required and it's missing") String businessNickname) {
        this.businessNickname = businessNickname;
    }

    public @NotNull(message = "The 'financialStatementPDF' file is required and it's missing") MultipartFile getFinancialStatementPDF() {
        return financialStatementPDF;
    }

    public void setFinancialStatementPDF(@NotNull(message = "The 'financialStatementPDF' file is required and it's missing") MultipartFile financialStatementPDF) {
        this.financialStatementPDF = financialStatementPDF;
    }

    public @NotNull(message = "The 'businessLicenseImage' file is required and it's missing") MultipartFile getBusinessLicenseImage() {
        return businessLicenseImage;
    }

    public void setBusinessLicenseImage(@NotNull(message = "The 'businessLicenseImage' file is required and it's missing") MultipartFile businessLicenseImage) {
        this.businessLicenseImage = businessLicenseImage;
    }
}
