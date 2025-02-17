package com.fullstackbootcamp.capstoneBackend.auth.bo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateBusinessRequest {
    @JsonProperty("username")  // maps to the username in JSON
    private String username;

    @JsonProperty("businessNickname")
    private String businessNickname;

    @JsonProperty("businessAvatarFileId")
    private Long businessAvatarFileId;

    @JsonProperty("financialStatementFileId")
    private Long financialStatementFileId;

    @JsonProperty("businessLicenseImageFileId")
    private Long businessLicenseImageFileId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBusinessNickname() {
        return businessNickname;
    }

    public void setBusinessNickname(String businessNickname) {
        this.businessNickname = businessNickname;
    }

    public Long getBusinessAvatarFileId() {
        return businessAvatarFileId;
    }

    public void setBusinessAvatarFileId(Long businessAvatarFileId) {
        this.businessAvatarFileId = businessAvatarFileId;
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
}
