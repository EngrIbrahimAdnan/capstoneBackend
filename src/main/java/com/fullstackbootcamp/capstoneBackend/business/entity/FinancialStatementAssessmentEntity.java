package com.fullstackbootcamp.capstoneBackend.business.entity;
import com.fullstackbootcamp.capstoneBackend.business.enums.BusinessState;
import jakarta.persistence.*;

@Entity
@Table(name = "business_data")
public class FinancialStatementAssessmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // -----------------------------
    // Financial Ratios – Profitability
    // -----------------------------
    @Column(name = "profitability_gross_margin")
    private Double profitabilityGrossMargin;

    @Column(name = "profitability_net_margin")
    private Double profitabilityNetMargin;

    @Column(name = "profitability_return_on_assets")
    private Double profitabilityReturnOnAssets;

    @Column(name = "profitability_return_on_equity")
    private Double profitabilityReturnOnEquity;

    // -----------------------------
    // Financial Ratios – Leverage
    // -----------------------------
    @Column(name = "leverage_debt_to_equity")
    private Double leverageDebtToEquity;

    @Column(name = "leverage_debt_to_assets")
    private Double leverageDebtToAssets;

    // -----------------------------
    // Financial Ratios – Operating
    // -----------------------------
    @Column(name = "operating_margin")
    private Double operatingMargin;

    // -----------------------------
    // Financial Ratios – Valuation
    // -----------------------------
    @Column(name = "valuation_dividend_payout_ratio")
    private Double valuationDividendPayoutRatio;

    @Column(name = "valuation_earnings_per_share")
    private Double valuationEarningsPerShare;

    // -----------------------------
    // Financial Ratios – Liquidity
    // -----------------------------
    @Column(name = "liquidity_current_ratio")
    private Double liquidityCurrentRatio;

    @Column(name = "liquidity_quick_ratio")
    private Double liquidityQuickRatio;

    // -----------------------------
    // Financial Ratios – Market
    // -----------------------------
    @Column(name = "market_price_earnings_ratio")
    private Double marketPriceEarningsRatio;

    // -----------------------------
    // Financial Ratios – Efficiency
    // -----------------------------
    @Column(name = "efficiency_accounts_receivable_turnover")
    private Double efficiencyAccountsReceivableTurnover;

    @Column(name = "efficiency_asset_turnover")
    private Double efficiencyAssetTurnover;

    // -----------------------------
    // Financial Ratios – Solvency
    // -----------------------------
    @Column(name = "solvency_interest_coverage_ratio")
    private Double solvencyInterestCoverageRatio;

    // -----------------------------
    // Financial Ratios – Capital Budgeting
    // -----------------------------
    @Column(name = "capital_budgeting_net_profit_from_operating_cash_flow")
    private Double capitalBudgetingNetProfitFromOperatingCashFlow;

    // -----------------------------
    // Assessment
    // -----------------------------
    @Column(name = "financial_score")
    private Double financialScore;

    @Column(name = "business_state")
    private BusinessState businessState;

    // Local Market Study (using longer columns for potentially long text)
    @Column(name = "market_overview", length = 2000)
    private String marketOverview;

    @Column(name = "business_prospects", length = 2000)
    private String businessProspects;

    // Recommendation
    @Column(name = "loan_feasibility")
    private String loanFeasibility;

    @Column(name = "recommended_loan_amount")
    private Double recommendedLoanAmount;

    @Column(name = "interest_rate")
    private String interestRate;

    @Column(name = "payment_period")
    private String paymentPeriod;

    // -----------------------------
    // Constructors
    // -----------------------------
    public FinancialStatementAssessmentEntity() {
    }

    public FinancialStatementAssessmentEntity(Double profitabilityGrossMargin, Double profitabilityNetMargin, Double profitabilityReturnOnAssets, Double profitabilityReturnOnEquity, Double leverageDebtToEquity, Double leverageDebtToAssets, Double operatingMargin, Double valuationDividendPayoutRatio, Double valuationEarningsPerShare, Double liquidityCurrentRatio, Double liquidityQuickRatio, Double marketPriceEarningsRatio, Double efficiencyAccountsReceivableTurnover, Double efficiencyAssetTurnover, Double solvencyInterestCoverageRatio, Double capitalBudgetingNetProfitFromOperatingCashFlow, Double financialScore, BusinessState businessState, String marketOverview, String businessProspects, String loanFeasibility, Double recommendedLoanAmount, String interestRate, String paymentPeriod) {
        this.profitabilityGrossMargin = profitabilityGrossMargin;
        this.profitabilityNetMargin = profitabilityNetMargin;
        this.profitabilityReturnOnAssets = profitabilityReturnOnAssets;
        this.profitabilityReturnOnEquity = profitabilityReturnOnEquity;
        this.leverageDebtToEquity = leverageDebtToEquity;
        this.leverageDebtToAssets = leverageDebtToAssets;
        this.operatingMargin = operatingMargin;
        this.valuationDividendPayoutRatio = valuationDividendPayoutRatio;
        this.valuationEarningsPerShare = valuationEarningsPerShare;
        this.liquidityCurrentRatio = liquidityCurrentRatio;
        this.liquidityQuickRatio = liquidityQuickRatio;
        this.marketPriceEarningsRatio = marketPriceEarningsRatio;
        this.efficiencyAccountsReceivableTurnover = efficiencyAccountsReceivableTurnover;
        this.efficiencyAssetTurnover = efficiencyAssetTurnover;
        this.solvencyInterestCoverageRatio = solvencyInterestCoverageRatio;
        this.capitalBudgetingNetProfitFromOperatingCashFlow = capitalBudgetingNetProfitFromOperatingCashFlow;
        this.financialScore = financialScore;
        this.businessState = businessState;
        this.marketOverview = marketOverview;
        this.businessProspects = businessProspects;
        this.loanFeasibility = loanFeasibility;
        this.recommendedLoanAmount = recommendedLoanAmount;
        this.interestRate = interestRate;
        this.paymentPeriod = paymentPeriod;
    }

    // -----------------------------
    // Getters and Setters
    // -----------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getProfitabilityGrossMargin() {
        return profitabilityGrossMargin;
    }

    public void setProfitabilityGrossMargin(Double profitabilityGrossMargin) {
        this.profitabilityGrossMargin = profitabilityGrossMargin;
    }

    public Double getProfitabilityNetMargin() {
        return profitabilityNetMargin;
    }

    public void setProfitabilityNetMargin(Double profitabilityNetMargin) {
        this.profitabilityNetMargin = profitabilityNetMargin;
    }

    public Double getProfitabilityReturnOnAssets() {
        return profitabilityReturnOnAssets;
    }

    public void setProfitabilityReturnOnAssets(Double profitabilityReturnOnAssets) {
        this.profitabilityReturnOnAssets = profitabilityReturnOnAssets;
    }

    public Double getProfitabilityReturnOnEquity() {
        return profitabilityReturnOnEquity;
    }

    public void setProfitabilityReturnOnEquity(Double profitabilityReturnOnEquity) {
        this.profitabilityReturnOnEquity = profitabilityReturnOnEquity;
    }

    public Double getLeverageDebtToEquity() {
        return leverageDebtToEquity;
    }

    public void setLeverageDebtToEquity(Double leverageDebtToEquity) {
        this.leverageDebtToEquity = leverageDebtToEquity;
    }

    public Double getLeverageDebtToAssets() {
        return leverageDebtToAssets;
    }

    public void setLeverageDebtToAssets(Double leverageDebtToAssets) {
        this.leverageDebtToAssets = leverageDebtToAssets;
    }

    public Double getOperatingMargin() {
        return operatingMargin;
    }

    public void setOperatingMargin(Double operatingMargin) {
        this.operatingMargin = operatingMargin;
    }

    public Double getValuationDividendPayoutRatio() {
        return valuationDividendPayoutRatio;
    }

    public void setValuationDividendPayoutRatio(Double valuationDividendPayoutRatio) {
        this.valuationDividendPayoutRatio = valuationDividendPayoutRatio;
    }

    public Double getValuationEarningsPerShare() {
        return valuationEarningsPerShare;
    }

    public void setValuationEarningsPerShare(Double valuationEarningsPerShare) {
        this.valuationEarningsPerShare = valuationEarningsPerShare;
    }

    public Double getLiquidityCurrentRatio() {
        return liquidityCurrentRatio;
    }

    public void setLiquidityCurrentRatio(Double liquidityCurrentRatio) {
        this.liquidityCurrentRatio = liquidityCurrentRatio;
    }

    public Double getLiquidityQuickRatio() {
        return liquidityQuickRatio;
    }

    public void setLiquidityQuickRatio(Double liquidityQuickRatio) {
        this.liquidityQuickRatio = liquidityQuickRatio;
    }

    public Double getMarketPriceEarningsRatio() {
        return marketPriceEarningsRatio;
    }

    public void setMarketPriceEarningsRatio(Double marketPriceEarningsRatio) {
        this.marketPriceEarningsRatio = marketPriceEarningsRatio;
    }

    public Double getEfficiencyAccountsReceivableTurnover() {
        return efficiencyAccountsReceivableTurnover;
    }

    public void setEfficiencyAccountsReceivableTurnover(Double efficiencyAccountsReceivableTurnover) {
        this.efficiencyAccountsReceivableTurnover = efficiencyAccountsReceivableTurnover;
    }

    public Double getEfficiencyAssetTurnover() {
        return efficiencyAssetTurnover;
    }

    public void setEfficiencyAssetTurnover(Double efficiencyAssetTurnover) {
        this.efficiencyAssetTurnover = efficiencyAssetTurnover;
    }

    public Double getSolvencyInterestCoverageRatio() {
        return solvencyInterestCoverageRatio;
    }

    public void setSolvencyInterestCoverageRatio(Double solvencyInterestCoverageRatio) {
        this.solvencyInterestCoverageRatio = solvencyInterestCoverageRatio;
    }

    public Double getCapitalBudgetingNetProfitFromOperatingCashFlow() {
        return capitalBudgetingNetProfitFromOperatingCashFlow;
    }

    public void setCapitalBudgetingNetProfitFromOperatingCashFlow(Double capitalBudgetingNetProfitFromOperatingCashFlow) {
        this.capitalBudgetingNetProfitFromOperatingCashFlow = capitalBudgetingNetProfitFromOperatingCashFlow;
    }

    public Double getFinancialScore() {
        return financialScore;
    }

    public void setFinancialScore(Double financialScore) {
        this.financialScore = financialScore;
    }

    public BusinessState getBusinessState() {
        return businessState;
    }

    public void setBusinessState(BusinessState businessState) {
        this.businessState = businessState;
    }

    public String getMarketOverview() {
        return marketOverview;
    }

    public void setMarketOverview(String marketOverview) {
        this.marketOverview = marketOverview;
    }

    public String getBusinessProspects() {
        return businessProspects;
    }

    public void setBusinessProspects(String businessProspects) {
        this.businessProspects = businessProspects;
    }

    public String getLoanFeasibility() {
        return loanFeasibility;
    }

    public void setLoanFeasibility(String loanFeasibility) {
        this.loanFeasibility = loanFeasibility;
    }

    public Double getRecommendedLoanAmount() {
        return recommendedLoanAmount;
    }

    public void setRecommendedLoanAmount(Double recommendedLoanAmount) {
        this.recommendedLoanAmount = recommendedLoanAmount;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getPaymentPeriod() {
        return paymentPeriod;
    }

    public void setPaymentPeriod(String paymentPeriod) {
        this.paymentPeriod = paymentPeriod;
    }
}
