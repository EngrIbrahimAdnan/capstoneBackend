package com.fullstackbootcamp.capstoneBackend.openai.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fullstackbootcamp.capstoneBackend.openai.bo.ChatRequest;
import com.fullstackbootcamp.capstoneBackend.openai.service.OpenAIService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
public class OpenAIController {

    private final OpenAIService openAIService;

    public OpenAIController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/api/v1/user/chat")
    public ResponseEntity<Map<String, Object>> chat(@Valid @RequestBody ChatRequest request) {
        try {
            Map<String, Object> json = openAIService.getChatGPTResponse(request.getPrompt());
            // Spring automatically converts Map to JSON
            return ResponseEntity.ok(json);
        } catch (JsonProcessingException e) {
            // Handle JSON parse errors or other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to parse ChatGPT response"));
        }
    }
}
