package com.fullstackbootcamp.capstoneBackend.user.service;

import com.fullstackbootcamp.capstoneBackend.user.bo.CreateUserRequest;
import com.fullstackbootcamp.capstoneBackend.user.dto.SignupResponseDTO;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import com.fullstackbootcamp.capstoneBackend.user.enums.CreateUserStatus;
import com.fullstackbootcamp.capstoneBackend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

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
            response.setMessage("Username/civil Id is already registered with");
            response.setStatus(CreateUserStatus.USER_ALREADY_EXISTS);
            return response;
        }

        else {
            UserEntity user = new UserEntity();
            user.setFirstName(request.getFirstName().toLowerCase()); // ensure its lower case
            user.setLastName(request.getLastName().toLowerCase()); // ensure its lower case
            user.setUsername(request.getUsername().toLowerCase()); // ensure its lower case
            user.setPassword(request.getPassword()); // TODO: encode later
            user.setCivilId(request.getCivilId());
            user.setMobileNumber(request.getMobileNumber());
            user.setRole(request.getRole());

            userRepository.save(user);

            SignupResponseDTO response = new SignupResponseDTO();
            response.setMessage("User is successfully registered");
            response.setStatus(CreateUserStatus.SUCCESS);
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
}
