package com.fullstackbootcamp.capstoneBackend.loan.service;

import com.fullstackbootcamp.capstoneBackend.auth.enums.TokenTypes;
import com.fullstackbootcamp.capstoneBackend.business.entity.BusinessEntity;
import com.fullstackbootcamp.capstoneBackend.business.service.BusinessService;
import com.fullstackbootcamp.capstoneBackend.loan.bo.CreateLoanRequest;
import com.fullstackbootcamp.capstoneBackend.loan.bo.CreateLoanResponse;
import com.fullstackbootcamp.capstoneBackend.loan.dto.CheckNotificationDTO;
import com.fullstackbootcamp.capstoneBackend.loan.dto.GetLoanRequestDTO;
import com.fullstackbootcamp.capstoneBackend.loan.dto.LoanRequestDTO;
import com.fullstackbootcamp.capstoneBackend.loan.dto.LoanResponseDTO;
import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanRequest;
import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanResponse;
import com.fullstackbootcamp.capstoneBackend.loan.enums.*;
import com.fullstackbootcamp.capstoneBackend.loan.repository.LoanRepository;
import com.fullstackbootcamp.capstoneBackend.loan.repository.LoanResponseRepository;
import com.fullstackbootcamp.capstoneBackend.notification.entity.NotificationEntity;
import com.fullstackbootcamp.capstoneBackend.notification.service.NotificationsService;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import com.fullstackbootcamp.capstoneBackend.user.enums.Roles;
import com.fullstackbootcamp.capstoneBackend.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;
    private final LoanResponseRepository loanResponseRepository;
    private final UserService userService;
    private final BusinessService businessService;
    private final NotificationsService notificationsService;



    public LoanServiceImpl(LoanRepository loanRepository,
                           UserService userService,
                           BusinessService businessService,
                           LoanResponseRepository loanResponseRepository,
                           NotificationsService notificationsService) {
        this.loanRepository = loanRepository;
        this.userService = userService;
        this.businessService = businessService;
        this.loanResponseRepository = loanResponseRepository;
        this.notificationsService = notificationsService;

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
        loanRequest.setBusiness(businessEntity.get());

        /* TODO:
         *  - Make API call here and do logic based on a list of parameters, in addition to:
         *  - loanPurpose, loanAmount, LoanTerm, Financial statement and business license
         */
        loanRequest.setRequestAnalysis("make api call here after doing logic");


        // default values upon creation before request
        loanRequest.setSelectedBanks(request.getSelectedBanks());
        loanRequest.setLoanTitle(request.getLoanTitle());
        loanRequest.setLoanPurpose(request.getLoanPurpose());
        loanRequest.setAmount(request.getAmount());
        loanRequest.setLoanTerm(request.getLoanTerm());
        loanRequest.setRepaymentPlan(request.getRepaymentPlan());
        loanRequest.setStatus(LoanRequestStatus.PENDING);
        loanRequest.setRejectionSource(RejectionSource.NONE); // rejected by None by default
        loanRequest.setReason(null);// no reason since it's not rejected yet
        loanRequest.setStatusDate(LocalDateTime.now()); // time of creation

        // for notifications view track
        loanRequest.setNotificationEntityList(new ArrayList<>()); // empty array for future loan responses


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

        // if user is not found in repository
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
        Optional<LoanRequest> loanRequest = getLoanRequestById(request.getLoanRequestId());

        if (loanRequest.isEmpty()) {
            response.setStatus(CreateLoanResponseStatus.FAIL);
            response.setMessage("No loan request is associated with id provided");
            return response; // If there was an error during validation, return early
        }

        // TODO: switch statement for each option

        // first case: the banker approves the request offer straight away
        LoanResponse loanResponse = new LoanResponse();
        loanResponse.setBanker(bankerUser.get());
        loanResponse.setAmount(request.getAmount());
        loanResponse.setLoanTerm(request.getLoanTerm());
        loanResponse.setRepaymentPlan(request.getRepaymentPlan());
        loanResponse.setStatus(request.getResponseStatus());
        loanResponse.setStatusDate(LocalDateTime.now());

        // for notifications view track
        loanResponse.setNotificationEntityList(new ArrayList<>());

        // update the original loan request upon the new loan response
        // submitted by banker
        LoanRequest updateLoanRequest = loanRequest.get();

        // Add the new loanResponse to the loanResponses list
        updateLoanRequest.getLoanResponses().add(loanResponse);
//        updateLoanRequest.setLoanResponses(updateLoanRequest.getLoanResponses());

        /* Note:
         *  - NEW_RESPONSE means the user has received an offer that he is either going to accept or negotiates
         */
        updateLoanRequest.setStatus(LoanRequestStatus.NEW_RESPONSE);
        updateLoanRequest.setStatusDate(LocalDateTime.now());

        /* Note:
         *  - reset the arrayList to be new.
         *  - this way all notifications associated with this loanResponse is reseted
         *  - this results in all users having to view the notifications
         */
        updateLoanRequest.setNotificationEntityList(new ArrayList<>());


        /* REVIEW:
         *  - here, We may also reassign requestAnalysis field as well and feed it back into
         *  - AI endpoint with new information so that it acts as a
         *  - history analysis, encompassing all responses obtained so far
         */

        // save both entities together once no error is encountered
        loanResponseRepository.save(loanResponse);
        LoanRequest loanRequests = loanRepository.save(updateLoanRequest);

        // Ensure banker's previous responses are revoked
        revokePreviousLoanResponses(loanRequests.getLoanResponses(), bankerUser.get());

        // if all is well, return success
        response.setStatus(CreateLoanResponseStatus.SUCCESS);
        response.setMessage("Loan Response is successfully sent to Business owner.");
        return response;


    }

    public GetLoanRequestDTO getLoanRequestById(Long id, Authentication authentication) {
        GetLoanRequestDTO response = new GetLoanRequestDTO();

        Jwt jwt = (Jwt) authentication.getPrincipal();

        // To ensure the access token is provided and NOT the refresh token
        if (jwt.getClaims().get("type").equals(TokenTypes.REFRESH.name())) {
            response.setStatus(LoanRequestRetrievalStatus.FAIL);
            response.setMessage("Incorrect Token provided. Please provide access token");
            return response;
        }


        // ensure the loan request exists using the id
        Optional<LoanRequest> loanRequest = getLoanRequestById(id);

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

    public CheckNotificationDTO viewRequest(Long id, Authentication authentication){
        CheckNotificationDTO response = new CheckNotificationDTO();

        Jwt jwt = (Jwt) authentication.getPrincipal();

        // To ensure the access token is provided and NOT the refresh token
        if (jwt.getClaims().get("type").equals(TokenTypes.REFRESH.name())) {
            response.setStatus(CheckNotificationStatus.FAIL);
            response.setMessage("Incorrect Token provided. Please provide access token");
            return response;
        }

        // ensure the user in the token exists
        Object civilId = jwt.getClaims().get("civilId"); // user civil id
        Optional<UserEntity> user = userService.getUserByCivilId(civilId.toString());

        // if user is not found in repository
        /* TODO: Instead of Fail, another enum value could be added so that it gets routed to an
            explicit case in the controller layer to utilize 'HttpStatus.NOT_FOUND' which is more
             appropriate than 'Bad_Request' when user is not found.
         */
        if (user.isEmpty()) {
            response.setStatus(CheckNotificationStatus.FAIL);
            response.setMessage("User does not exist");
            return response;
        }

        // ensure the loan request exists using the id
        Optional<LoanRequest> loanRequest = getLoanRequestById(id);

        if (loanRequest.isEmpty()) {
            response.setStatus(CheckNotificationStatus.FAIL);
            response.setMessage("No loan request is associated with id provided");
            return response; // If there was an error during validation, return early
        }


        NotificationEntity notification = new NotificationEntity();
//        notification.setUser(user.get());
        NotificationEntity savedNotification = notificationsService.saveNotificationEntity(notification);

        loanRequest.get().getNotificationEntityList().add(savedNotification);
        loanRepository.save(loanRequest.get());

        response.setStatus(CheckNotificationStatus.FAIL);
        response.setMessage("Request has been viewed by user");
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

    public Optional<LoanRequest> getLoanRequestById(Long id) {
        return loanRepository.findById(id);
    }


    public void revokePreviousLoanResponses(List<LoanResponse> loanResponses, UserEntity user) {
        // Filter responses belonging to the specific user
        List<LoanResponse> userLoans = loanResponses.stream()
                .filter(response -> response.getBanker().equals(user))
                .sorted(Comparator.comparing(LoanResponse::getStatusDate).thenComparing(LoanResponse::getId).reversed()) // Sort by Date first, then ID
                .collect(Collectors.toList());

        // Ensure at least one exists
        if (userLoans.isEmpty()) {
            return;
        }

        for (int i = 1; i < userLoans.size(); i++) {
            userLoans.get(i).setStatus(LoanResponseStatus.RESCINDED);
        }

        // If you are using a database, persist the changes
        loanResponseRepository.saveAll(userLoans.subList(1, userLoans.size())); // Save only revoked ones
    }


}
