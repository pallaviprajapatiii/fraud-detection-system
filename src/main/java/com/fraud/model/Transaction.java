package com.fraud.model;

import java.time.LocalDateTime;

public class Transaction {
    private int id;
    private int accountId;
    private double amount;
    private LocalDateTime transactionTime;
    private String status; // e.g. "PENDING", "APPROVED", "FLAGGED"

    public Transaction() {
    }

    public Transaction(int id, int accountId, double amount, LocalDateTime transactionTime, String status) {
        this.id = id;
        this.accountId = accountId;
        this.amount = amount;
        this.transactionTime = transactionTime;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}