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
import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanRequestEntity;
import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanResponseEntity;
import com.fullstackbootcamp.capstoneBackend.loan.enums.*;
import com.fullstackbootcamp.capstoneBackend.loan.repository.LoanRequestRepository;
import com.fullstackbootcamp.capstoneBackend.loan.repository.LoanResponseRepository;
import com.fullstackbootcamp.capstoneBackend.notification.entity.NotificationEntity;
import com.fullstackbootcamp.capstoneBackend.notification.service.NotificationsService;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import com.fullstackbootcamp.capstoneBackend.user.enums.Bank;
import com.fullstackbootcamp.capstoneBackend.user.enums.Roles;
import com.fullstackbootcamp.capstoneBackend.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {
    private final LoanRequestRepository loanRequestRepository;
    private final LoanResponseRepository loanResponseRepository;
    private final UserService userService;
    private final BusinessService businessService;
    private final NotificationsService notificationsService;



    public LoanServiceImpl(LoanRequestRepository loanRequestRepository,
                           UserService userService,
                           BusinessService businessService,
                           LoanResponseRepository loanResponseRepository,
                           NotificationsService notificationsService) {
        this.loanRequestRepository = loanRequestRepository;
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
        LoanRequestEntity loanRequestEntity = new LoanRequestEntity();
        loanRequestEntity.setLoanResponses(new ArrayList<>()); // empty array for future loan responses
        loanRequestEntity.setBusiness(businessEntity.get());

        /* TODO:
         *  - Make API call here and do logic based on a list of parameters, in addition to:
         *  - loanPurpose, loanAmount, LoanTerm, Financial statement and business license
         */
        loanRequestEntity.setRequestAnalysis("make api call here after doing logic");


        // default values upon creation before request
        loanRequestEntity.setSelectedBanks(request.getSelectedBanks());
        loanRequestEntity.setLoanTitle(request.getLoanTitle());
        loanRequestEntity.setLoanPurpose(request.getLoanPurpose());
        loanRequestEntity.setAmount(request.getAmount());
        loanRequestEntity.setLoanTerm(request.getLoanTerm());
        loanRequestEntity.setRepaymentPlan(request.getRepaymentPlan());
        loanRequestEntity.setStatus(LoanRequestStatus.PENDING);
        loanRequestEntity.setRejectionSource(RejectionSource.NONE); // rejected by None by default
        loanRequestEntity.setReason(null);// no reason since it's not rejected yet
        loanRequestEntity.setStatusDate(LocalDateTime.now()); // time of creation

        // for notifications view track
        loanRequestEntity.setLoanRequestNotifications(new ArrayList<>()); // empty array for future loan responses


        loanRequestRepository.save(loanRequestEntity);

        // if all is well, return success
        response.setStatus(CreateLoanRequestStatus.SUCCESS);
        response.setMessage("Loan Request is created.");
        return response;
    }

    @Override
    public Map<String, Object> getLoanRequestsPageable(
            int page,
            String status,
            String search,
            int limit,
            Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        // Validate token and ensure user is banker
        String message = validateToken(Roles.BANKER, authentication);
        if (message != null) {
            response.put("status", CreateLoanResponseStatus.FAIL);
            response.put("message", message);
            return response;
        }

        // Ensure the user exists
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Object civilId = jwt.getClaims().get("civilId");
        Optional<UserEntity> bankerUser = userService.getUserByCivilId(civilId.toString());
        if (bankerUser.isEmpty()) {
            response.put("status", CreateLoanResponseStatus.FAIL);
            response.put("message", "User does not exist");
            return response;
        }

        Bank bank = bankerUser.get().getBank();
        Pageable pageable = PageRequest.of(page, limit, Sort.by("statusDate").descending());

        // Determine which filters are applied.
        boolean isStatusFilterApplied = status != null && !status.isEmpty() && !status.equalsIgnoreCase("all");
        boolean isSearchFilterApplied = search != null && !search.isEmpty();

        Page<LoanRequestEntity> loanRequestPage;
        if (!isStatusFilterApplied && !isSearchFilterApplied) {
            // No filtering—return all requests for the bank.
            loanRequestPage = loanRequestRepository.findBySelectedBankPageable(bank, pageable);
        } else if (isStatusFilterApplied && !isSearchFilterApplied) {
            // Filter only by status.
            LoanRequestStatus loanRequestStatus = LoanRequestStatus.valueOf(status);
            loanRequestPage = loanRequestRepository.findBySelectedBankAndStatusPageable(bank, loanRequestStatus, pageable);
        } else if (!isStatusFilterApplied && isSearchFilterApplied) {
            // Filter only by search string.
            loanRequestPage = loanRequestRepository.findBySelectedBankAndSearchPageable(bank, search, pageable);
        } else {
            // Filter by both status and search string.
            LoanRequestStatus loanRequestStatus = LoanRequestStatus.valueOf(status);
            loanRequestPage = loanRequestRepository.findBySelectedBankAndStatusAndSearchPageable(bank, loanRequestStatus, search, pageable);
        }

        // Build response: convert entities to a JSON-friendly structure.
        response.put("status", CreateLoanResponseStatus.SUCCESS);
        List<Map<String, Object>> requestDetails = loanRequestPage.getContent().stream().map(request -> {
            Map<String, Object> details = new HashMap<>();
            details.put("id", request.getId());
            details.put("businessName", request.getBusiness().getBusinessNickname());
            details.put("businessOwner", request.getBusiness().getBusinessOwnerUser().getFirstName()
                    + " " + request.getBusiness().getBusinessOwnerUser().getLastName());
            details.put("amount", request.getAmount());
            details.put("paymentPeriod", request.getLoanTerm());
            details.put("status", request.getStatus());
            details.put("date", request.getStatusDate());
            return details;
        }).collect(Collectors.toList());
        response.put("requests", requestDetails);
        response.put("totalRecords", loanRequestPage.getTotalElements());

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
        Optional<LoanRequestEntity> loanRequest = getLoanRequestById(request.getLoanRequestId());

        if (loanRequest.isEmpty()) {
            response.setStatus(CreateLoanResponseStatus.FAIL);
            response.setMessage("No loan request is associated with id provided");
            return response; // If there was an error during validation, return early
        }

        // TODO: switch statement for each option

        // first case: the banker approves the request offer straight away
        LoanResponseEntity loanResponseEntity = new LoanResponseEntity();
        loanResponseEntity.setBanker(bankerUser.get());
        loanResponseEntity.setAmount(request.getAmount());
        loanResponseEntity.setLoanTerm(request.getLoanTerm());
        loanResponseEntity.setRepaymentPlan(request.getRepaymentPlan());
        loanResponseEntity.setStatus(request.getResponseStatus());
        loanResponseEntity.setStatusDate(LocalDateTime.now());

        // for notifications view track
        loanResponseEntity.setLoanResponseNotifications(new ArrayList<>());

        // update the original loan request upon the new loan response
        // submitted by banker
        LoanRequestEntity updateLoanRequestEntity = loanRequest.get();

        // Add the new loanResponse to the loanResponses list
        updateLoanRequestEntity.getLoanResponses().add(loanResponseEntity);
        updateLoanRequestEntity.setLoanResponses(updateLoanRequestEntity.getLoanResponses());

        /* Note:
         *  - NEW_RESPONSE means the user has received an offer that he is either going to accept or negotiates
         */
        updateLoanRequestEntity.setStatus(LoanRequestStatus.NEW_RESPONSE);
        updateLoanRequestEntity.setStatusDate(LocalDateTime.now());

        /* Note:
         *  - reset the arrayList to be new.
         *  - this way all notifications associated with this loanResponse is reseted
         *  - this results in all users having to view the notifications
         */

        updateLoanRequestEntity.setLoanRequestNotifications(new ArrayList<>());


        /* REVIEW:
         *  - here, We may also reassign requestAnalysis field as well and feed it back into
         *  - AI endpoint with new information so that it acts as a
         *  - history analysis, encompassing all responses obtained so far
         */

        // save both entities together once no error is encountered
        loanResponseRepository.save(loanResponseEntity);
        LoanRequestEntity loanRequestsEntity = loanRequestRepository.save(updateLoanRequestEntity);

        // Ensure banker's previous responses are revoked
        revokePreviousLoanResponses(loanRequestsEntity.getLoanResponses(), bankerUser.get());

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
        Optional<LoanRequestEntity> loanRequest = getLoanRequestById(id);

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
        Optional<LoanRequestEntity> loanRequest = getLoanRequestById(id);

        if (loanRequest.isEmpty()) {
            response.setStatus(CheckNotificationStatus.FAIL);
            response.setMessage("No loan request is associated with id provided");
            return response; // If there was an error during validation, return early
        }


        NotificationEntity notification = new NotificationEntity();
        notification.setUser(user.get());

        System.out.println(user.get().getCivilId());
        System.out.println(user.get().getFirstName());


        NotificationEntity newNotification=  notificationsService.saveNotificationEntity(notification);

        loanRequest.get().getLoanRequestNotifications().add(newNotification);

        System.out.println(user.get().getCivilId());
        System.out.println(user.get().getFirstName());


        loanRequestRepository.save(loanRequest.get());

        System.out.println("after save");

        response.setStatus(CheckNotificationStatus.SUCCESS);
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

    public Optional<LoanRequestEntity> getLoanRequestById(Long id) {
        return loanRequestRepository.findById(id);
    }

    public void revokePreviousLoanResponses(List<LoanResponseEntity> loanResponsEntities, UserEntity user) {
        // Filter responses belonging to the specific user
        List<LoanResponseEntity> userLoans = loanResponsEntities.stream()
                .filter(response -> response.getBanker().equals(user))
                .sorted(Comparator.comparing(LoanResponseEntity::getStatusDate).thenComparing(LoanResponseEntity::getId).reversed()) // Sort by Date first, then ID
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
