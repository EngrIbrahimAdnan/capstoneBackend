package com.fullstackbootcamp.capstoneBackend.loan.service;

import com.fullstackbootcamp.capstoneBackend.auth.enums.TokenTypes;
import com.fullstackbootcamp.capstoneBackend.business.entity.BusinessEntity;
import com.fullstackbootcamp.capstoneBackend.business.service.BusinessService;
import com.fullstackbootcamp.capstoneBackend.loan.bo.CreateLoanRequest;
import com.fullstackbootcamp.capstoneBackend.loan.bo.CreateLoanResponse;
import com.fullstackbootcamp.capstoneBackend.loan.dto.GetLoanRequestDTO;
import com.fullstackbootcamp.capstoneBackend.loan.dto.LoanRequestDTO;
import com.fullstackbootcamp.capstoneBackend.loan.dto.LoanResponseDTO;
import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanRequest;
import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanResponse;
import com.fullstackbootcamp.capstoneBackend.loan.enums.*;
import com.fullstackbootcamp.capstoneBackend.loan.repository.LoanRepository;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import com.fullstackbootcamp.capstoneBackend.user.enums.Roles;
import com.fullstackbootcamp.capstoneBackend.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;
    private final UserService userService;
    private final BusinessService businessService;


    public LoanServiceImpl(LoanRepository loanRepository,
                           UserService userService,
                           BusinessService businessService) {
        this.loanRepository = loanRepository;
        this.userService = userService;
        this.businessService = businessService;
    }

    /* Note:
     *  - createLoanRequest function is only for business owner to send original loan request to bank
     *  - createLoanResponse function is only for banker to either accept, counter, reject offer
     */
    @Override
    public LoanRequestDTO createLoanRequest(CreateLoanRequest request, Authentication authentication) {
        LoanRequestDTO response = new LoanRequestDTO();

        // Validate token and role and get message back if any error is encountered
        String message = validateToken(Roles.BUSINESS_OWNER, authentication);

        // return a response if a message is returned
        if (message != null) {
            response.setStatus(CreateLoanRequestStatus.FAIL);
            response.setMessage(message);
            return response;
        }

        // ensure the user in the token exists
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Object civilId = jwt.getClaims().get("civilId"); // user civil id
        Optional<UserEntity> user = userService.getUserByCivilId(civilId.toString());

        // if user is not found in repository
        /* TODO: Instead of Fail, another enum value could be added so that it gets routed to an
            explicit case in the controller layer to utilize 'HttpStatus.NOT_FOUND' which is more
             appropriate than 'Bad_Request' when user is not found.
         */
        if (user.isEmpty()) {
            response.setStatus(CreateLoanRequestStatus.FAIL);
            response.setMessage("User does not exist");
            return response;
        }

        // check business exists with user entity obtained
        Optional<BusinessEntity> businessEntity = businessService.getBusinessOwnerEntity(user.get());

        if (businessEntity.isEmpty()) {
            response.setStatus(CreateLoanRequestStatus.FAIL);
            response.setMessage("No business is associated with user");
            return response; // If there was an error during validation, return early
        }

        // default values
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setLoanResponses(new ArrayList<>()); // empty array for future loan responses
        loanRequest.setBanker(null); // null because no banker has assigned it to themselves
        loanRequest.setBusiness(businessEntity.get());
        loanRequest.setBank(request.getBank());

        /* TODO:
         *  - Make API call here and do logic based on:
         *  - loanPurpose, loanAmount, LoanTerm, Financial statement and business license
         */
        loanRequest.setRequestAnalysis("make api call here after doing logic");

        // default values upon creation
        loanRequest.setLoanTitle(request.getLoanTitle());
        loanRequest.setLoanPurpose(request.getLoanPurpose());
        loanRequest.setAmount(request.getAmount());
        loanRequest.setLoanTerm(request.getLoanTerm());
        loanRequest.setRepaymentPlan(request.getRepaymentPlan());
        loanRequest.setStatus(LoanRequestStatus.PENDING);
        loanRequest.setRejectionSource(RejectionSource.NONE); // rejected by None by default
        loanRequest.setReason(null);// no reason since it's not rejected yet
        loanRequest.setStatusDate(LocalDate.now()); // time of creation
        loanRequest.setViewed(false); // hasn't been viewed by user yet
        loanRepository.save(loanRequest);

        // if all is well, return success
        response.setStatus(CreateLoanRequestStatus.SUCCESS);
        response.setMessage("Loan Request is created.");
        return response;
    }

    public LoanResponseDTO createLoanResponse(CreateLoanResponse request, Authentication authentication) {
        LoanResponseDTO response = new LoanResponseDTO();

        // Validate token and ensure user is banker
        String message = validateToken(Roles.BANKER, authentication);

        // return a response if a message is returned
        if (message != null) {
            response.setStatus(CreateLoanResponseStatus.FAIL);
            response.setMessage(message);
            return response;
        }

        // ensure the user in the token exists
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Object civilId = jwt.getClaims().get("civilId"); // user civil id
        Optional<UserEntity> bankerUser = userService.getUserByCivilId(civilId.toString());

        // if use is not found in repository
        /* TODO: Instead of Fail, another enum value could be added so that it gets routed to an
            explicit case in the controller layer to utilize 'HttpStatus.NOT_FOUND' which is more
             appropriate than 'Bad_Request' when user is not found.
         */
        if (bankerUser.isEmpty()) {
            response.setStatus(CreateLoanResponseStatus.FAIL);
            response.setMessage("User does not exist");
            return response;
        }

        // ensure the loan request exists using the id
        Optional<LoanRequest> loanRequest = getLoanById(request.getLoanRequestId());

        if (loanRequest.isEmpty()) {
            response.setStatus(CreateLoanResponseStatus.FAIL);
            response.setMessage("No loan request is associated with id provided");
            return response; // If there was an error during validation, return early
        }

        // TODO: switch statement for each option

        // first case: the banker approves the request offer straight away
        LoanResponse loanResponse = new LoanResponse();
        loanResponse.setAmount(request.getAmount());
        loanResponse.setLoanTerm(request.getLoanTerm());
        loanResponse.setRepaymentPlan(request.getRepaymentPlan());
        loanResponse.setStatus(LoanRequestStatus.APPROVED);
        loanResponse.setStatusDate(LocalDate.now());
        loanResponse.setViewed(false);

        // update the original loan request upon the new loan response
        // submitted by banker
        LoanRequest updateLoanRequest = loanRequest.get();

        // Add the new loanResponse to the loanResponses list
        updateLoanRequest.getLoanResponses().add(loanResponse);
        updateLoanRequest.setLoanResponses(updateLoanRequest.getLoanResponses());

        // Assign the banker to request
        updateLoanRequest.setBanker(bankerUser.get());

        /* REVIEW:
         *  - here, We may reassign requestAnalysis field as well and feed it back into
         *  - AI endpoint with new information so that it acts as a
         *  - history analysis, encompassing all responses obtained so far
         */

        loanRepository.save(updateLoanRequest);

        // if all is well, return success
        response.setStatus(CreateLoanResponseStatus.SUCCESS);
        response.setMessage("Loan Request is created.");
        return response;


    }

    public GetLoanRequestDTO getLoanRequestById(Long id, Authentication authentication){
        GetLoanRequestDTO response = new GetLoanRequestDTO();

        Jwt jwt = (Jwt) authentication.getPrincipal();

        // To ensure the access token is provided and NOT the refresh token
        if (jwt.getClaims().get("type").equals(TokenTypes.REFRESH.name())) {
            response.setStatus(LoanRequestRetrievalStatus.FAIL);
            response.setMessage("Incorrect Token provided. Please provide access token");
            return response;
        }

        // ensure the loan request exists using the id
        Optional<LoanRequest> loanRequest = getLoanById(id);

        if (loanRequest.isEmpty()) {
            response.setStatus(LoanRequestRetrievalStatus.FAIL);
            response.setMessage("No loan request is associated with id provided");
            return response; // If there was an error during validation, return early
        }

        response.setStatus(LoanRequestRetrievalStatus.SUCCESS);
        response.setMessage("Loan Request entity is successfully retrieved");
        response.setEntity(loanRequest.get());
        return response;
    }


    public String validateToken(Roles role, Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();

        // To ensure the access token is provided and NOT the refresh token
        if (jwt.getClaims().get("type").equals(TokenTypes.REFRESH.name())) {
            return "Incorrect Token provided. Please provide access token";
        }

        // returns message if the user is anything but the passed 'role'
        if (!jwt.getClaims().get("roles").equals(role.name())) {
            return "Not allowed for provided role. This endpoint is only for " + role.name();
        }

        return null; // No errors, return null to continue the flow
    }

    public Optional<LoanRequest> getLoanById(Long id) {
        return loanRepository.findById(id);
    }

}
