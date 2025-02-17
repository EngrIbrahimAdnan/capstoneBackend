package com.fullstackbootcamp.capstoneBackend.loan.service;

import com.fullstackbootcamp.capstoneBackend.auth.bo.CustomUserDetails;
import com.fullstackbootcamp.capstoneBackend.auth.enums.TokenTypes;
import com.fullstackbootcamp.capstoneBackend.business.entity.BusinessEntity;
import com.fullstackbootcamp.capstoneBackend.business.service.BusinessService;
import com.fullstackbootcamp.capstoneBackend.chat.entity.ChatEntity;
import com.fullstackbootcamp.capstoneBackend.chat.enums.NotificationType;
import com.fullstackbootcamp.capstoneBackend.chat.repository.ChatRepository;
import com.fullstackbootcamp.capstoneBackend.file.entity.FileEntity;
import com.fullstackbootcamp.capstoneBackend.file.service.FileService;
import com.fullstackbootcamp.capstoneBackend.loan.bo.CreateLoanRequest;
import com.fullstackbootcamp.capstoneBackend.loan.bo.CreateLoanResponse;
import com.fullstackbootcamp.capstoneBackend.loan.bo.CreateOfferResponse;
import com.fullstackbootcamp.capstoneBackend.loan.dto.*;
import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanRequestEntity;
import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanResponseEntity;
import com.fullstackbootcamp.capstoneBackend.loan.enums.*;
import com.fullstackbootcamp.capstoneBackend.loan.repository.LoanRequestRepository;
import com.fullstackbootcamp.capstoneBackend.loan.repository.LoanResponseRepository;
import com.fullstackbootcamp.capstoneBackend.loanNotification.entity.LoanNotificationEntity;
import com.fullstackbootcamp.capstoneBackend.loanNotification.service.LoanNotificationsService;
import com.fullstackbootcamp.capstoneBackend.notification.entity.NotificationEntity;
import com.fullstackbootcamp.capstoneBackend.notification.service.NotificationService;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import com.fullstackbootcamp.capstoneBackend.user.enums.Bank;
import com.fullstackbootcamp.capstoneBackend.user.enums.Roles;
import com.fullstackbootcamp.capstoneBackend.user.repository.UserRepository;
import com.fullstackbootcamp.capstoneBackend.user.service.UserService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.MessagingException;
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
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final BusinessService businessService;
    private final LoanNotificationsService loanNotificationsService;
    private final EmailService emailService;
    private final FileService fileService;
    private final NotificationService notificationService;

    public LoanServiceImpl(LoanRequestRepository loanRequestRepository,
                           ChatRepository chatRepository,
                           UserService userService,
                           BusinessService businessService,
                           LoanResponseRepository loanResponseRepository,
                           LoanNotificationsService loanNotificationsService,
                           EmailService emailService,
                           FileService fileService,
                           NotificationService notificationService,
                           UserRepository userRepository) {
        this.loanRequestRepository = loanRequestRepository;
        this.userService = userService;
        this.chatRepository = chatRepository;
        this.businessService = businessService;
        this.loanResponseRepository = loanResponseRepository;
        this.loanNotificationsService = loanNotificationsService;
        this.emailService = emailService;
        this.fileService = fileService;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
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
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String civilId = userDetails.getCivilId();
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

        // Get business avatar to display in email
        Optional<FileEntity> file = fileService.getFile(loanRequestEntity.getBusiness().getBusinessAvatarFileId());

        for (Bank bank: request.getSelectedBanks()){
            if (bank==Bank.BOUBYAN_BANK){
                String targetEmail = "mohamed.baqer.banker@gmail.com";

                try {
                    emailService.sendVerificationEmail(targetEmail, "Mohamed", loanRequestEntity, file.get());
                } catch (MessagingException e) {
                    response.setStatus(CreateLoanRequestStatus.FAIL);
                    response.setMessage("Unable to send Verification email to the address provided. Please ensure it is entered correctly.");
                    return response;
                }
            }

        }

        // Send notification
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setType(NotificationType.NEW_LOAN_REQUEST);
        notificationEntity.setCreatedAt(LocalDateTime.now());

        // Set sender details (business owner)
        notificationEntity.setSenderName(user.get().getUsername());
        notificationEntity.setSenderFirstName(user.get().getFirstName());
        notificationEntity.setSenderRole(user.get().getRole());

        // Set business details
        String businessName = businessEntity.get().getBusinessNickname();
        notificationEntity.setBusinessName(businessName);

        // Set message content
        String messageContent = "New loan request: " + request.getLoanTitle() + " for " + request.getAmount();
        notificationEntity.setMessage(messageContent);

        // Send to each selected bank
        for (Bank bank : request.getSelectedBanks()) {
            Optional<UserEntity> banker = userRepository.findByBank(bank);
            if (banker.isEmpty()) {
                continue;
            }
            notificationEntity.setRecipient(banker.get());
            notificationEntity.setRecipientName(banker.get().getFirstName());
            notificationService.sendNotification(notificationEntity);
        }

        loanRequestRepository.save(loanRequestEntity);

        // if all is well, return success
        response.setStatus(CreateLoanRequestStatus.SUCCESS);
        response.setMessage("Loan Request is created.");
        return response;
    }

    @Override
    @Cacheable(value = "loanRequests", key = "#page + '_' + #status + '_' + #search + '_' + #limit" + " + '_' + #authentication")
    public Map<String, Object> getLoanRequestsPageable(int page, String status, String search, int limit, Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        // Validate token and ensure user is banker
        String message = validateToken(Roles.BANKER, authentication);
        if (message != null) {
            response.put("status", CreateLoanResponseStatus.FAIL);
            response.put("message", message);
            return response;
        }

        // Get civilId based on authentication type
        String civilId;
        if (authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            civilId = jwt.getClaims().get("civilId").toString();
        } else if (authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            civilId = userDetails.getCivilId();
        } else {
            response.put("status", CreateLoanResponseStatus.FAIL);
            response.put("message", "Invalid authentication type");
            return response;
        }

        // Ensure the user exists
        Optional<UserEntity> bankerUser = userService.getUserByCivilId(civilId);
        if (bankerUser.isEmpty()) {
            response.put("status", CreateLoanResponseStatus.FAIL);
            response.put("message", "User does not exist");
            return response;
        }

        Bank bank = bankerUser.get().getBank();
        Pageable pageable = PageRequest.of(page, limit, Sort.by("statusDate").descending());

        // Get loan requests based on status
        Page<LoanRequestEntity> loanRequestPage;
        if (status == null || status.isEmpty() || status.equalsIgnoreCase("all")) {
            loanRequestPage = loanRequestRepository.findRequestsByBank(bank, pageable);
        } else {
            try {
                switch (status.toUpperCase()) {
                    case "PENDING":
                        loanRequestPage = loanRequestRepository.findPendingRequestsByBank(bank, pageable);
                        break;
                    case "APPROVED":
                        loanRequestPage = loanRequestRepository.findApprovedRequestsByBank(bank, LoanResponseStatus.APPROVED, pageable);
                        break;
                    case "REJECTED":
                        loanRequestPage = loanRequestRepository.findRejectedRequestsByBank(bank, LoanResponseStatus.REJECTED, pageable);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid status: " + status);
                }
            } catch (IllegalArgumentException e) {
                response.put("status", CreateLoanResponseStatus.FAIL);
                response.put("message", e.getMessage());
                return response;
            }
        }

        // Build response
        List<Map<String, Object>> requestDetails = loanRequestPage.getContent().stream().map(request -> {
            Map<String, Object> details = new HashMap<>();
            details.put("id", request.getId());

            // Get loan response status for this bank
            Optional<LoanResponseStatus> loanResponseStatus = request.getLoanResponses().stream()
                    .filter(loanResponse -> loanResponse.getBank().equals(bank))
                    .map(LoanResponseEntity::getStatus)
                    .findFirst();

            boolean otherBanksHaveMadeACounterResponse = request.getLoanResponses().stream()
                    .filter(loanResponse -> !loanResponse.getBank().equals(bank))
                    .anyMatch(loanResponse -> loanResponse.getStatus() != null);

            details.put("otherBanksHaveMadeCounterResponse", otherBanksHaveMadeACounterResponse);
            details.put("loanRequestStatus", request.getStatus());
            details.put("loanResponseStatus", loanResponseStatus.orElse(null));
            details.put("businessName", request.getBusiness().getBusinessNickname());
            details.put("businessOwner", request.getBusiness().getBusinessOwnerUser().getFirstName() +
                    " " + request.getBusiness().getBusinessOwnerUser().getLastName());
            details.put("amount", request.getAmount());
            details.put("paymentPeriod", request.getLoanTerm());
            details.put("date", request.getStatusDate());

            return details;
        }).collect(Collectors.toList());

        response.put("requests", requestDetails);
        response.put("totalRecords", loanRequestPage.getTotalElements());
        response.put("status", CreateLoanResponseStatus.SUCCESS);

        return response;
    }

    @Override
    public List<GetLoanRequestsOfBusinessDTO> getLoanRequestsOfBusiness(Long businessId, Authentication authentication) {
        // Get bank of logged in banker
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Object civilId = userDetails.getCivilId();
        Optional<UserEntity> bankerUser = userService.getUserByCivilId(civilId.toString());
        if (bankerUser.isEmpty()) {
            throw new IllegalArgumentException("User does not exist");
        }

        Bank bank = bankerUser.get().getBank();

        // Get loan requests of business pageable
        Pageable pageable = PageRequest.of(0, 10, Sort.by("statusDate").descending());

        Page<LoanRequestEntity> loanRequestEntities = loanRequestRepository.findPendingRequestsByBankAndBusinessOwner(bank, businessId, pageable);

        return loanRequestEntities.getContent().stream()
                .map(loanRequest -> new GetLoanRequestsOfBusinessDTO(loanRequest, bank))
                .collect(Collectors.toList());
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
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String civilId = userDetails.getCivilId();
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


        // update the original loan request upon the new loan response
        // submitted by banker
        LoanRequestEntity updateLoanRequestEntity = loanRequest.get();

        // Ensure banker's previous responses are revoked first
        revokePreviousLoanResponses(updateLoanRequestEntity.getLoanResponses(), bankerUser.get());

        /* TODO (Important): check the status of the request is not "APPROVED"
         *  - It shouldn't be possible for a banker to submit a response after it
         *  - gets approved (the business owner already accepted an offer)
         */

        // first case: the banker approves the request offer straight away
        LoanResponseEntity loanResponseEntity = new LoanResponseEntity();
        loanResponseEntity.setBanker(bankerUser.get());
        loanResponseEntity.setAmount(request.getAmount());
        loanResponseEntity.setReason(request.getReason());
        loanResponseEntity.setLoanTerm(request.getLoanTerm());
        loanResponseEntity.setRepaymentPlan(request.getRepaymentPlan());
        loanResponseEntity.setStatus(request.getResponseStatus());
        loanResponseEntity.setStatusDate(LocalDateTime.now());
        loanResponseEntity.setBank(bankerUser.get().getBank());
        loanResponseEntity.setRejectionReason(request.getReason());

        // for notifications view track
        loanResponseEntity.setLoanResponseNotifications(new ArrayList<>());


        // Add the new loanResponse to the loanResponses list
        updateLoanRequestEntity.getLoanResponses().add(loanResponseEntity);
        updateLoanRequestEntity.setLoanResponses(updateLoanRequestEntity.getLoanResponses());

        /* Note:
         *  - NEW_RESPONSE means the user has received an offer that he is either going to accept or negotiate
         */
        updateLoanRequestEntity.setStatus(LoanRequestStatus.NEW_RESPONSE);
        updateLoanRequestEntity.setStatusDate(LocalDateTime.now());

        /* Note:
         *  - reset the arrayList to be new.
         *  - this way all notifications associated with this loanResponse are reset
         *  - this results in all users having to view the notifications
         */
        updateLoanRequestEntity.setLoanRequestNotifications(new ArrayList<>());

        /* REVIEW:
         *  - here, We may also reassign the requestAnalysis field as well and feed it back into
         *  - the AI endpoint with new information so that it acts as a
         *  - history analysis, encompassing all responses obtained so far
         */

        // Save both entities together once no error is encountered
        loanResponseRepository.save(loanResponseEntity);
        loanRequestRepository.save(updateLoanRequestEntity);

        // if all is well, return success
        response.setStatus(CreateLoanResponseStatus.SUCCESS);
        response.setMessage("Loan Response is successfully sent to Business owner.");
        return response;


    }

    public GetLoanRequestDTO getLoanRequestById(Long id, Authentication authentication) {
        GetLoanRequestDTO response = new GetLoanRequestDTO();

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // ensure the loan request exists using the id
        Optional<LoanRequestEntity> loanRequest = getLoanRequestById(id);

        if (loanRequest.isEmpty()) {
            response.setStatus(LoanRequestRetrievalStatus.FAIL);
            response.setMessage("No loan request is associated with id provided");
            return response; // If there was an error during validation, return early
        }

        // Ensure the user exists
        String civilId = userDetails.getCivilId();
        Optional<UserEntity> bankerUser = userService.getUserByCivilId(civilId.toString());
        if (bankerUser.isEmpty()) {
            return response;
        }

        Bank bank = bankerUser.get().getBank();

        // Get loan response status for this bank
        Optional<LoanResponseStatus> loanResponseStatus = loanRequest.get().getLoanResponses().stream().filter(loanResponse -> loanResponse.getBank() == bank).map(LoanResponseEntity::getStatus).findFirst();

        // Get rejection reason for this bank
        Optional<String> rejectionReason = loanRequest.get().getLoanResponses().stream().filter(loanResponse -> loanResponse.getBank() == bank).map(LoanResponseEntity::getRejectionReason).findFirst();

        response.setRejectionReason(rejectionReason.orElse(null));

        response.setResponseStatus(loanResponseStatus.orElse(null));
        response.setStatus(LoanRequestRetrievalStatus.SUCCESS);
        response.setMessage("Loan Request entity is successfully retrieved");
        response.setEntity(loanRequest.get());

        // Get the business owner from the loan request
        UserEntity businessOwner = loanRequest.get().getBusiness().getBusinessOwnerUser();

        // Find existing chats for both the banker and business owner
        List<ChatEntity> bankerChats = chatRepository.findByBankerId(bankerUser.get().getId());
        List<ChatEntity> businessOwnerChats = chatRepository.findByBusinessOwnerId(businessOwner.getId());

        // Find intersection of chats (the chat between these two users if it exists)
        Optional<ChatEntity> existingChat = bankerChats.stream()
                .filter(businessOwnerChats::contains)
                .findFirst();

        response.setChatId(existingChat.map(ChatEntity::getId).orElse(null));

        return response;
    }


    public GetAllLoanRequestsDTO getAllLoanRequestsForBusinessOwner(Authentication authentication) {
        GetAllLoanRequestsDTO response = new GetAllLoanRequestsDTO();

        // ensure it's business owner token provided
        String message = validateToken(Roles.BUSINESS_OWNER, authentication);

        // return a response if a message is returned
        if (message != null) {
            response.setStatus(LoanRequestRetrievalStatus.FAIL);
            response.setMessage(message);
            return response;
        }

        // ensure the user in the token exists
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String civilId = userDetails.getCivilId();
        Optional<UserEntity> businessOwner = userService.getUserByCivilId(civilId.toString());

        // if user is not found in repository
        /* TODO: Instead of Fail, another enum value could be added so that it gets routed to an
            explicit case in the controller layer to utilize 'HttpStatus.NOT_FOUND' which is more
             appropriate than 'Bad_Request' when user is not found.
         */
        if (businessOwner.isEmpty()) {
            response.setStatus(LoanRequestRetrievalStatus.FAIL);
            response.setMessage("user does not exist");
            return response;
        }

        // check business exists with user entity obtained
        Optional<BusinessEntity> businessEntity = businessService.getBusinessOwnerEntity(businessOwner.get());

        if (businessEntity.isEmpty()) {
            response.setStatus(LoanRequestRetrievalStatus.FAIL);
            response.setMessage("No business is associated with this user");
            return response; // If there was an error during validation, return early
        }

        // retrieve all loan requests associated with business entity (only those that are not withdrawn are returned)
        Optional<List<LoanRequestEntity>> allLoanRequests = getAllLoanRequestByStatus(businessEntity.get(), LoanRequestStatus.WITHDRAWN);

        response.setStatus(LoanRequestRetrievalStatus.SUCCESS);
        response.setMessage("Successfully retrieved all requests for business owner.");
        response.setAllRequests(allLoanRequests.get());
        return response;
    }

    // NOTE: For business owner
    public OfferResponseDTO acceptOffer(Long loanRequestId, Long loanResponseId, Authentication authentication) {
        OfferResponseDTO response = new OfferResponseDTO();

        // ensure it's business owner token provided
        String message = validateToken(Roles.BUSINESS_OWNER, authentication);

        // return a response if a message is returned
        if (message != null) {
            response.setStatus(OfferSubmissionStatus.FAIL);
            response.setMessage(message);
            return response;
        }

        // NOTE: ensure the loan request exists
        Optional<LoanRequestEntity> loanRequest = loanRequestRepository.findById(loanRequestId);

        if (loanRequest.isEmpty()) {
            response.setStatus(OfferSubmissionStatus.FAIL);
            response.setMessage("No loan Request exist with tha provided ID");
            return response;
        }

        // NOTE: ensure the loan response exists
        Optional<LoanResponseEntity> loanResponse = loanResponseRepository.findById(loanResponseId);

        if (loanResponse.isEmpty()) {
            response.setStatus(OfferSubmissionStatus.FAIL);
            response.setMessage("No loan response exist with tha provided ID");
            return response;
        }

        // REVIEW: we might also need to check 'rejectionSource' and "Status" to ensure it is not rejected yet

        List<LoanResponseEntity> allResponses = loanRequest.get().getLoanResponses();

        for (LoanResponseEntity responseLoop : allResponses) {
            // Set the time of change for all responses
            responseLoop.setStatusDate(LocalDateTime.now());

            if (responseLoop.getId().equals(loanResponseId)) {
                // Keep only the selected response as approved
                responseLoop.setStatus(LoanResponseStatus.APPROVED);
            } else {
                if (responseLoop.getStatus() == LoanResponseStatus.APPROVED ||
                        responseLoop.getStatus() == LoanResponseStatus.COUNTER_OFFER) {
                    // Change others to rejected
                    responseLoop.setStatus(LoanResponseStatus.REJECTED);
                    responseLoop.setReason("The business owner has accepted a different offer");
                }
            }
        }

        LoanRequestEntity loanRequestUpdated = loanRequest.get();

        loanRequestUpdated.setLoanResponses(allResponses);
        loanRequestUpdated.setAmount(loanResponse.get().getAmount());
        loanRequestUpdated.setLoanTerm(loanResponse.get().getLoanTerm());
        loanRequestUpdated.setRepaymentPlan(loanResponse.get().getRepaymentPlan());
        loanRequestUpdated.setStatus(LoanRequestStatus.APPROVED);

        loanRequestRepository.save(loanRequestUpdated);

        response.setStatus(OfferSubmissionStatus.SUCCESS);
        response.setMessage("Successfully accepted bank's offer.");
        return response;

    }

    // NOTE: For business owner
    public OfferResponseDTO withdrawOffer(Long loanRequestId, Authentication authentication) {
        OfferResponseDTO response = new OfferResponseDTO();

        // ensure it's business owner token provided
        String message = validateToken(Roles.BUSINESS_OWNER, authentication);

        // return a response if a message is returned
        if (message != null) {
            response.setStatus(OfferSubmissionStatus.FAIL);
            response.setMessage(message);
            return response;
        }

        // NOTE: ensure the loan request exists
        Optional<LoanRequestEntity> loanRequest = loanRequestRepository.findById(loanRequestId);

        if (loanRequest.isEmpty()) {
            response.setStatus(OfferSubmissionStatus.FAIL);
            response.setMessage("No loan Request exist with tha provided ID");
            return response;
        }

        loanRequest.get().setStatus(LoanRequestStatus.WITHDRAWN);
        loanRequestRepository.save(loanRequest.get());

        response.setStatus(OfferSubmissionStatus.SUCCESS);
        response.setMessage("Successfully withdrew loan Request.");
        return response;
    }

    // NOTE: for business owners rejecting loan offers
    public OfferResponseDTO rejectOffer(CreateOfferResponse request, Authentication authentication) {
        OfferResponseDTO response = new OfferResponseDTO();

        // ensure it's business owner token provided
        String message = validateToken(Roles.BUSINESS_OWNER, authentication);

        // return a response if a message is returned
        if (message != null) {
            response.setStatus(OfferSubmissionStatus.FAIL);
            response.setMessage(message);
            return response;
        }

        // NOTE: ensure the loan request exists
        Optional<LoanRequestEntity> loanRequest = loanRequestRepository.findById(request.getLoanRequestId());

        if (loanRequest.isEmpty()) {
            response.setStatus(OfferSubmissionStatus.FAIL);
            response.setMessage("No loan Request exist with tha provided ID");
            return response;
        }

        // NOTE: ensure the loan response exists
        Optional<LoanResponseEntity> loanResponse = loanResponseRepository.findById(request.getLoanResponseId());

        if (loanResponse.isEmpty()) {
            response.setStatus(OfferSubmissionStatus.FAIL);
            response.setMessage("No loan response exist with tha provided ID");
            return response;
        }

        // REVIEW: we might also need to check 'rejectionSource' and "Status" to ensure it is not rejected yet

        // TODO: give business owner option to state reason for rejection, instead of template
        loanResponse.get().setReason("Loan Offer rejected by Business Owner");
        loanResponse.get().setStatus(LoanResponseStatus.REJECTED);
        loanResponse.get().setRejectionSource(RejectionSource.BUSINESS_OWNER);

        // if there are no pending loan responses (APPROVED or COUNTER_OFFER), change loan request back to pending
        // instead of New response
        boolean hasValidResponse = loanRequest.get().getLoanResponses().stream()
                .anyMatch(loanResponseEntity -> loanResponseEntity.getStatus() == LoanResponseStatus.APPROVED
                        || loanResponseEntity.getStatus() == LoanResponseStatus.COUNTER_OFFER);

        if (!hasValidResponse) {
            loanRequest.get().setStatus(LoanRequestStatus.PENDING);
        }
        loanResponseRepository.save(loanResponse.get());
        loanRequestRepository.save(loanRequest.get());

        response.setStatus(OfferSubmissionStatus.SUCCESS);
        response.setMessage("Successfully rejected bank's offer.");
        return response;
    }

    public CheckNotificationDTO viewRequest(Long id, Authentication authentication) {
        CheckNotificationDTO response = new CheckNotificationDTO();

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String civilId = userDetails.getCivilId();
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


        LoanNotificationEntity notification = new LoanNotificationEntity();
        notification.setUser(user.get());

        System.out.println(user.get().getCivilId());
        System.out.println(user.get().getFirstName());


        LoanNotificationEntity newNotification = loanNotificationsService.saveNotificationEntity(notification);

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
        if (!(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return "Invalid authentication type";
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // Instead of checking jwt claims, check the user's roles
        if (!userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(role.name()))) {
            return "Not allowed for provided role. This endpoint is only for " + role.name();
        }

        return null;
    }

    public Optional<LoanRequestEntity> getLoanRequestById(Long id) {
        return loanRequestRepository.findById(id);
    }

    public Optional<LoanResponseEntity> getLoanResponseById(Long id) {
        return loanResponseRepository.findById(id);
    }

    public Optional<List<LoanRequestEntity>> getAllLoanRequest(BusinessEntity business) {
        return loanRequestRepository.findByBusiness(business);
    }

    public Optional<List<LoanRequestEntity>> getAllLoanRequestByStatus(BusinessEntity business, LoanRequestStatus status) {
        return loanRequestRepository.findByBusinessAndStatusNot(business, status);
    }

    public void revokePreviousLoanResponses(List<LoanResponseEntity> loanResponseEntities, UserEntity user) {
        // Loop through all loan responses and update those with the specified user
        for (LoanResponseEntity response : loanResponseEntities) {
            if (response.getBanker().equals(user) &&
                    (response.getStatus() == LoanResponseStatus.APPROVED ||
                            response.getStatus() == LoanResponseStatus.COUNTER_OFFER)) {
                response.setStatus(LoanResponseStatus.RESCINDED); // Set status to RESCINDED
                response.setStatusDate(LocalDateTime.now()); // Set the current date/time
            }
        }

        // Persist changes to the database
        loanResponseRepository.saveAll(loanResponseEntities); // Save all updated responses
    }


}
