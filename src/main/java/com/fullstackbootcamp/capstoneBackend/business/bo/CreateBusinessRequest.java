package com.fullstackbootcamp.capstoneBackend.business.bo;

import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessState;
import jakarta.validation.constraints.NotNull;

public class CreateBusinessRequest {
    @NotNull(message = "The 'businessName' field is required and it's missing")
    private String businessName;
    @NotNull(message = "The 'businessLicense' field is required and it's missing")
    private String businessLicense;

    // TODO: Add financial statement

    public @NotNull(message = "The 'businessName' field is required and it's missing") String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(@NotNull(message = "The 'businessName' field is required and it's missing") String businessName) {
        this.businessName = businessName;
    }

    public @NotNull(message = "The 'businessLicense' field is required and it's missing") String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(@NotNull(message = "The 'businessLicense' field is required and it's missing") String businessLicense) {
        this.businessLicense = businessLicense;
    }
}
