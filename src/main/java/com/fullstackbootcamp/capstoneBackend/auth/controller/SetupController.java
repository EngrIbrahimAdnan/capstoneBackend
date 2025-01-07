package com.fullstackbootcamp.capstoneBackend.auth.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fullstackbootcamp.capstoneBackend.auth.dto.LoadUsersResponseDTO;
import com.fullstackbootcamp.capstoneBackend.auth.service.AuthService;
import com.fullstackbootcamp.capstoneBackend.user.bo.CreateUserRequest;
import com.fullstackbootcamp.capstoneBackend.user.dto.SignupResponseDTO;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import com.fullstackbootcamp.capstoneBackend.user.enums.CreateUserStatus;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/v1/setup")
@RestController
public class SetupController {

    private final AuthService authService;

    public SetupController(AuthService authService) {
        this.authService = authService;
    }


    // Loads users from "users.json" in "predefinedData" folder under resources
    @PostMapping("/load-users")
    public ResponseEntity<LoadUsersResponseDTO> signupUser() {

        // Pass filename and list of the object required
        LoadUsersResponseDTO response = authService.loadEntites("users.json",
                new TypeReference<List<CreateUserRequest>>() {
                });


        switch (response.getStatus()) {
            case SUCCESS: // accepted status returned for user creation
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

            case FAIL: // bad request status returned when field is not in correct format
                return ResponseEntity.badRequest().body(response);

            case USER_ALREADY_EXISTS: // conflict status returned when username/civilId already exists
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

            default: // default error
                LoadUsersResponseDTO noResponse = new LoadUsersResponseDTO();
                noResponse.setStatus(CreateUserStatus.FAIL);
                noResponse.setMessage("Error status unrecognized");
                return ResponseEntity.badRequest().body(noResponse);
        }
    }
}
