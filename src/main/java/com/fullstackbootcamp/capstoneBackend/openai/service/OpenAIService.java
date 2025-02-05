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

  public Map<String, Object> getFinancialAssessment(String prompt) throws JsonProcessingException {

    RestTemplate restTemplate = new RestTemplate();

    // Prepare headers
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + API_KEY);
    headers.set("Content-Type", "application/json");

    // System message
    Map<String, Object> systemMessage =
        Map.of(
            "role",
            "system",
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

            **Return all results in valid JSON** with the structure of the following example:

            {
               "financialRatios": {
                 "profitabilityRatios": {
                   "grossMargin": 0.6,
                   "netMargin": 0.4,
                   "returnOnAssets": 0.16,
                   "returnOnEquity": 0.4
                 },
                 "leverageRatios": {
                   "debtToEquity": 1.5,
                   "debtToAssets": 0.6
                 },
                 "operatingRatios": {
                   "operatingMargin": 0.2
                 },
                 "valuationRatios": {
                   "dividendPayoutRatio": 0.25,
                   "earningsPerShare": 1
                 },
                 "liquidityRatios": {
                   "currentRatio": 2,
                   "quickRatio": 1.43
                 },
                 "marketRatios": {
                   "priceEarningsRatio": 1
                 },
                 "efficiencyRatios": {
                   "accountsReceivableTurnover": 4,
                   "assetTurnover": 0.4
                 },
                 "solvencyRatios": {
                   "interestCoverageRatio": 1
                 },
                 "capitalBudgetingRatios": {
                   "netProfitFromOperatingCashFlow": 0.625
                 }
               },
               "assessment": {
                 "financialScore": 8,
                 "businessState": "GOOD",
                 "localMarketStudy": {
                   "marketOverview": "The Kuwaiti restaurant market is competitive, driven by high urbanization rates and significant disposable income among residents. Increasing demand for diverse cuisines and premium dining experiences is notable, alongside significant regulatory compliance requirements. However, risks such as fluctuating food costs and market saturation are key considerations.",
                   "businessProspects": "The restaurant business has good prospects in Kuwait, given the growing demand for dining experiences and the company's financial health. The certified Islamic compliance also appeals to a wider customer base."
                 },
                 "recommendation": {
                   "loanFeasibility": "Yes",
                   "conditions": [
                     {
                       "recommendedLoanAmount": 100000,
                       "interestRate": "4%",
                       "paymentPeriod": "12 months"
                     }
                   ]
                 }
               }
             }

            The conditions should define the recommended loan amount, the interest rate, and the period of payment (for example, 100,000 KWD over 10 months)

            If there are any ratios or values that are not available, make the values null.

            Make sure your response can be parsed easily in JSON format without extra commentary. DO NOT wrap the JSON in triple backticks.
            """);

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

  public Map<String, Object> convertFinancialStatement(String financialStatementText) throws JsonProcessingException {

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
            You are a financial analysis assistant. Your task is to parse the provided financial statement documentation and convert it into a JSON object that exactly matches the following structure:
        
            {
              "statementPeriod": "String",                // The period of the statement
              "revenue": "String",                        // Revenue figure
              "costOfGoodsSold": "String",                // Cost of goods sold
              "grossProfit": "String",                    // Gross profit
              "operatingExpenses": "String",              // Operating expenses
              "netIncome": "String",                      // Net income
              "zakatAmount": "String or null",            // Zakat amount (nullable)
              "totalAssets": "String",                    // Total assets
              "cashAndCashEquivalents": "String",         // Cash and cash equivalents
              "accountsReceivable": "String",             // Accounts receivable
              "ijaraAssets": "String or null",            // Ijara assets (nullable)
              "totalLiabilities": "String",               // Total liabilities
              "accountsPayable": "String",                // Accounts payable
              "murabahaPayables": "String or null",       // Murabaha payables (nullable)
              "shareholderEquity": "String",              // Shareholder equity
              "operatingCashFlow": "String",              // Operating cash flow
              "investingCashFlow": "String",              // Investing cash flow
              "financingCashFlow": "String",              // Financing cash flow
              "netCashFlow": "String",                    // Net cash flow
              "mudaraba": "String or null",               // Mudaraba (nullable)
              "islamicComplianceCertification": "String or null", // Islamic compliance certification (nullable)
              "dividendPayments": "String or null"        // Dividend payments (nullable)
            }
        
            For any ratios or values that are not available in the documentation, assign the value null. Make sure your response is valid JSON and includes only the JSON object without any additional commentary or markdown formatting. Do NOT wrap your output in triple backticks.
            """
    );

    Map<String, Object> userMessage = Map.of(
            "role", "user",
            "content", financialStatementText
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

  public Map<String, Object> convertBusinessLicense(String financialStatementText) throws JsonProcessingException {

    RestTemplate restTemplate = new RestTemplate();

    // Prepare headers
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + API_KEY);
    headers.set("Content-Type", "application/json");

    // System message
    Map<String, Object> systemMessage =
        Map.of(
            "role",
            "system",
            "content",
            """
            You are a financial analysis assistant. Your task is to parse the provided business license text and convert it into a JSON object that exactly matches the following structure:
            IF THE INPUT IS INCORRECT, STILL RETURN THIS JSON OBJECT
            {
              //(right column)
                  private String licenseNumber;
                  private LocalDate issueDate;
                  private String centralNumber;
                  private String commercialRegistrationNumber;
                  private String legalEntity;
                  private String businessName;
                  private String capital;

                  // (left column)
                  private String fileNumber;
                  private LocalDate expiryDate;
                  private String civilAuthorityNumber;
                  private String licenseType;
                  private LocalDate registrationDate;

                  // Business Activities
                  private String activityName;
                  private String activityCode;

                  // Business Address
                  private String addressReferenceNumber;
                  private String governorate;
                  private String area;
                  private String block;
                  private String section;
                  private String street;
                  private String buildingName;
                  private String floor;
                  private String unitNumber;

                  // Other Relevant Fields
                  private LocalDate lastTransactionDate;
                  private String requestNumber;
            }
            The text will be in arabic and these are the following translated fields, but what you return should be in english:

            licenseNumber (رقم الترخيص) - Unique identifier for the business license.
            issueDate (تاريخ إصدار الترخيص) - The date the license was issued.
            centralNumber (الرقم المركزي) - A central registration number.
            commercialRegistrationNumber (رقم السجل التجاري) - The business's official commercial registration number.
            legalEntity (الكيان القانوني) - The legal type of the business (e.g., LLC, Sole Proprietorship).
            businessName (تحت الاسم التجاري) - The trade name of the business.
            capital (رأس المال - دينار كويتي) - The registered capital in Kuwaiti Dinar (KWD).

            (left column)
            fileNumber (رقم الملف) - A unique identifier for the business file.
            expiryDate (تاريخ انتهاء الترخيص) - The date the license expires.
            civilAuthorityNumber (رقم الجهة المدني) - The civil authority registration number.
            licenseType (نوع الترخيص) - Specifies the type of license (e.g., commercial, industrial, etc.).

            registrationDate (تاريخ القيد في السجل) - The date the business was registered.

            Business Activities:
            activityName (اسم النشاط) - The name of the business activity.
            activityCode (رمز النشاط) - The activity code related to the business operation.


            Business Address:
            addressReferenceNumber (الرقم الآلي للعنوان) - A unique reference for the address.
            governorate (المحافظة) - The region or governorate where the business is located.
            area (المنطقة) - The specific area within the governorate.
            block (القطعة) - The block number in the area.
            section (القسيمة) - The plot or section number.
            street (الشارع) - The street name.
            buildingName (اسم المبنى) - The name of the building.
            floor (الدور) - The floor number.
            unitNumber (رقم الوحدة) - The unit number within the building.


            Other Relevant Fields:
            lastTransactionDate (آخر إجراء تم علي الترخيص) - The last transaction performed on the license.
            requestNumber (برقم طلب) - The request number related to the last transaction.
            
            For any ratios or values that are not available in the documentation, assign the value null. Make sure your response is valid JSON and includes only the JSON object without any additional commentary or markdown formatting. Do NOT wrap your output in triple backticks.
            """);

    Map<String, Object> userMessage = Map.of(
            "role", "user",
            "content", financialStatementText
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
