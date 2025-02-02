package com.fullstackbootcamp.capstoneBackend.loan.service;

import com.fullstackbootcamp.capstoneBackend.auth.enums.TokenTypes;
import com.fullstackbootcamp.capstoneBackend.business.bo.AddBusinessRequest;
import com.fullstackbootcamp.capstoneBackend.business.dto.AddBusinessDTO;
import com.fullstackbootcamp.capstoneBackend.business.dto.getBusinessDTO;
import com.fullstackbootcamp.capstoneBackend.business.entity.BusinessEntity;
import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessRetrievalStatus;
import com.fullstackbootcamp.capstoneBackend.business.service.BusinessService;
import com.fullstackbootcamp.capstoneBackend.loan.bo.CreateLoanRequest;
import com.fullstackbootcamp.capstoneBackend.loan.dto.LoanRequestDTO;
import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanRequest;
import com.fullstackbootcamp.capstoneBackend.loan.enums.CreateLoanRequestStatus;
import com.fullstackbootcamp.capstoneBackend.loan.enums.LoanRequestStatus;
import com.fullstackbootcamp.capstoneBackend.loan.enums.RejectionSource;
import com.fullstackbootcamp.capstoneBackend.loan.repository.LoanRepository;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import com.fullstackbootcamp.capstoneBackend.user.enums.CreateUserStatus;
import com.fullstackbootcamp.capstoneBackend.user.enums.Roles;
import com.fullstackbootcamp.capstoneBackend.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    @Override
    public LoanRequestDTO createLoanRequest(CreateLoanRequest request, Authentication authentication) {

        LoanRequestDTO response = new LoanRequestDTO();

        String message = validateToken(authentication); // Validate token and get response

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

        // if use is not found in repository
        /* TODO: Instead of Fail, another enum value could be added so that it gets routed to an
            explicit case in the controller layer to utilize 'HttpStatus.NOT_FOUND' which is more
             appropriate than 'Bad_Request' when user is not found.
         */
        if (user.isEmpty()) {
            response.setStatus(CreateLoanRequestStatus.FAIL);
            response.setMessage("User does not exist");
            return response;
        }

        // check business exists
        Optional<BusinessEntity> businessEntity = businessService.getBusinessOwnerEntity(user.get());

        if (businessEntity.isEmpty()) {
            response.setStatus(CreateLoanRequestStatus.FAIL);
            response.setMessage("No business is associated with user");
            return response; // If there was an error during validation, return early
        }

        // default values
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setBanker(null); // null because no banker has assigned it to themselves
        loanRequest.setBusiness(businessEntity.get());
        loanRequest.setBank(request.getBank());

        /* TODO:
         *  - Make API call here and do logic based on:
         *  - loanAmount, LoanTerm, Financial statement and business license
         */

        loanRequest.setRequestAnalysis("make api call here after doing logic");
        loanRequest.setAmount(request.getAmount());
        loanRequest.setLoanTerm(request.getLoanTerm());
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

}
