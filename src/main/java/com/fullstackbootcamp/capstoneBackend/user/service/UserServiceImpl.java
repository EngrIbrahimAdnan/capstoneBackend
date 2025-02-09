package com.fullstackbootcamp.capstoneBackend.user.service;

import com.fullstackbootcamp.capstoneBackend.auth.util.JwtUtil;
import com.fullstackbootcamp.capstoneBackend.chat.dto.ChatDTO;
import com.fullstackbootcamp.capstoneBackend.chat.dto.ChatPreviewDTO;
import com.fullstackbootcamp.capstoneBackend.chat.entity.ChatEntity;
import com.fullstackbootcamp.capstoneBackend.chat.repository.ChatRepository;
import com.fullstackbootcamp.capstoneBackend.chat.service.ChatMapperService;
import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanRequestEntity;
import com.fullstackbootcamp.capstoneBackend.loan.repository.LoanRequestRepository;
import com.fullstackbootcamp.capstoneBackend.user.bo.CreateUserRequest;
import com.fullstackbootcamp.capstoneBackend.auth.dto.SignupResponseDTO;
import com.fullstackbootcamp.capstoneBackend.user.dto.DashboardResponse;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import com.fullstackbootcamp.capstoneBackend.user.enums.Bank;
import com.fullstackbootcamp.capstoneBackend.user.enums.CreateUserStatus;
import com.fullstackbootcamp.capstoneBackend.user.repository.UserRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final LoanRequestRepository loanRequestRepository;
    private final ChatRepository chatRepository;
    private final JwtUtil jwtUtil;
    private final ChatMapperService chatMapperService;

    public UserServiceImpl(UserRepository userRepository,
                           LoanRequestRepository loanRequestRepository,
                           ChatRepository chatRepository,
                           JwtUtil jwtUtil,
                           ChatMapperService chatMapperService) {
        this.userRepository = userRepository;
        this.loanRequestRepository = loanRequestRepository;
        this.chatRepository = chatRepository;
        this.jwtUtil = jwtUtil;
        this.chatMapperService = chatMapperService;
    }

    @Override
    public SignupResponseDTO createUser(CreateUserRequest request) {

        // Check if either username or civil id is already registered with
        boolean civilIdPresent = getUserByCivilId(request.getCivilId()).isPresent();
        boolean usernamePresent = getUserByUsername(request.getUsername()).isPresent();

        // if either one is true, a conflict status is returned since user already exists
        if (civilIdPresent || usernamePresent) {
            SignupResponseDTO response = new SignupResponseDTO();
            response.setStatus(CreateUserStatus.USER_ALREADY_EXISTS);
            response.setMessage("Username/civil Id is already registered with");
            return response;
        }

        else {
            UserEntity user = new UserEntity();
            user.setFirstName(request.getFirstName().toLowerCase()); // ensure its lower case
            user.setLastName(request.getLastName().toLowerCase()); // ensure its lower case
            user.setUsername(request.getUsername().toLowerCase()); // ensure its lower case
            user.setPassword(request.getPassword());
            user.setCivilId(request.getCivilId());
            user.setMobileNumber(request.getMobileNumber());
            user.setRole(request.getRole());
            user.setBank(request.getBank());


            userRepository.save(user);

            SignupResponseDTO response = new SignupResponseDTO();
            response.setStatus(CreateUserStatus.SUCCESS);
            response.setMessage("User is successfully registered");
            return response;
        }
    }

    @Override
    public Optional<UserEntity> getUserByCivilId(String civilId) {
        return userRepository.findByCivilId(civilId);
    }

    @Override
    public Optional<UserEntity> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // For bankers
    // TODO
    @Override
    public DashboardResponse getDashboardData(String token) {
        // Get user from token
        String username = jwtUtil.extractUserUsernameFromToken(token);
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        DashboardResponse response = new DashboardResponse();
        // Get and set notifications
        response.setNotifications(null);
        // Get and set pending review
        response.setPendingReview(getPendingReview(user.getBank()));
        // Get and set five most recent requests
        response.setFiveMostRecentRequests(getFiveMostRecentRequests(getRequestsMadeToBank(user.getBank())));
        // Get and set four most recent chats
        response.setFourMostRecentChats(getFourMostRecentChats(user));
        // Get and set recent history
        response.setRecentHistory(getRecentHistory(user.getBank()));
        return response;
    }

    public List<LoanRequestEntity> getRequestsMadeToBank(Bank bank) {
        return loanRequestRepository.findBySelectedBank(bank);
    }

    public Map<String, BigDecimal> getPendingReview(Bank bank) {
        List<LoanRequestEntity> loanRequests = loanRequestRepository.findBySelectedBankAndStatusPending(bank);

        Map<String, Object> fiveMostRecentRequests = getFiveMostRecentRequests(loanRequests);

        Map<String, BigDecimal> pendingReview = new HashMap<>();
        pendingReview.put("pending", BigDecimal.valueOf(loanRequests.size()));
        pendingReview.put("dinarsInReview", loanRequests.stream()
                .map(LoanRequestEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        return pendingReview;
    }

    // TODO
    public Map<String, Object> getNotifications(UserEntity user) {
        return Collections.emptyMap();
    }

    public Map<String, Object> getFiveMostRecentRequests(List<LoanRequestEntity> pendingLoanRequests) {
        Map<String, Object> recentRequests = new HashMap<>();

        List<Map<String, Object>> requestDetails = pendingLoanRequests.stream()
                .sorted(Comparator.comparing(LoanRequestEntity::getStatusDate).reversed())
                .limit(5)
                .map(request -> {
                    Map<String, Object> details = new HashMap<>();
                    details.put("id", request.getId());
                    details.put("businessName", request.getBusiness().getBusinessNickname());
                    details.put("amount", request.getAmount());
                    details.put("title", request.getLoanTitle());
                    details.put("date", request.getStatusDate());
                    return details;
                })
                .collect(Collectors.toList());

        recentRequests.put("requests", requestDetails);
        return recentRequests;
    }

    public List<ChatPreviewDTO> getFourMostRecentChats(UserEntity user) {
        List<ChatEntity> chats = chatRepository.findRecentChatsByUserId(user.getId(), PageRequest.of(0, 4));
        return chatMapperService.convertToPreviews(chats, user.getUsername());
    }

    public List<Map<String, Object>> getRecentHistory(Bank bank) {
        List< LoanRequestEntity> loanRequests = loanRequestRepository.findBySelectedBankAndStatusNotPending(bank);

        return loanRequests.stream()
                .sorted(Comparator.comparing(LoanRequestEntity::getStatusDate).reversed())
                .limit(5)
                .map(request -> {
                    Map<String, Object> details = new HashMap<>();
                    details.put("id", request.getId());
                    details.put("businessName", request.getBusiness().getBusinessNickname());
                    details.put("amount", request.getAmount());
                    details.put("title", request.getLoanTitle());
                    details.put("date", request.getStatusDate());
                    details.put("status", request.getStatus());
                    return details;
                })
                .collect(Collectors.toList());
    }
}
