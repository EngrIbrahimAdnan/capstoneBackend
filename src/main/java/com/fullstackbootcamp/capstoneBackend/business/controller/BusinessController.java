package com.fullstackbootcamp.capstoneBackend.business.controller;

import com.fullstackbootcamp.capstoneBackend.business.dto.BusinessDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/business")
@RestController
public class BusinessController {

    @PostMapping("/create")
    public ResponseEntity<BusinessDTO> createBusiness() {
        // TODO
        return null;
    }
}
