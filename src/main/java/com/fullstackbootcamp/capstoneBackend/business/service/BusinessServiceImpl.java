package com.fullstackbootcamp.capstoneBackend.business.service;

import com.fullstackbootcamp.capstoneBackend.auth.enums.TokenTypes;
import com.fullstackbootcamp.capstoneBackend.business.bo.AddBusinessRequest;
import com.fullstackbootcamp.capstoneBackend.business.dto.AddBusinessDTO;
import com.fullstackbootcamp.capstoneBackend.business.dto.getBusinessDTO;
import com.fullstackbootcamp.capstoneBackend.business.entity.BusinessEntity;
import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessAdditionStatus;
import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessRetrievalStatus;
import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessState;
import com.fullstackbootcamp.capstoneBackend.business.repository.BusinessRepository;
import com.fullstackbootcamp.capstoneBackend.file.entity.FileEntity;
import com.fullstackbootcamp.capstoneBackend.file.service.FileService;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import com.fullstackbootcamp.capstoneBackend.user.enums.Roles;
import com.fullstackbootcamp.capstoneBackend.user.service.UserService;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Optional;

@Service
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;
    private final UserService userService;
    private final FileService fileService;

    public BusinessServiceImpl(BusinessRepository businessRepository, UserService userService, FileService fileService) {
        this.businessRepository = businessRepository;
        this.userService = userService;
        this.fileService = fileService;
    }


    @Override
    public AddBusinessDTO addBusiness2(AddBusinessRequest request, Authentication authentication) {
        AddBusinessDTO response = new AddBusinessDTO();

        String message = validateToken(authentication); // Validate token and get response

        // validate Token
        if (message != null) {
            response.setStatus(BusinessAdditionStatus.FAIL);
            response.setMessage(message);
            return response; // If there was an error during validation, return early
        }

        // ensure the user in the token exists
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Object civilId = jwt.getClaims().get("civilId"); // user civil id
        Optional<UserEntity> user = userService.getUserByCivilId(civilId.toString());

        if (user.isEmpty()) {
            response.setStatus(BusinessAdditionStatus.FAIL);
            response.setMessage("User does not exist.");
            return response;
        }

        // For the time being, we assume each business owner can only have one business
        // this can be later changed.

        System.out.println("Before call");

        Optional<BusinessEntity> businessEntity = businessRepository.findByBusinessOwnerUser(user.get());


        if (businessEntity.isPresent()) {
            response.setStatus(BusinessAdditionStatus.ALREADY_EXIST);
            response.setMessage("Business Owner already exists. There can only be one.");
            System.out.println("in here");
            return response;
        }

        BusinessEntity business = new BusinessEntity();
        business.setBusinessNickname(request.getBusinessNickname().toLowerCase());
        business.setBusinessOwnerUser(user.get());

        business.setBusinessState(BusinessState.CRITICAL);// todo: currently hard coded, use the information from financial statment to calculate the state
//        business.setFinancialStatement(null);

        business.setFinancialScore(8.3);// todo: currently hard coded, use the information from financial statment to calculate the state

        // to store financial statement pdf
        try {
            FileEntity entity = fileService.saveFile(request.getFinancialStatementPDF());
            business.setFinancialStatementId(entity.getId());

        } catch (Exception e) {
            response.setStatus(BusinessAdditionStatus.FAIL);
            response.setMessage("Unable to upload PDF document.");
            return response;
        }

        // to store business license Image
        try {
            FileEntity entity = fileService.saveFile(request.getBusinessLicenseImage());
            business.setBusinessLicenseImageId(entity.getId());

        } catch (Exception e) {
            response.setStatus(BusinessAdditionStatus.FAIL);
            response.setMessage("Unable to upload business license as image.");
            return response;
        }


        businessRepository.save(business);

        response.setStatus(BusinessAdditionStatus.SUCCESS);
        response.setMessage("Successfully added business to user.");
        return response;

    }


    @Override
    public AddBusinessDTO addBusiness(AddBusinessRequest request, Authentication authentication) {
//        AddBusinessDTO response = new AddBusinessDTO();
//
//        String message = validateToken(authentication); // Validate token and get response
//
//        // validate Token
//        if (message != null) {
//            response.setStatus(BusinessAdditionStatus.FAIL);
//            response.setMessage(message);
//            return response; // If there was an error during validation, return early
//        }
//
//        // ensure the user in the token exists
//        Jwt jwt = (Jwt) authentication.getPrincipal();
//        Object civilId = jwt.getClaims().get("civilId"); // user civil id
//        Optional<UserEntity> user = userService.getUserByCivilId(civilId.toString());
//
//        if (user.isEmpty()) {
//            response.setStatus(BusinessAdditionStatus.FAIL);
//            response.setMessage("User does not exist.");
//            return response;
//        }
//
//        // For the time being, we assume each business owner can only have one business
//        // this can be later changed.
//
//        System.out.println("Before call");
//
//        Optional<BusinessEntity> businessEntity = businessRepository.findByBusinessOwnerUser(user.get());
//
//
//        if (businessEntity.isPresent()) {
//            response.setStatus(BusinessAdditionStatus.ALREADY_EXIST);
//            response.setMessage("Business Owner already exists. There can only be one.");
//            System.out.println("in here");
//            return response;
//        }
//
//        BusinessEntity business = new BusinessEntity();
//        business.setBusinessNickname(request.getBusinessNickname().toLowerCase());
//        business.setBusinessOwnerUser(user.get());
//
//        business.setBusinessState(BusinessState.CRITICAL);// todo: currently hard coded, use the information from financial statment to calculate the state
////        business.setFinancialStatement(null);
//
//        business.setFinancialScore(8.3);// todo: currently hard coded, use the information from financial statment to calculate the state
//
//        // to store financial statement pdf
//        try {
//            FileEntity entity = fileService.saveFile(request.getFinancialStatementPDF());
//            business.setFinancialStatementPDF(entity);
//
//        } catch (Exception e) {
//            response.setStatus(BusinessAdditionStatus.FAIL);
//            response.setMessage("Unable to upload PDF document.");
//            return response;
//        }
//
//        // to store business license Image
//        try {
//            FileEntity entity = fileService.saveFile(request.getBusinessLicenseImage());
//            business.setBusinessLicenseImage(entity);
//
//        } catch (Exception e) {
//            response.setStatus(BusinessAdditionStatus.FAIL);
//            response.setMessage("Unable to upload business license as image.");
//            return response;
//        }
//
//
//        businessRepository.save(business);
//
//        response.setStatus(BusinessAdditionStatus.SUCCESS);
//        response.setMessage("Successfully added business to user.");
//        return response;

        return new AddBusinessDTO();

    }


    public String validateToken(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();

        // To ensure the access token is provided and NOT the refresh token
        if (jwt.getClaims().get("type").equals(TokenTypes.REFRESH.name())) {
            return "Incorrect Token provided. Please provide access token";
        }

        // Ensures the user is business owner
        if (jwt.getClaims().get("roles").equals(Roles.BANKER.name())) {
            return "Not allowed for bankers. This endpoint is only for Business Owners";
        }

        return null; // No errors, return null to continue the flow
    }

    public ResponseEntity<?> getBusiness2(Authentication authentication){
        getBusinessDTO response = new getBusinessDTO();

        String message = validateToken(authentication); // Validate token and get response

        // validate Token
        if (message != null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }

        // ensure the user in the token exists
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Object civilId = jwt.getClaims().get("civilId"); // user civil id
        Optional<UserEntity> user = userService.getUserByCivilId(civilId.toString());

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist.");
        }

        // check business exists
        Optional<BusinessEntity> businessEntity = businessRepository.findByBusinessOwnerUser(user.get());

        if (businessEntity.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Business entity not found.");
        }

        // Prepare headers and files
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        // Add the business entity as JSON
        body.add("businessEntity", businessEntity);


        // Check existence of file
        Optional<FileEntity> financialStatement= fileService.getFile(businessEntity.get().getFinancialStatementId());

        if (financialStatement.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Financial Statement file not found for business entity .");
        }


        // Add the financial statement as a file attachment
        HttpHeaders fileHeaders1 = new HttpHeaders();
        fileHeaders1.setContentDispositionFormData("attachment", financialStatement.get().getName());
        fileHeaders1.setContentType(MediaType.parseMediaType(financialStatement.get().getType()));
        HttpEntity<byte[]> financialStatementPart = new HttpEntity<>(financialStatement.get().getData(), fileHeaders1);
        body.add("financialStatementPDF", financialStatementPart);


        // Check existence of file
        Optional<FileEntity> businessLicense= fileService.getFile(businessEntity.get().getBusinessLicenseImageId());

        if (businessLicense.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Business license file not found for current business entity.");
        }

        // Add the business license as a file attachment
        HttpHeaders fileHeaders2 = new HttpHeaders();
        fileHeaders2.setContentDispositionFormData("attachment", businessLicense.get().getName());
        fileHeaders2.setContentType(MediaType.parseMediaType(businessLicense.get().getType()));
        HttpEntity<byte[]> businessLicensePart = new HttpEntity<>(businessLicense.get().getData(), fileHeaders2);
        body.add("businessLicenseImage", businessLicensePart);

        // Build the response
        return new ResponseEntity<>(body, headers, HttpStatus.OK);

    }


    public getBusinessDTO getBusiness(Authentication authentication) {
        getBusinessDTO response = new getBusinessDTO();

        String message = validateToken(authentication); // Validate token and get response

        // validate Token
        if (message != null) {
            response.setStatus(BusinessRetrievalStatus.FAIL);
            response.setMessage(message);
            return response; // If there was an error during validation, return early
        }

        // ensure the user in the token exists
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Object civilId = jwt.getClaims().get("civilId"); // user civil id
        Optional<UserEntity> user = userService.getUserByCivilId(civilId.toString());

        if (user.isEmpty()) {
            response.setStatus(BusinessRetrievalStatus.FAIL);
            response.setMessage("User does not exist.");
            return response;
        }

        // check business exists
        Optional<BusinessEntity> businessEntity = businessRepository.findByBusinessOwnerUser(user.get());

        if (businessEntity.isEmpty()) {
            response.setStatus(BusinessRetrievalStatus.FAIL);
            response.setMessage("Business entity does not exist.");
            return response;
        }

        response.setStatus(BusinessRetrievalStatus.SUCCESS);
        response.setMessage("Business entity is successfully retrieved.");
        response.setEntity(businessEntity.get());

        return response;
    }

}
