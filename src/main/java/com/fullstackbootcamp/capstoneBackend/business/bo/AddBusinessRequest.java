package com.fullstackbootcamp.capstoneBackend.business.bo;

import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessState;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class AddBusinessRequest {

    @NotNull(message = "The 'businessNickname' field is required and it's missing")
    private String businessNickname;

    @NotNull(message = "The 'financialStatementPDF' file is required and it's missing")
    private MultipartFile financialStatementPDF;

    @NotNull(message = "The 'businessLicenseImage' file is required and it's missing")
    private MultipartFile businessLicenseImage;

    @Nullable // Nullable should be changed later once the text scan is implemented and tested to work
    private String financialStatement;

    @Nullable  // Nullable should be changed later once the text scan is implemented and tested to work
    private String businessLicense;

    @Nullable // Nullable should be changed later once the text scan and AI endpoint is implemented and tested to work
    private String statementAnalysis;

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

    public String getFinancialStatement() {
        return financialStatement;
    }

    public void setFinancialStatement(String financialStatement) {
        this.financialStatement = financialStatement;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    public String getStatementAnalysis() {
        return statementAnalysis;
    }

    public void setStatementAnalysis(String statementAnalysis) {
        this.statementAnalysis = statementAnalysis;
    }

}
