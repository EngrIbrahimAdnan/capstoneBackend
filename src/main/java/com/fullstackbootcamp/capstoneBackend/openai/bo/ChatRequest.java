package com.fullstackbootcamp.capstoneBackend.openai.bo;

import jakarta.validation.constraints.NotNull;

public class ChatRequest {
    @NotNull(message = "Prompt is required")
    private String prompt;

    // Getter and Setter
    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}