package com.fullstackbootcamp.capstoneBackend.auth.controller;

import com.fullstackbootcamp.capstoneBackend.auth.dto.RefreshTokenRequestDTO;
import com.fullstackbootcamp.capstoneBackend.auth.dto.TokenResponseDTO;
import com.fullstackbootcamp.capstoneBackend.auth.enums.TokenServiceStatus;
import com.fullstackbootcamp.capstoneBackend.auth.service.AuthService;
import com.fullstackbootcamp.capstoneBackend.auth.util.JwtUtil;
import com.fullstackbootcamp.capstoneBackend.user.bo.CreateUserRequest;
import com.fullstackbootcamp.capstoneBackend.user.bo.LoginRequest;
import com.fullstackbootcamp.capstoneBackend.auth.dto.SignupResponseDTO;
import com.fullstackbootcamp.capstoneBackend.user.enums.CreateUserStatus;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/auth/v1")
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

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponseDTO> refreshAccessToken(@Valid @RequestBody RefreshTokenRequestDTO request, BindingResult bindingResult) {
        // ensures no field is missing and typos of field name. the field missing is returned before service layer
        if (bindingResult.hasErrors()) {

            // store list of all fields missing as strings
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .toList();

            // Return signup response
            TokenResponseDTO noResponse = new TokenResponseDTO();
            noResponse.setStatus(TokenServiceStatus.FAILURE); // set fail status
            noResponse.setMessage(errorMessages.getFirst()); // point to first missing field found
            return ResponseEntity.badRequest().body(noResponse);// bad request returned since field is missing
        }

        try {
            TokenResponseDTO response = authService.refreshAccessToken(request.getRefreshToken());
            switch (response.getStatus()) {
                case SUCCESS: // accepted status returned for successfully refreshing access token
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

                case TOKEN_INVALID: // unauthorized is returned when access token is provided instead of refresh token
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);


                case UNAUTHORIZED: // conflict status returned when username/civilId already exists
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

                case INVALID_REQUEST: // bad request status returned when field is not in correct format
                    return ResponseEntity.badRequest().body(response);

                default: // default error
                    response.setStatus(TokenServiceStatus.FAILURE);
                    response.setMessage("Failed in refreshing token");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

        } catch (Exception e) {
            TokenResponseDTO response = new TokenResponseDTO();
            response.setStatus(TokenServiceStatus.FAILURE);
            response.setMessage("Unknown Error encountered when refreshing the access Token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Delegate to AuthService
            TokenResponseDTO response = authService.login(loginRequest);

            switch (response.getStatus()) {
                case SUCCESS: // accepted status returned for successfully logging in
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

                case UNAUTHORIZED: // unauthorized status returned when user does not exist in database
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

                case INVALID_REQUEST: // bad request status returned when provided password does not match the password in database
                    return ResponseEntity.badRequest().body(response);

                default: // default error
                    response.setStatus(TokenServiceStatus.FAILURE);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

}
