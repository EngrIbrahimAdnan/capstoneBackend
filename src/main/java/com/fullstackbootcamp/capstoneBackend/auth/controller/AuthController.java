package com.fullstackbootcamp.capstoneBackend.auth.controller;

import com.fullstackbootcamp.capstoneBackend.auth.service.AuthService;
import com.fullstackbootcamp.capstoneBackend.user.bo.CreateUserRequest;
import com.fullstackbootcamp.capstoneBackend.user.dto.SignupResponseDTO;
import com.fullstackbootcamp.capstoneBackend.user.enums.CreateUserStatus;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/v1/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // signup for user
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDTO> signupUser(@Valid @RequestBody CreateUserRequest request, BindingResult bindingResult) {

        // ensures no field is missing and typos of field name. the field missing is returned before service layer
        if (bindingResult.hasErrors()) {

            // store list of all fields missing as strings
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .toList();

            // Return signup response
            SignupResponseDTO noResponse = new SignupResponseDTO();
            noResponse.setStatus(CreateUserStatus.FAIL); // set fail status
            noResponse.setMessage(errorMessages.getFirst()); // point to first missing field found
            return ResponseEntity.badRequest().body(noResponse);// bad request returned since field is missing
        }

        SignupResponseDTO response = authService.processSignupRequest(request);

        switch (response.getStatus()) {
            case SUCCESS: // accepted status returned for user creation
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

            case FAIL: // bad request status returned when field is not in correct format
                return ResponseEntity.badRequest().body(response);

            case USER_ALREADY_EXISTS: // conflict status returned when username/civilId already exists
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

            default: // default error
                SignupResponseDTO noResponse = new SignupResponseDTO();
                noResponse.setStatus(CreateUserStatus.FAIL);
                noResponse.setMessage("Error status unrecognized");
                return ResponseEntity.badRequest().body(noResponse);
        }
    }

    @GetMapping("/login")
    public String loginUser() {
        return "User has logged in up";
    }

}
