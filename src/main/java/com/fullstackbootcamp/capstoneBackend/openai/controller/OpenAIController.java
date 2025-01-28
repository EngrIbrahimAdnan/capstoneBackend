package com.fullstackbootcamp.capstoneBackend.openai.controller;

import com.fullstackbootcamp.capstoneBackend.openai.bo.ChatRequest;
import com.fullstackbootcamp.capstoneBackend.openai.service.OpenAIService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
public class OpenAIController {

    private final OpenAIService openAIService;

    public OpenAIController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/api/v1/user/chat")
    public String chat(@Valid @RequestBody ChatRequest request) {
        return openAIService.getChatGPTResponse(request.getPrompt());
    }
}
