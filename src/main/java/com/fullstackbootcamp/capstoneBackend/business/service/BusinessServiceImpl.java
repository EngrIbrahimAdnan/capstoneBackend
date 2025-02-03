package com.fullstackbootcamp.capstoneBackend.business.service;

import com.fullstackbootcamp.capstoneBackend.auth.enums.TokenTypes;
import com.fullstackbootcamp.capstoneBackend.business.bo.AddBusinessRequest;
import com.fullstackbootcamp.capstoneBackend.business.dto.AddBusinessDTO;
import com.fullstackbootcamp.capstoneBackend.business.dto.getBusinessDTO;
import com.fullstackbootcamp.capstoneBackend.business.entity.BusinessEntity;
import com.fullstackbootcamp.capstoneBackend.business.entity.BusinessLicenseEntity;
import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessAdditionStatus;
import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessRetrievalStatus;
import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessState;
import com.fullstackbootcamp.capstoneBackend.business.repository.BusinessRepository;
import com.fullstackbootcamp.capstoneBackend.file.entity.FileEntity;
import com.fullstackbootcamp.capstoneBackend.file.service.FileService;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import com.fullstackbootcamp.capstoneBackend.user.enums.Roles;
import com.fullstackbootcamp.capstoneBackend.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    // Business owner adds their business
    @Override
    public AddBusinessDTO addBusiness(AddBusinessRequest request, Authentication authentication) {
        AddBusinessDTO response = new AddBusinessDTO();

        String message = validateToken(authentication); // Validate token and get response

        // validate Token
        if (message != null) {
            response.setStatus(BusinessAdditionStatus.FAIL);
            response.setMessage(message); // attach error message
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

        // Ensure the business owner doesn't have any businesses they are associated with, before the business is added
        /* NOTE:
            At the moment, its one-to-one relationship between business owner user and business entity
            However, Realistically, there can be more than one. But it's done so, for the sake of simplicity
         */
        Optional<BusinessEntity> businessEntity = businessRepository.findByBusinessOwnerUser(user.get());

        if (businessEntity.isPresent()) {
            response.setStatus(BusinessAdditionStatus.ALREADY_EXIST);
            response.setMessage("Business Owner already exists. There can only be one.");
            return response;
        }

        // The business entity is added
        BusinessEntity business = new BusinessEntity();

        // Add Business Owner user entity
        business.setBusinessOwnerUser(user.get());

        // add Business Nickname
        /* REVIEW:
            nickname doesn't have to be lower-cased since it won't be used to search with
            Its merely a convenience for the user
         */

        business.setBusinessNickname(request.getBusinessNickname());

        // Attempt to store financial statement document
        try {
            FileEntity entity = fileService.saveFile(request.getFinancialStatementPDF());

            // Upon successfully saving file, ID pointing to the financial statement file in file repository is added
            /* Hack:
                Although it looks disgusting to associate the business entity with the file entity this way,
                this gets rid of the error message "LOB: unable to stream". This might be revisited later due to how
                disgusting this looks. The error steams from adding relationships with large files (like images)
                will look for a way around it.
             */
            business.setFinancialStatementFileId(entity.getId());
        } catch (Exception e) {
            response.setStatus(BusinessAdditionStatus.FAIL);
            response.setMessage("Unable to upload PDF document.");
            return response;
        }

        // to store business license Image
        try {
            FileEntity entity = fileService.saveFile(request.getBusinessLicenseImage());

            /* HACK:
                Again, we are associating the business entity with the ID of the business license file
                instead of an entity relationship
             */
            business.setBusinessLicenseImageFileId(entity.getId());

        } catch (Exception e) {
            response.setStatus(BusinessAdditionStatus.FAIL);
            response.setMessage("Unable to upload business license as image.");
            return response;
        }

        /* Todo:
            Depending on how the string, containing the fields extracted from document, is
            structured and separated, perform logic here to set the following fields:
            -   Financial Statement
            -   Business License
            -   Financial Analysis
            -   Business State
            -   Financial Score
         */

        // add financial statement entity
        // TODO: change from null add all necessary fields
        business.setFinancialStatement(null);

        // add business license
        // TODO: set all other fields
        /* NOTE:
            error handling to check if a business license already associates with
            the business entity won't be necessary since the business entity is just created
            However, this error may come in handy if you use pgadmin to delete the business entity
         */
        BusinessLicenseEntity businessLicense = new BusinessLicenseEntity();
        businessLicense.setIssueDate(LocalDate.now());
        businessLicense.setLicenseNumber("23JSCL23");
        business.setBusinessLicense(businessLicense);

        // TODO: Set the financial analysis obtained from openai
        business.setFinancialAnalysis("This is a financial analysis based ...");

        // TODO: perform logic and switch statement to set the correct business state and score
        business.setBusinessState(BusinessState.CRITICAL);
        business.setFinancialScore(8.3);

        businessRepository.save(business);

        response.setStatus(BusinessAdditionStatus.SUCCESS);
        response.setMessage("Successfully added business to user.");
        return response;
    }


    public getBusinessDTO getBusiness(Authentication authentication) {
        getBusinessDTO response = new getBusinessDTO();

        String message = validateToken(authentication); // Validate token and get response

        // return a response if a message is returned
        if (message != null) {
            response.setStatus(BusinessRetrievalStatus.FAIL);
            response.setMessage(message);
            return response;
        }

        // ensure the user in the token exists
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Object civilId = jwt.getClaims().get("civilId"); // user civil id
        Optional<UserEntity> user = userService.getUserByCivilId(civilId.toString());

        // if use is not found in repository
        /* TODO: Instead of Fail, another enum value could be added so that it gets routed to an
            explicit case in the controller layer to utilize 'HttpStatus.NOT_FOUND' which is more
             appropriate than 'Bad_Request' when user is not found.
         */
        if (user.isEmpty()) {
            response.setStatus(BusinessRetrievalStatus.FAIL);
            response.setMessage("User does not exist");
            return response;
        }

        // check business exists
        Optional<BusinessEntity> businessEntity = getBusinessOwnerEntity(user.get());

        if (businessEntity.isEmpty()) {
            response.setStatus(BusinessRetrievalStatus.FAIL);
            response.setMessage("No business is associated with user");
            return response; // If there was an error during validation, return early
        }

        // Check existence of both files
        /* REVIEW: although this wouldn't throwout an error for 'financial Statement' & 'business license' since only the image Ids
         *   are stored, this is especially critical when the relationships are revisited and added between business entities
         *   and file entities. It doesn't hurt to also check for the time being*/

        Optional<FileEntity> financialStatement = fileService.getFile(businessEntity.get().getFinancialStatementFileId());

        if (financialStatement.isEmpty()) {
            response.setStatus(BusinessRetrievalStatus.FAIL);
            response.setMessage("No financial statement is associated with business");
            return response;
        }

        Optional<FileEntity> businessLicense = fileService.getFile(businessEntity.get().getBusinessLicenseImageFileId());

        if (businessLicense.isEmpty()) {
            response.setStatus(BusinessRetrievalStatus.FAIL);
            response.setMessage("No business license is associated with business");
            return response;
        }

        // if all is well, return success
        response.setStatus(BusinessRetrievalStatus.SUCCESS);
        response.setMessage("Business entity is successfully retrieved.");
        response.setEntity(businessEntity.get());
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

    public Optional<BusinessEntity> getBusinessOwnerEntity(UserEntity user) {
        return businessRepository.findByBusinessOwnerUser(user);
    }

}
