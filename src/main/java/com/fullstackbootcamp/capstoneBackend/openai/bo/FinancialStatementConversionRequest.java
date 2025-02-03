package com.fullstackbootcamp.capstoneBackend.openai.bo;

public class FinancialStatementConversionRequest {
    private String financialStatementText;

    public String getFinancialStatementText() {
        return financialStatementText;
    }

    public void setFinancialStatementText(String financialStatementText) {
        this.financialStatementText = financialStatementText;
    }
}
