package com.fullstackbootcamp.capstoneBackend.business.controller;

import com.fullstackbootcamp.capstoneBackend.auth.dto.SignupResponseDTO;
import com.fullstackbootcamp.capstoneBackend.auth.enums.TokenTypes;
import com.fullstackbootcamp.capstoneBackend.business.bo.AddBusinessRequest;
import com.fullstackbootcamp.capstoneBackend.business.bo.AddBusinessRequestWithFiles;
import com.fullstackbootcamp.capstoneBackend.business.dto.AddBusinessDTO;
import com.fullstackbootcamp.capstoneBackend.business.dto.getBusinessDTO;
import com.fullstackbootcamp.capstoneBackend.business.entity.BusinessEntity;
import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessAdditionStatus;
import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessRetrievalStatus;
import com.fullstackbootcamp.capstoneBackend.business.service.BusinessService;
import com.fullstackbootcamp.capstoneBackend.user.bo.CreateUserRequest;
import com.fullstackbootcamp.capstoneBackend.user.enums.CreateUserStatus;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequestMapping("/business/v1")
@RestController
public class BusinessController {

    private final BusinessService businessService;

    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @PostMapping("/add")
    public ResponseEntity<AddBusinessDTO> addBusiness(@RequestParam("businessNickname") String businessNickname,
                                                      @RequestParam("financialStatementPDF") MultipartFile financialStatementPDF,
                                                      @RequestParam("businessLicenseImage") MultipartFile businessLicenseImage,
                                                      Authentication authentication) {

        AddBusinessRequestWithFiles request = new AddBusinessRequestWithFiles();
        request.setBusinessNickname(businessNickname);
        request.setFinancialStatementPDF(financialStatementPDF);
        request.setBusinessLicenseImage(businessLicenseImage);

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

}
