package com.fullstackbootcamp.capstoneBackend.business.controller;


import com.fullstackbootcamp.capstoneBackend.business.bo.AddBusinessRequest;
import com.fullstackbootcamp.capstoneBackend.business.dto.AddBusinessDTO;
import com.fullstackbootcamp.capstoneBackend.business.dto.getBusinessDTO;
import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessAdditionStatus;
import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessRetrievalStatus;
import com.fullstackbootcamp.capstoneBackend.business.service.BusinessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/business")
@RestController
public class BusinessController {


    private final BusinessService businessService;

    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;

    }


    @PostMapping("/add")
    public ResponseEntity<AddBusinessDTO> addBusiness(@RequestParam("businessNickname") String businessNickname,
                                                      @RequestParam("financialStatementPDF") MultipartFile financialStatementPDF, // image file
                                                      @RequestParam("businessLicenseImage") MultipartFile businessLicenseImage, // image file
                                                      @RequestParam(value = "financialStatementText", required = false) String financialStatementText, // fields extracted into string
                                                      @RequestParam(value = "businessLicenseText", required = false) String businessLicenseText, // fields extracted into string
                                                      Authentication authentication) {

        /*
         NOTE:
          Manually creating the BO entity since @RequestBody is designed
          for parsing a single JSON object, not a mixed multipart/form-data request.
        */

        AddBusinessRequest request = new AddBusinessRequest();
        request.setBusinessNickname(businessNickname);

        // Uploaded files
        /* HACK:
            currently, financial statement is an image since many of the document-upload
            libraries are deprecated in react native. This may be revisited later.
         */
        request.setFinancialStatementPDF(financialStatementPDF);
        request.setBusinessLicenseImage(businessLicenseImage);

        // Extracted text from the two files, received from application
        /* Note:
            The text extractions are nullable. No error would be thrown out here
            if the fields weren't provided
         */
        request.setFinancialStatementText(financialStatementText);
        request.setBusinessLicenseText(businessLicenseText);

        AddBusinessDTO response = businessService.addBusiness(request, authentication);

        switch (response.getStatus()) {
            case SUCCESS: // accepted status returned for successfully adding business
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

            case FAIL: // bad request status returned when field is not in correct format
                return ResponseEntity.badRequest().body(response);

            case ALREADY_EXIST: // conflict status returned when business entity already exists
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

            default: // default error
                AddBusinessDTO noResponse = new AddBusinessDTO();
                noResponse.setStatus(BusinessAdditionStatus.FAIL);
                noResponse.setMessage("Error status unrecognized");
                return ResponseEntity.badRequest().body(noResponse);
        }
    }

    /* NOTE:
        As for the two files, this get method returns the business entity with only their ids.
        Due to how the response entity needs to be structured to return files, another
        endpoint needs to be called to retrieve those files.
     */
    @GetMapping("/get")
    public ResponseEntity<getBusinessDTO> getBusiness(Authentication authentication) {

        getBusinessDTO response = businessService.getBusiness(authentication);

        switch (response.getStatus()) {
            case SUCCESS: // accepted status returned for successfully adding business
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

            case FAIL: // bad request status returned when field is not in correct format
                return ResponseEntity.badRequest().body(response);

            default: // default error
                getBusinessDTO noResponse = new getBusinessDTO();
                noResponse.setStatus(BusinessRetrievalStatus.FAIL);
                noResponse.setMessage("Error status unrecognized");
                return ResponseEntity.badRequest().body(noResponse);
        }
    }

    /* TODO:
        Although the endpoint in file's controller works just fine to get financial statement & business license, it
        is possible to create endpoints here that return the financial statement & business license directly without passing
        any parameters by using the token of the user to get user entity, followed by getting business entity, and then lastly
        use the ids stored in the business entity to retrieve the files from the file repository.
    */
}
