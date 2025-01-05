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
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/v1/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDTO> signupUser(@Valid @RequestBody CreateUserRequest request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.toList());

            SignupResponseDTO noResponse = new SignupResponseDTO();
            noResponse.setStatus(CreateUserStatus.FAIL);
            noResponse.setMessage(errorMessages.get(0));

            return ResponseEntity.badRequest().body(noResponse);
        }

        SignupResponseDTO response = authService.processSignupRequest(request);

        switch (response.getStatus()) {
            case SUCCESS:
                return ResponseEntity.ok(response);
            case FAIL:
                return ResponseEntity.badRequest().body(response);
            default:
                SignupResponseDTO noResponse = new SignupResponseDTO();
                noResponse.setStatus(CreateUserStatus.FAIL);
                noResponse.setMessage("Error status unrecognized");
                return ResponseEntity.badRequest().body(noResponse);
        }
    }

    @GetMapping("/login")
    public String loginUser() {
        return "User has signed up";
    }

}
