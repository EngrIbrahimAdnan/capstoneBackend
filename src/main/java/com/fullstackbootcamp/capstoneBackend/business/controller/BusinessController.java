package com.fullstackbootcamp.capstoneBackend.business.controller;

import com.fullstackbootcamp.capstoneBackend.business.bo.CreateBusinessRequest;
import com.fullstackbootcamp.capstoneBackend.business.dto.BusinessDTO;
import com.fullstackbootcamp.capstoneBackend.business.service.BusinessService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/business")
@RestController
public class BusinessController {

    private BusinessService businessService;

    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @PostMapping("/create")
    public ResponseEntity<BusinessDTO> createBusiness(@RequestHeader("Authorization") String authHeader,
                                                      @Valid @RequestBody CreateBusinessRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(businessService.createBusiness(authHeader, request));
    }

    @GetMapping()
    public ResponseEntity<List<BusinessDTO>> getBusinesses(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(businessService.getBusiness(authHeader));
    }
}
