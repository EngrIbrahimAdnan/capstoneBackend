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

        // Ensure civil id and phone number contains only numbers and no letters
        if (containsLetters(request.getCivilId()) || containsLetters(request.getMobileNumber())){
            SignupResponseDTO response = new SignupResponseDTO();
            response.setMessage("Civil id and mobile number must only contain numbers.");
            response.setStatus(CreateUserStatus.FAIL);
            return response;
        }

        // Check if either username or civil id is already registered with
        boolean civilIdPresent = getUserByCivilId(request.getCivilId()).isPresent();
        boolean usernamePresent = getUserByUsername(request.getUsername()).isPresent();

        // if either one is true, a return response is returned
        if (civilIdPresent || usernamePresent) {
            SignupResponseDTO response = new SignupResponseDTO();
            response.setMessage("Username/password is already registered with");
            response.setStatus(CreateUserStatus.FAIL);
            return response;
        }

        else {
            UserEntity user = new UserEntity();
            user.setFirstName(request.getFirstName().toLowerCase()); // ensure first name is case
            user.setLastName(request.getLastName().toLowerCase());
            user.setUsername(request.getUsername().toLowerCase());
            user.setCivilId(request.getCivilId());
            user.setMobileNumber(request.getMobileNumber());
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

    public boolean containsLetters(String str) {
        // Check if the string contains any alphabetic characters
        return str != null && str.matches(".*[a-zA-Z].*");
    }

}
