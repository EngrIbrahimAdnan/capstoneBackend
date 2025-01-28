package com.fullstackbootcamp.capstoneBackend.openai.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAIService {

  private final String API_URL = "https://api.openai.com/v1/chat/completions";

  private final String sampleResponse = "";

  @Value("${myapp.secret-key}")
  private String API_KEY;

  public String getChatGPTResponse(String prompt) {

    RestTemplate restTemplate = new RestTemplate();

    // Prepare headers
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + API_KEY);
    headers.set("Content-Type", "application/json");

    // Create a system message and the user message
    Map<String, Object> systemMessage = Map.of(
            "role", "system",
            // TODO: Give a proper system message
            "content", "You are a helpful chatbot. Please respond in a friendly and concise manner."
    );

    Map<String, Object> userMessage = Map.of(
            "role", "user",
            "content", prompt
    );

    // Prepare request body
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("model", "o1-mini");
    // Put both the system and user messages in the messages array
    requestBody.put("messages", List.of(systemMessage, userMessage));

    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

    // Call the API, asking Spring to parse into a nested Map
    ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
            API_URL,
            HttpMethod.POST,
            entity,
            new ParameterizedTypeReference<>() {}
    );

    Map<String, Object> responseBody = response.getBody();

    // The 'choices' field is typically a list of maps
    List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
    Map<String, Object> firstChoice = choices.get(0);

    // The 'message' field is another map
    Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");

    // Finally, the 'content' field is a string
    String content = (String) message.get("content");

    return content;
  }

}
