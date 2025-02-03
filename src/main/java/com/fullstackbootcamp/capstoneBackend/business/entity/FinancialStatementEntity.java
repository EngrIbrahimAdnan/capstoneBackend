package com.fullstackbootcamp.capstoneBackend.business.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "financial_statements")
public class FinancialStatementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "financial_statement_assessment_id", nullable = true)
    private FinancialStatementAssessmentEntity financialStatementAssessment;

    @Column(name = "statement_id", nullable = true, unique = true)
    private String statementId;

    @Column(name = "statement_period", nullable = true)
    private String statementPeriod;

    @Column(name = "revenue", nullable = true)
    private String revenue;

    @Column(name = "cost_of_goods_sold", nullable = true)
    private String costOfGoodsSold;

    @Column(name = "gross_profit", nullable = true)
    private String grossProfit;

    @Column(name = "operating_expenses", nullable = true)
    private String operatingExpenses;

    @Column(name = "net_income", nullable = true)
    private String netIncome;

    @Column(name = "zakat_amount", nullable = true)
    private String zakatAmount;

    @Column(name = "total_assets", nullable = true)
    private String totalAssets;

    @Column(name = "cash_and_cash_equivalents", nullable = true)
    private String cashAndCashEquivalents;

    @Column(name = "accounts_receivable", nullable = true)
    private String accountsReceivable;

    @Column(name = "ijara_assets", nullable = true)
    private String ijaraAssets;

    @Column(name = "total_liabilities", nullable = true)
    private String totalLiabilities;

    @Column(name = "accounts_payable", nullable = true)
    private String accountsPayable;

    @Column(name = "murabaha_payables", nullable = true)
    private String murabahaPayables;

    @Column(name = "shareholder_equity", nullable = true)
    private String shareholderEquity;

    @Column(name = "operating_cash_flow", nullable = true)
    private String operatingCashFlow;

    @Column(name = "investing_cash_flow", nullable = true)
    private String investingCashFlow;

    @Column(name = "financing_cash_flow", nullable = true)
    private String financingCashFlow;

    @Column(name = "net_cash_flow", nullable = true)
    private String netCashFlow;

    @Column(name = "mudaraba", nullable = true)
    private String mudaraba;

    @Column(name = "islamic_compliance_certification", nullable = true)
    private String islamicComplianceCertification;

    @Column(name = "dividend_payments", nullable = true)
    private String dividendPayments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatementId() {
        return statementId;
    }

    public void setStatementId(String statementId) {
        this.statementId = statementId;
    }

    public String getStatementPeriod() {
        return statementPeriod;
    }

    public void setStatementPeriod(String statementPeriod) {
        this.statementPeriod = statementPeriod;
    }

    public String getRevenue() {
        return revenue;
    }

    public void setRevenue(String revenue) {
        this.revenue = revenue;
    }

    public String getCostOfGoodsSold() {
        return costOfGoodsSold;
    }

    public void setCostOfGoodsSold(String costOfGoodsSold) {
        this.costOfGoodsSold = costOfGoodsSold;
    }

    public String getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(String grossProfit) {
        this.grossProfit = grossProfit;
    }

    public String getOperatingExpenses() {
        return operatingExpenses;
    }

    public void setOperatingExpenses(String operatingExpenses) {
        this.operatingExpenses = operatingExpenses;
    }

    public String getNetIncome() {
        return netIncome;
    }

    public void setNetIncome(String netIncome) {
        this.netIncome = netIncome;
    }

    public String getZakatAmount() {
        return zakatAmount;
    }

    public void setZakatAmount(String zakatAmount) {
        this.zakatAmount = zakatAmount;
    }

    public String getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(String totalAssets) {
        this.totalAssets = totalAssets;
    }

    public String getCashAndCashEquivalents() {
        return cashAndCashEquivalents;
    }

    public void setCashAndCashEquivalents(String cashAndCashEquivalents) {
        this.cashAndCashEquivalents = cashAndCashEquivalents;
    }

    public String getAccountsReceivable() {
        return accountsReceivable;
    }

    public void setAccountsReceivable(String accountsReceivable) {
        this.accountsReceivable = accountsReceivable;
    }

    public String getIjaraAssets() {
        return ijaraAssets;
    }

    public void setIjaraAssets(String ijaraAssets) {
        this.ijaraAssets = ijaraAssets;
    }

    public String getTotalLiabilities() {
        return totalLiabilities;
    }

    public void setTotalLiabilities(String totalLiabilities) {
        this.totalLiabilities = totalLiabilities;
    }

    public String getAccountsPayable() {
        return accountsPayable;
    }

    public void setAccountsPayable(String accountsPayable) {
        this.accountsPayable = accountsPayable;
    }

    public String getMurabahaPayables() {
        return murabahaPayables;
    }

    public void setMurabahaPayables(String murabahaPayables) {
        this.murabahaPayables = murabahaPayables;
    }

    public String getShareholderEquity() {
        return shareholderEquity;
    }

    public void setShareholderEquity(String shareholderEquity) {
        this.shareholderEquity = shareholderEquity;
    }

    public String getOperatingCashFlow() {
        return operatingCashFlow;
    }

    public void setOperatingCashFlow(String operatingCashFlow) {
        this.operatingCashFlow = operatingCashFlow;
    }

    public String getInvestingCashFlow() {
        return investingCashFlow;
    }

    public void setInvestingCashFlow(String investingCashFlow) {
        this.investingCashFlow = investingCashFlow;
    }

    public String getFinancingCashFlow() {
        return financingCashFlow;
    }

    public void setFinancingCashFlow(String financingCashFlow) {
        this.financingCashFlow = financingCashFlow;
    }

    public String getNetCashFlow() {
        return netCashFlow;
    }

    public void setNetCashFlow(String netCashFlow) {
        this.netCashFlow = netCashFlow;
    }

    public String getMudaraba() {
        return mudaraba;
    }

    public void setMudaraba(String mudaraba) {
        this.mudaraba = mudaraba;
    }

    public String getIslamicComplianceCertification() {
        return islamicComplianceCertification;
    }

    public void setIslamicComplianceCertification(String islamicComplianceCertification) {
        this.islamicComplianceCertification = islamicComplianceCertification;
    }

    public String getDividendPayments() {
        return dividendPayments;
    }

    public void setDividendPayments(String dividendPayments) {
        this.dividendPayments = dividendPayments;
    }

    public FinancialStatementAssessmentEntity getFinancialStatementAssessment() {
        return financialStatementAssessment;
    }

    public void setFinancialStatementAssessment(FinancialStatementAssessmentEntity financialStatementAssessment) {
        this.financialStatementAssessment = financialStatementAssessment;
    }
}
