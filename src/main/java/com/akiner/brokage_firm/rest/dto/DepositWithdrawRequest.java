package com.akiner.brokage_firm.rest.dto;

import jakarta.validation.constraints.NotNull;

public class DepositWithdrawRequest {
    @NotNull private Long customerId;
    @NotNull private double amount;
    private String iban;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }
}
