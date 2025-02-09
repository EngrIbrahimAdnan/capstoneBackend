package com.fullstackbootcamp.capstoneBackend.user.service;

import com.fullstackbootcamp.capstoneBackend.user.bo.CreateUserRequest;
import com.fullstackbootcamp.capstoneBackend.auth.dto.SignupResponseDTO;
import com.fullstackbootcamp.capstoneBackend.user.dto.DashboardResponse;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import com.fullstackbootcamp.capstoneBackend.user.enums.CreateUserStatus;
import com.fullstackbootcamp.capstoneBackend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    // TODO
    @Override
    public DashboardResponse getDashboardData(String token) {
        return null;
    }

    // TODO
    // pendingReview format:
    // { "pending": 0, "dinarsInReview": 0 }
    @Override
    public Map<String, Object> getPendingReview(UserEntity user) {
        return null;
    }

    // TODO
    @Override
    public Map<String, Object> getNotifications(UserEntity user) {
        return null;
    }

    // TODO
    @Override
    public Map<String, Object> getFiveMostRecentRequests(UserEntity user) {
        return null;
    }

    // TODO
    @Override
    public Map<String, Object> getFourMostRecentChats(UserEntity user) {
        return null;
    }

    // TODO
    @Override
    public Map<String, Object> getRecentHistory(UserEntity user) {
        return null;
    }
}
