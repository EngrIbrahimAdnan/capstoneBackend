package com.fullstackbootcamp.capstoneBackend.business.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fullstackbootcamp.capstoneBackend.auth.bo.CustomUserDetails;
import com.fullstackbootcamp.capstoneBackend.auth.enums.TokenTypes;
import com.fullstackbootcamp.capstoneBackend.business.bo.AddBusinessRequest;
import com.fullstackbootcamp.capstoneBackend.business.dto.AddBusinessDTO;
import com.fullstackbootcamp.capstoneBackend.business.dto.GetBusinessDTO;
import com.fullstackbootcamp.capstoneBackend.business.entity.BusinessEntity;
import com.fullstackbootcamp.capstoneBackend.business.entity.BusinessLicenseEntity;
import com.fullstackbootcamp.capstoneBackend.business.entity.FinancialStatementAssessmentEntity;
import com.fullstackbootcamp.capstoneBackend.business.entity.FinancialStatementEntity;
import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessAdditionStatus;
import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessRetrievalStatus;
import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessState;
import com.fullstackbootcamp.capstoneBackend.business.repository.BusinessRepository;
import com.fullstackbootcamp.capstoneBackend.business.repository.FinancialStatementAssessmentRepository;
import com.fullstackbootcamp.capstoneBackend.business.repository.FinancialStatementRepository;
import com.fullstackbootcamp.capstoneBackend.file.entity.FileEntity;
import com.fullstackbootcamp.capstoneBackend.file.service.FileService;
import com.fullstackbootcamp.capstoneBackend.openai.service.OpenAIService;
import com.fullstackbootcamp.capstoneBackend.user.entity.UserEntity;
import com.fullstackbootcamp.capstoneBackend.user.enums.Roles;
import com.fullstackbootcamp.capstoneBackend.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BusinessServiceImpl implements BusinessService {

    private final BusinessRepository businessRepository;
    private final FinancialStatementAssessmentRepository financialStatementAssessmentRepository;
    private final FinancialStatementRepository financialStatementRepository;
    private final OpenAIService openAIService;
    private final UserService userService;
    private final FileService fileService;

    public BusinessServiceImpl(BusinessRepository businessRepository,
                               UserService userService,
                               FinancialStatementRepository financialStatementRepository,
                               FinancialStatementAssessmentRepository financialStatementAssessmentRepository,
                               OpenAIService openAIService,
                               FileService fileService) {
        this.businessRepository = businessRepository;
        this.userService = userService;
        this.financialStatementRepository = financialStatementRepository;
        this.financialStatementAssessmentRepository = financialStatementAssessmentRepository;
        this.openAIService = openAIService;
        this.fileService = fileService;
    }

    // Business owner adds their business
    @Override
    public AddBusinessDTO addBusiness(AddBusinessRequest request, Authentication authentication) throws JsonProcessingException {
        AddBusinessDTO response = new AddBusinessDTO();

        String message = validateToken(authentication); // Validate token and get response

        // validate Token
        if (message != null) {
            response.setStatus(BusinessAdditionStatus.FAIL);
            response.setMessage(message); // attach error message
            return response; // If there was an error during validation, return early
        }

        // ensure the user in the token exists
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String civilId = userDetails.getCivilId();
        Optional<UserEntity> user = userService.getUserByCivilId(civilId.toString());

        if (user.isEmpty()) {
            response.setStatus(BusinessAdditionStatus.FAIL);
            response.setMessage("User does not exist.");
            return response;
        }

        // Ensure the business owner doesn't have any businesses they are associated with, before the business is added
        /* NOTE:
            At the moment, its one-to-one relationship between business owner user and business entity
            However, Realistically, there can be more than one. But it's done so, for the sake of simplicity
         */
        Optional<BusinessEntity> businessEntity = businessRepository.findByBusinessOwnerUser(user.get());

        if (businessEntity.isPresent()) {
            response.setStatus(BusinessAdditionStatus.ALREADY_EXIST);
            response.setMessage("Business Owner already exists. There can only be one.");
            return response;
        }

        // The business entity is added
        BusinessEntity business = new BusinessEntity();

        // Add Business Owner user entity
        business.setBusinessOwnerUser(user.get());


        // Attempt to store business avatar
        try {
            FileEntity entity = fileService.saveFile(request.getBusinessAvatar());

            // Upon successfully saving file, ID pointing to the financial statement file in file repository is added
            /* Hack:
                Although it looks disgusting to associate the business entity with the file entity this way,
                this gets rid of the error message "LOB: unable to stream". This might be revisited later due to how
                disgusting this looks. The error steams from adding relationships with large files (like images)
                will look for a way around it.
             */
            business.setBusinessAvatarFileId(entity.getId());
        } catch (Exception e) {
            response.setStatus(BusinessAdditionStatus.FAIL);
            response.setMessage("Unable to upload avatar document.");
            return response;
        }

        // add Business Nickname
        /* REVIEW:
            nickname doesn't have to be lower-cased since it won't be used to search with
            Its merely a convenience for the user
         */

        business.setBusinessNickname(request.getBusinessNickname());

        // Attempt to store financial statement document
        try {
            FileEntity entity = fileService.saveFile(request.getFinancialStatementPDF());

            // Upon successfully saving file, ID pointing to the financial statement file in file repository is added
            /* Hack:
                Although it looks disgusting to associate the business entity with the file entity this way,
                this gets rid of the error message "LOB: unable to stream". This might be revisited later due to how
                disgusting this looks. The error steams from adding relationships with large files (like images)
                will look for a way around it.
             */
            business.setFinancialStatementFileId(entity.getId());
        } catch (Exception e) {
            response.setStatus(BusinessAdditionStatus.FAIL);
            response.setMessage("Unable to upload PDF document.");
            return response;
        }

        // to store business license Image
        try {
            FileEntity entity = fileService.convertAndSaveAsPDF(request.getBusinessLicenseImage());

            /* HACK:
                Again, we are associating the business entity with the ID of the business license file
                instead of an entity relationship
             */
            business.setBusinessLicenseImageFileId(entity.getId());

        } catch (Exception e) {
            response.setStatus(BusinessAdditionStatus.FAIL);
            response.setMessage("Unable to upload business license as image.");
            return response;
        }

        // TODO: Remove, this is for testing purposes
        request.setFinancialStatementText("FINANCIAL STATEMENT Entity Name: Jumbo Shrimp Entity Type: Corporation Statement ID: FS-2025001 Statement Period: 01 Jan 2024 - 31 Dec 2024 Currency: KWD (Kuwaiti Dinar) Business Type Restaurant Income Statement Description Amount (KWD) Revenue 200,000.00 Cost of Goods Sold (COGS) 80,000.00 Gross Profit 120,000.00 Operating Expenses 40,000.00 Net Income 80,000.00 Zakat Amount (2.5%) 2,000.00 Balance Sheet Assets Amount (KWD) Total Assets 500,000.00 Cash and Cash Equivalents 100,000.00 Accounts Receivable 50,000.00 Ijara Assets 20,000.00 Liabilities Amount (KWD) Total Liabilities 300,000.00 Accounts Payable 70,000.00 Murabaha Payables 50,000.00 Equity Amount (KWD) Shareholder Equity 200,000.00 Cash Flow Statement Description Amount (KWD) Operating Cash Flow 50,000.00 Investing Cash Flow -30,000.00 Financing Cash Flow 40,000.00 Net Cash Flow 60,000.00 Other Relevant Fields Description Value Zakat Eligibility Yes Profit Sharing (Mudarabah) 10,000.00 KWD Islamic Compliance Certification Certified ID: IC-12345 Dividend Payments 20,000.00 KWD");

        // Convert financial statement
        Map<String, Object> finacincialStatementJson = openAIService.convertFinancialStatement(request.getFinancialStatementText());

        // Create and save financial statement
        FinancialStatementEntity financialStatement = new FinancialStatementEntity();

        financialStatement.setStatementId((String) finacincialStatementJson.get("statementId"));
        financialStatement.setStatementPeriod((String) finacincialStatementJson.get("statementPeriod"));
        financialStatement.setRevenue((String) finacincialStatementJson.get("revenue"));
        financialStatement.setCostOfGoodsSold((String) finacincialStatementJson.get("costOfGoodsSold"));
        financialStatement.setGrossProfit((String) finacincialStatementJson.get("grossProfit"));
        financialStatement.setOperatingExpenses((String) finacincialStatementJson.get("operatingExpenses"));
        financialStatement.setNetIncome((String) finacincialStatementJson.get("netIncome"));
        financialStatement.setZakatAmount((String) finacincialStatementJson.get("zakatAmount")); // may be null
        financialStatement.setTotalAssets((String) finacincialStatementJson.get("totalAssets"));
        financialStatement.setCashAndCashEquivalents((String) finacincialStatementJson.get("cashAndCashEquivalents"));
        financialStatement.setAccountsReceivable((String) finacincialStatementJson.get("accountsReceivable"));
        financialStatement.setIjaraAssets((String) finacincialStatementJson.get("ijaraAssets")); // may be null
        financialStatement.setTotalLiabilities((String) finacincialStatementJson.get("totalLiabilities"));
        financialStatement.setAccountsPayable((String) finacincialStatementJson.get("accountsPayable"));
        financialStatement.setMurabahaPayables((String) finacincialStatementJson.get("murabahaPayables")); // may be null
        financialStatement.setShareholderEquity((String) finacincialStatementJson.get("shareholderEquity"));
        financialStatement.setOperatingCashFlow((String) finacincialStatementJson.get("operatingCashFlow"));
        financialStatement.setInvestingCashFlow((String) finacincialStatementJson.get("investingCashFlow"));
        financialStatement.setFinancingCashFlow((String) finacincialStatementJson.get("financingCashFlow"));
        financialStatement.setNetCashFlow((String) finacincialStatementJson.get("netCashFlow"));
        financialStatement.setMudaraba((String) finacincialStatementJson.get("mudaraba")); // may be null
        financialStatement.setIslamicComplianceCertification((String) finacincialStatementJson.get("islamicComplianceCertification")); // may be null
        financialStatement.setDividendPayments((String) finacincialStatementJson.get("dividendPayments")); // may be null

        business.setFinancialStatement(financialStatement);

        // Set financial statement assessment
        Map<String, Object> financialStatementAssessmentJson = openAIService.getFinancialAssessment(request.getFinancialStatementText());

        FinancialStatementAssessmentEntity financialStatementAssessment = new FinancialStatementAssessmentEntity();


// --- Financial Ratios ---
        Map<String, Object> financialRatios = (Map<String, Object>) financialStatementAssessmentJson.get("financialRatios");
        if (financialRatios != null) {
            // Profitability Ratios
            Map<String, Object> profitabilityRatios = (Map<String, Object>) financialRatios.get("profitabilityRatios");
            if (profitabilityRatios != null) {
                financialStatementAssessment.setProfitabilityGrossMargin(castToDouble(profitabilityRatios.get("grossMargin")));
                financialStatementAssessment.setProfitabilityNetMargin(castToDouble(profitabilityRatios.get("netMargin")));
                financialStatementAssessment.setProfitabilityReturnOnAssets(castToDouble(profitabilityRatios.get("returnOnAssets")));
                financialStatementAssessment.setProfitabilityReturnOnEquity(castToDouble(profitabilityRatios.get("returnOnEquity")));
            }

            // Leverage Ratios
            Map<String, Object> leverageRatios = (Map<String, Object>) financialRatios.get("leverageRatios");
            if (leverageRatios != null) {
                financialStatementAssessment.setLeverageDebtToEquity(castToDouble(leverageRatios.get("debtToEquity")));
                financialStatementAssessment.setLeverageDebtToAssets(castToDouble(leverageRatios.get("debtToAssets")));
            }

            // Operating Ratios
            Map<String, Object> operatingRatios = (Map<String, Object>) financialRatios.get("operatingRatios");
            if (operatingRatios != null) {
                financialStatementAssessment.setOperatingMargin(castToDouble(operatingRatios.get("operatingMargin")));
            }

            // Valuation Ratios
            Map<String, Object> valuationRatios = (Map<String, Object>) financialRatios.get("valuationRatios");
            if (valuationRatios != null) {
                financialStatementAssessment.setValuationDividendPayoutRatio(castToDouble(valuationRatios.get("dividendPayoutRatio")));
                financialStatementAssessment.setValuationEarningsPerShare(castToDouble(valuationRatios.get("earningsPerShare")));
            }

            // Liquidity Ratios
            Map<String, Object> liquidityRatios = (Map<String, Object>) financialRatios.get("liquidityRatios");
            if (liquidityRatios != null) {
                financialStatementAssessment.setLiquidityCurrentRatio(castToDouble(liquidityRatios.get("currentRatio")));
                financialStatementAssessment.setLiquidityQuickRatio(castToDouble(liquidityRatios.get("quickRatio")));
            }

            // Market Ratios
            Map<String, Object> marketRatios = (Map<String, Object>) financialRatios.get("marketRatios");
            if (marketRatios != null) {
                financialStatementAssessment.setMarketPriceEarningsRatio(castToDouble(marketRatios.get("priceEarningsRatio")));
            }

            // Efficiency Ratios
            Map<String, Object> efficiencyRatios = (Map<String, Object>) financialRatios.get("efficiencyRatios");
            if (efficiencyRatios != null) {
                financialStatementAssessment.setEfficiencyAccountsReceivableTurnover(castToDouble(efficiencyRatios.get("accountsReceivableTurnover")));
                financialStatementAssessment.setEfficiencyAssetTurnover(castToDouble(efficiencyRatios.get("assetTurnover")));
            }

            // Solvency Ratios
            Map<String, Object> solvencyRatios = (Map<String, Object>) financialRatios.get("solvencyRatios");
            if (solvencyRatios != null) {
                financialStatementAssessment.setSolvencyInterestCoverageRatio(castToDouble(solvencyRatios.get("interestCoverageRatio")));
            }

            // Capital Budgeting Ratios
            Map<String, Object> capitalBudgetingRatios = (Map<String, Object>) financialRatios.get("capitalBudgetingRatios");
            if (capitalBudgetingRatios != null) {
                financialStatementAssessment.setCapitalBudgetingNetProfitFromOperatingCashFlow(
                        castToDouble(capitalBudgetingRatios.get("netProfitFromOperatingCashFlow")));
            }
        }

// --- Assessment ---
        Map<String, Object> assessment = (Map<String, Object>) financialStatementAssessmentJson.get("assessment");
        if (assessment != null) {
            // Financial Score (ensure conversion to Integer)
            Object scoreObj = assessment.get("financialScore");
            if (scoreObj instanceof Number) {
                financialStatementAssessment.setFinancialScore(((Number) scoreObj).doubleValue());
                business.setFinancialScore(((Number) scoreObj).doubleValue());
            }

            // Business State (convert the String to the BusinessState enum)
            Object businessStateObj = assessment.get("businessState");
            if (businessStateObj != null) {
                try {
                    // Convert to uppercase to match the enum naming
                    BusinessState businessState = BusinessState.valueOf(businessStateObj.toString().toUpperCase());
                    financialStatementAssessment.setBusinessState(businessState);
                    business.setBusinessState(businessState);
                } catch (IllegalArgumentException e) {
                    // If the value is not a valid BusinessState, set it to STABLE
                    financialStatementAssessment.setBusinessState(BusinessState.STABLE);
                    business.setBusinessState(BusinessState.STABLE);
                }
            }

            // Local Market Study
            Map<String, Object> localMarketStudy = (Map<String, Object>) assessment.get("localMarketStudy");
            if (localMarketStudy != null) {
                financialStatementAssessment.setMarketOverview((String) localMarketStudy.get("marketOverview"));
                financialStatementAssessment.setBusinessProspects((String) localMarketStudy.get("businessProspects"));
                business.setFinancialAnalysis((String) localMarketStudy.get("businessProspects"));
            }

            // Recommendation
            Map<String, Object> recommendation = (Map<String, Object>) assessment.get("recommendation");
            if (recommendation != null) {
                financialStatementAssessment.setLoanFeasibility((String) recommendation.get("loanFeasibility"));
                List<Map<String, Object>> conditions = (List<Map<String, Object>>) recommendation.get("conditions");
                if (conditions != null && !conditions.isEmpty()) {
                    Map<String, Object> condition = conditions.get(0);
                    financialStatementAssessment.setRecommendedLoanAmount(castToDouble(condition.get("recommendedLoanAmount")));
                    financialStatementAssessment.setInterestRate((String) condition.get("interestRate"));
                    financialStatementAssessment.setPaymentPeriod((String) condition.get("paymentPeriod"));
                }
            }
        }

        financialStatementAssessmentRepository.save(financialStatementAssessment);
        financialStatement.setFinancialStatementAssessment(financialStatementAssessment);
        financialStatementRepository.save(financialStatement);

        business.setFinancialStatement(financialStatement);

        // add business license
        /* NOTE:
            error handling to check if a business license already associates with
            the business entity won't be necessary since the business entity is just created
            However, this error may come in handy if you use pgadmin to delete the business entity
         */
        BusinessLicenseEntity businessLicense = new BusinessLicenseEntity();

        Map<String, Object> businessLicenseJson = openAIService.convertBusinessLicense(request.getBusinessLicenseText());

        // (Right column)
        businessLicense.setLicenseNumber(asString(businessLicenseJson.get("licenseNumber")));
        businessLicense.setIssueDate(asLocalDate(businessLicenseJson.get("issueDate")));
        businessLicense.setCentralNumber(asString(businessLicenseJson.get("centralNumber")));
        businessLicense.setCommercialRegistrationNumber(asString(businessLicenseJson.get("commercialRegistrationNumber")));
        businessLicense.setLegalEntity(asString(businessLicenseJson.get("legalEntity")));
        businessLicense.setBusinessName(asString(businessLicenseJson.get("businessName")));
        businessLicense.setCapital(asString(businessLicenseJson.get("capital")));

        // (Left column)
        businessLicense.setFileNumber(asString(businessLicenseJson.get("fileNumber")));
        businessLicense.setExpiryDate(asLocalDate(businessLicenseJson.get("expiryDate")));
        businessLicense.setCivilAuthorityNumber(asString(businessLicenseJson.get("civilAuthorityNumber")));
        businessLicense.setLicenseType(asString(businessLicenseJson.get("licenseType")));
        businessLicense.setRegistrationDate(asLocalDate(businessLicenseJson.get("registrationDate")));

        // Business Activities
        businessLicense.setActivityName(asString(businessLicenseJson.get("activityName")));
        businessLicense.setActivityCode(asString(businessLicenseJson.get("activityCode")));

        // Business Address
        businessLicense.setAddressReferenceNumber(asString(businessLicenseJson.get("addressReferenceNumber")));
        businessLicense.setGovernorate(asString(businessLicenseJson.get("governorate")));
        businessLicense.setArea(asString(businessLicenseJson.get("area")));
        businessLicense.setBlock(asString(businessLicenseJson.get("block")));
        businessLicense.setSection(asString(businessLicenseJson.get("section")));
        businessLicense.setStreet(asString(businessLicenseJson.get("street")));
        businessLicense.setBuildingName(asString(businessLicenseJson.get("buildingName")));
        businessLicense.setFloor(asString(businessLicenseJson.get("floor")));
        businessLicense.setUnitNumber(asString(businessLicenseJson.get("unitNumber")));

        // Other Relevant Fields
        businessLicense.setLastTransactionDate(asLocalDate(businessLicenseJson.get("lastTransactionDate")));
        businessLicense.setRequestNumber(asString(businessLicenseJson.get("requestNumber")));

        business.setBusinessLicense(businessLicense);

        businessRepository.save(business);

        response.setStatus(BusinessAdditionStatus.SUCCESS);
        response.setMessage("Successfully added business to user.");
        return response;
    }


    public GetBusinessDTO getBusiness(Authentication authentication) {
        GetBusinessDTO response = new GetBusinessDTO();

        String message = validateToken(authentication); // Validate token and get response

        // return a response if a message is returned
        if (message != null) {
            response.setStatus(BusinessRetrievalStatus.FAIL);
            response.setMessage(message);
            return response;
        }

        // ensure the user in the token exists
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String civilId = userDetails.getCivilId();
        Optional<UserEntity> user = userService.getUserByCivilId(civilId.toString());

        // if use is not found in repository
        /* TODO: Instead of Fail, another enum value could be added so that it gets routed to an
            explicit case in the controller layer to utilize 'HttpStatus.NOT_FOUND' which is more
             appropriate than 'Bad_Request' when user is not found.
         */
        if (user.isEmpty()) {
            response.setStatus(BusinessRetrievalStatus.FAIL);
            response.setMessage("User does not exist");
            return response;
        }

        // check business exists
        Optional<BusinessEntity> businessEntity = getBusinessOwnerEntity(user.get());

        if (businessEntity.isEmpty()) {
            response.setStatus(BusinessRetrievalStatus.FAIL);
            response.setMessage("No business is associated with user");
            return response; // If there was an error during validation, return early
        }

        // Check existence of both files
        /* REVIEW: although this wouldn't throwout an error for 'financial Statement' & 'business license' since only the image Ids
         *   are stored, this is especially critical when the relationships are revisited and added between business entities
         *   and file entities. It doesn't hurt to also check for the time being*/

        Optional<FileEntity> financialStatement = fileService.getFile(businessEntity.get().getFinancialStatementFileId());

        if (financialStatement.isEmpty()) {
            response.setStatus(BusinessRetrievalStatus.FAIL);
            response.setMessage("No financial statement is associated with business");
            return response;
        }

        Optional<FileEntity> businessLicense = fileService.getFile(businessEntity.get().getBusinessLicenseImageFileId());

        if (businessLicense.isEmpty()) {
            response.setStatus(BusinessRetrievalStatus.FAIL);
            response.setMessage("No business license is associated with business");
            return response;
        }

        // if all is well, return success
        response.setStatus(BusinessRetrievalStatus.SUCCESS);
        response.setMessage("Business entity is successfully retrieved.");
        response.setEntity(businessEntity.get());
        return response;
    }


    public String validateToken(Authentication authentication) {
//        Jwt jwt = (Jwt) authentication.getPrincipal();
//
//        // To ensure the access token is provided and NOT the refresh token
//        if (jwt.getClaims().get("type").equals(TokenTypes.REFRESH.name())) {
//            return "Incorrect Token provided. Please provide access token";
//        }
//
//        // Ensures the user is business owner
//        if (jwt.getClaims().get("roles").equals(Roles.BANKER.name())) {
//            return "Not allowed for bankers. This endpoint is only for Business Owners";
//        }

        return null; // No errors, return null to continue the flow
    }

    private Double castToDouble(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue();
        }
        try {
            return Double.valueOf(obj.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Helper method to safely convert an Object to String.
     */
    private String asString(Object value) {
        return value != null ? value.toString() : null;
    }

    /**
     * Helper method to convert an Object to LocalDate.
     * Assumes that the value is a String in ISO-8601 format (e.g., "2023-04-28").
     */
    private LocalDate asLocalDate(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return LocalDate.parse(value.toString());
        } catch (Exception e) {
            // Log the exception if needed and return null if the date can't be parsed.
            return null;
        }
    }
    public Optional<BusinessEntity> getBusinessOwnerEntity(UserEntity user) {
        return businessRepository.findByBusinessOwnerUser(user);
    }
}
