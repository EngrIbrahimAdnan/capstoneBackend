package com.fullstackbootcamp.capstoneBackend.business.service;

import com.fullstackbootcamp.capstoneBackend.auth.dto.SignupResponseDTO;
import com.fullstackbootcamp.capstoneBackend.auth.enums.TokenTypes;
import com.fullstackbootcamp.capstoneBackend.business.bo.AddBusinessRequest;
import com.fullstackbootcamp.capstoneBackend.business.bo.AddBusinessRequestWithFiles;
import com.fullstackbootcamp.capstoneBackend.business.dto.AddBusinessDTO;
import com.fullstackbootcamp.capstoneBackend.business.dto.getBusinessDTO;
import com.fullstackbootcamp.capstoneBackend.business.entity.BusinessEntity;
import com.fullstackbootcamp.capstoneBackend.business.entity.BusinessEntityWithImages;
import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessAdditionStatus;
import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessRetrievalStatus;
import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessState;
import com.fullstackbootcamp.capstoneBackend.business.repository.BusinessRepository;
import com.fullstackbootcamp.capstoneBackend.file.entity.FileEntity;
import com.fullstackbootcamp.capstoneBackend.file.repository.FileRepository;
import com.fullstackbootcamp.capstoneBackend.file.service.FileService;
import com.fullstackbootcamp.capstoneBackend.user.bo.CreateUserRequest;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import com.fullstackbootcamp.capstoneBackend.user.enums.Roles;
import com.fullstackbootcamp.capstoneBackend.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Map;
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
    public AddBusinessDTO addBusiness(AddBusinessRequestWithFiles request, Authentication authentication) {
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
        Optional<UserEntity> user=userService.getUserByCivilId(civilId.toString());

        if (user.isEmpty()){
            response.setStatus(BusinessAdditionStatus.FAIL);
            response.setMessage("User does not exist.");
            return response;
        }

        BusinessEntityWithImages business = new BusinessEntityWithImages();
        business.setBusinessNickname(request.getBusinessNickname().toLowerCase());
        business.setBusinessOwnerUser(user.get());

//        business.setBusinessState(BusinessState.CRITICAL);// todo: currently hard coded, use the information from financial statment to calculate the state
//        business.setFinancialStatement(null);

//        business.setFinancialScore(8.3);// todo: currently hard coded, use the information from financial statment to calculate the state

        // to store financial statement pdf
        try {
            FileEntity entity = fileService.saveFile(request.getFinancialStatementPDF());
            business.setFinancialStatementPDF(entity);

        } catch (Exception e) {
            response.setStatus(BusinessAdditionStatus.FAIL);
            response.setMessage("Unable to upload PDF document.");
            return response;
        }

        // to store business license Image
        try {
            FileEntity entity = fileService.saveFile(request.getBusinessLicenseImage());
            business.setBusinessLicenseImage(entity);

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


    public getBusinessDTO getBusiness(Authentication authentication){
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
        Optional<UserEntity> user=userService.getUserByCivilId(civilId.toString());

        if (user.isEmpty()){
            response.setStatus(BusinessRetrievalStatus.FAIL);
            response.setMessage("User does not exist.");
            return response;
        }

        // check business exists
        Optional<BusinessEntityWithImages> businessEntity = businessRepository.findByBusinessOwnerUser(user.get());
        if (businessEntity.isEmpty()){
            response.setStatus(BusinessRetrievalStatus.FAIL);
            response.setMessage("Business entity does not exist.");
            return response;
        }

        response.setStatus(BusinessRetrievalStatus.SUCCESS);
        response.setMessage("Business entity does not exist.");

//        response.setEntity(businessEntity.get());

        return response;
    }

}
