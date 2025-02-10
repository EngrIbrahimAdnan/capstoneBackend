package com.fullstackbootcamp.capstoneBackend.loan.controller;

import com.fullstackbootcamp.capstoneBackend.loan.bo.CreateLoanRequest;
import com.fullstackbootcamp.capstoneBackend.loan.bo.CreateLoanResponse;
import com.fullstackbootcamp.capstoneBackend.loan.dto.*;
import com.fullstackbootcamp.capstoneBackend.loan.entity.LoanRequestEntity;
import com.fullstackbootcamp.capstoneBackend.loan.enums.CheckNotificationStatus;
import com.fullstackbootcamp.capstoneBackend.loan.enums.CreateLoanRequestStatus;
import com.fullstackbootcamp.capstoneBackend.loan.enums.CreateLoanResponseStatus;
import com.fullstackbootcamp.capstoneBackend.loan.enums.LoanRequestRetrievalStatus;
import com.fullstackbootcamp.capstoneBackend.loan.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/loan")
@RestController
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping("/bank/history")
    public ResponseEntity<Map<String, Object>> getLoanRequestsPageable(
            Authentication authentication,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "status", defaultValue = "") String status,
            @RequestParam(value = "search", defaultValue = "") String search,
            @RequestParam(value = "limit", defaultValue = "8") int limit) {
        try {
            Map<String, Object> loans = loanService.getLoanRequestsPageable(page, status, search, limit, authentication);
            return ResponseEntity.ok(loans);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    // endpoint for business owners to request loan offers from bankers
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

    // endpoint for bankers to response to loan
    @PostMapping("/response")
    public ResponseEntity<LoanResponseDTO> counterLoan(@Valid @RequestBody CreateLoanResponse request,
                                                       BindingResult bindingResult,
                                                       Authentication authentication) {

        // ensures no field is missing and typos of field name. the field missing is returned before service layer
        if (bindingResult.hasErrors()) {

            // store list of all fields missing as strings
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .toList();

            // Return signup response
            LoanResponseDTO noResponse = new LoanResponseDTO();
            noResponse.setStatus(CreateLoanResponseStatus.FAIL); // set fail status
            noResponse.setMessage(errorMessages.getFirst()); // point to first missing field found
            return ResponseEntity.badRequest().body(noResponse);// bad request returned since field is missing
        }

        LoanResponseDTO response = loanService.createLoanResponse(request, authentication);

        switch (response.getStatus()) {
            case SUCCESS: // accepted status returned for successfully creating loan request
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

            case FAIL: // bad request status returned when field is not in correct format
                return ResponseEntity.badRequest().body(response);

            default: // default error
                LoanResponseDTO noResponse = new LoanResponseDTO();
                noResponse.setStatus(CreateLoanResponseStatus.FAIL);
                noResponse.setMessage("Error status unrecognized");
                return ResponseEntity.badRequest().body(noResponse);
        }
    }

    @PostMapping("/request/notifications/{id}/viewed")
    public ResponseEntity<CheckNotificationDTO> requestView(@PathVariable Long id,
                                                            Authentication authentication) {

        CheckNotificationDTO response = loanService.viewRequest(id, authentication);

        switch (response.getStatus()) {
            case SUCCESS: // accepted status returned for successfully creating loan request
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

            case FAIL: // bad request status returned when field is not in correct format
                return ResponseEntity.badRequest().body(response);

            default: // default error
                CheckNotificationDTO noResponse = new CheckNotificationDTO();
                noResponse.setStatus(CheckNotificationStatus.FAIL);
                noResponse.setMessage("Error status unrecognized");
                return ResponseEntity.badRequest().body(noResponse);
        }
    }

    @GetMapping("/request/{id}")
    public ResponseEntity<GetLoanRequestDTO> getrequestLoan(@PathVariable Long id,
                                                            Authentication authentication) {

        GetLoanRequestDTO response = loanService.getLoanRequestById(id, authentication);

        switch (response.getStatus()) {
            case SUCCESS: // accepted status returned for successfully creating loan request
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

            case FAIL: // bad request status returned when field is not in correct format
                return ResponseEntity.badRequest().body(response);

            default: // default error
                GetLoanRequestDTO noResponse = new GetLoanRequestDTO();
                noResponse.setStatus(LoanRequestRetrievalStatus.FAIL);
                noResponse.setMessage("Error status unrecognized");
                return ResponseEntity.badRequest().body(noResponse);
        }
    }


    /* NOTE:
     *  - this endpoint is for business owner getting all loan requests for their busienss
     *  - of course, this assumes each business owner has only one business
     *  - you don't pass anything aside from token
     */
    @GetMapping("/request/all")
    public ResponseEntity<GetAllLoanRequestsDTO> getAllRequestLoan(Authentication authentication) {

        GetAllLoanRequestsDTO response = loanService.getAllLoanRequestsForBusinessOwner(authentication);

        switch (response.getStatus()) {
            case SUCCESS: // accepted status returned for successfully creating loan request
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

            case FAIL: // bad request status returned when field is not in correct format
                return ResponseEntity.badRequest().body(response);

            default: // default error
                GetAllLoanRequestsDTO noResponse = new GetAllLoanRequestsDTO();
                noResponse.setStatus(LoanRequestRetrievalStatus.FAIL);
                noResponse.setMessage("Error status unrecognized");
                return ResponseEntity.badRequest().body(noResponse);
        }
    }

}
