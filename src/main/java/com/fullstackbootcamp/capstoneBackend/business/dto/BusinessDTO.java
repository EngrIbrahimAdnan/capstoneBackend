package com.fullstackbootcamp.capstoneBackend.business.dto;

import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessState;

public class BusinessDTO {
    // TODO: Add financial score and business state once logic is implemented
    private String businessOwnerName;

    /* NOTE:
        these are commented out since business name is now a field of financial statement entity
        while businessLicense is now an entirely separate entity. I simply commented them out
        to avoid playing with the logic intended.
     */


//    private String businessName;
//    private String businessLicense;

    public BusinessDTO(String businessOwnerName
//                       ,String businessName
//                       ,String businessLicense
    ) {
        this.businessOwnerName = businessOwnerName;
//        this.businessName = businessName;
//        this.businessLicense = businessLicense;
    }

    public String getBusinessOwnerName() {
        return businessOwnerName;
    }

    public void setBusinessOwnerName(String businessOwnerName) {
        this.businessOwnerName = businessOwnerName;
    }

//    public String getBusinessName() {
//        return businessName;
//    }
//
//    public void setBusinessName(String businessName) {
//        this.businessName = businessName;
//    }
//
//    public String getBusinessLicense() {
//        return businessLicense;
//    }
//
//    public void setBusinessLicense(String businessLicense) {
//        this.businessLicense = businessLicense;
//    }
}
