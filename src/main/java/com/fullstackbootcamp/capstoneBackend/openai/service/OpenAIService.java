package com.fullstackbootcamp.capstoneBackend.openai.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

  public Map<String, Object> getChatGPTResponse(String prompt) throws JsonProcessingException {

    RestTemplate restTemplate = new RestTemplate();

    // Prepare headers
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + API_KEY);
    headers.set("Content-Type", "application/json");

    // System message
    Map<String, Object> systemMessage = Map.of(
            "role", "system",
            "content",
            """
            You are a financial analysis assistant. Based on the provided financial statement documentation, 
            perform the following tasks:
            
            1. Calculate and provide the following financial ratios:
               - Profitability ratios
               - Leverage ratios
               - Operating ratios
               - Valuation ratios
               - Liquidity ratios
               - Market ratios
               - Efficiency ratios
               - Solvency ratios
               - Capital budgeting ratios
        
            2. Provide a numerical financialScore between 0 and 10 (0 = worst, 10 = best).
        
            3. Provide a businessState that must be exactly one of the following:
               EXCELLENT, GOOD, STABLE, STRUGGLING, CRITICAL, or BANKRUPT.
        
            4. Include a local market study that considers the Kuwaiti market:
               - marketOverview: a summary of relevant market conditions in Kuwait
               - businessProspects: whether this specific business has good prospects in Kuwait
        
            5. Give a personal recommendation (loanFeasibility) to the banker about whether 
               or not to provide a loan to the company, and specify any conditions or requirements 
               in a list (conditions).
        
            **Return all results in valid JSON** with the following structure:
        
            {
              "financialRatios": {
                "profitabilityRatios": "...",
                "leverageRatios": "...",
                "operatingRatios": "...",
                "valuationRatios": "...",
                "liquidityRatios": "...",
                "marketRatios": "...",
                "efficiencyRatios": "...",
                "solvencyRatios": "...",
                "capitalBudgetingRatios": "..."
              },
              "assessment": {
                "financialScore": 0,
                "businessState": "",
                "localMarketStudy": {
                  "marketOverview": "",
                  "businessProspects": ""
                },
                "recommendation": {
                  "loanFeasibility": "",
                  "conditions": []
                }
              }
            }
        
            Make sure your response can be parsed easily in JSON format without extra commentary. DO NOT wrap the JSON in triple backticks.
            """
    );

    Map<String, Object> userMessage = Map.of(
            "role", "user",
            "content", prompt
    );

    // Prepare request body
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("model", "chatgpt-4o-latest");
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

    // Use Jackson to parse the content JSON into a Map
    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> parsedJson = mapper.readValue(content, new TypeReference<>() {});
    return parsedJson;
  }

}
