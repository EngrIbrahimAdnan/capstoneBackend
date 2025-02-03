package com.fullstackbootcamp.capstoneBackend.loan.controller;

import com.fullstackbootcamp.capstoneBackend.auth.dto.SignupResponseDTO;
import com.fullstackbootcamp.capstoneBackend.business.dto.AddBusinessDTO;
import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessAdditionStatus;
import com.fullstackbootcamp.capstoneBackend.loan.bo.CreateLoanRequest;
import com.fullstackbootcamp.capstoneBackend.loan.dto.LoanRequestDTO;
import com.fullstackbootcamp.capstoneBackend.loan.enums.CreateLoanRequestStatus;
import com.fullstackbootcamp.capstoneBackend.loan.service.LoanService;
import com.fullstackbootcamp.capstoneBackend.user.bo.CreateUserRequest;
import com.fullstackbootcamp.capstoneBackend.user.enums.CreateUserStatus;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/loan")
@RestController
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }


    @PostMapping("/request")
    public ResponseEntity<LoanRequestDTO> requestLoan(@Valid @RequestBody CreateLoanRequest request,
                                                      BindingResult bindingResult,
                                                      Authentication authentication) {

        // ensures no field is missing and typos of field name. the field missing is returned before service layer
        if (bindingResult.hasErrors()) {

            // store list of all fields missing as strings
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .toList();

            // Return signup response
            LoanRequestDTO noResponse = new LoanRequestDTO();
            noResponse.setStatus(CreateLoanRequestStatus.FAIL); // set fail status
            noResponse.setMessage(errorMessages.getFirst()); // point to first missing field found
            return ResponseEntity.badRequest().body(noResponse);// bad request returned since field is missing
        }

        LoanRequestDTO response = loanService.createLoanRequest(request, authentication);

        switch (response.getStatus()) {
            case SUCCESS: // accepted status returned for successfully creating loan request
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

            case FAIL: // bad request status returned when field is not in correct format
                return ResponseEntity.badRequest().body(response);

            default: // default error
                LoanRequestDTO noResponse = new LoanRequestDTO();
                noResponse.setStatus(CreateLoanRequestStatus.FAIL);
                noResponse.setMessage("Error status unrecognized");
                return ResponseEntity.badRequest().body(noResponse);
        }
    }
}
