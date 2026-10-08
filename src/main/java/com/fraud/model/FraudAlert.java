package com.fraud.model;

// this class is for storing information about a fraud alert
// a fraud alert gets created when a transaction looks suspicious
public class FraudAlert {

    private int id;
    private int transactionId;
    private String reason;
    private int riskScore;
    private boolean reviewed;

    // empty constructor, in case we want to create the object first
    // and fill in the values later using setters
    public FraudAlert() {
    }

    // constructor with all the values
    public FraudAlert(int id, int transactionId, String reason, int riskScore, boolean reviewed) {
        this.id = id;
        this.transactionId = transactionId;
        this.reason = reason;
        this.riskScore = riskScore;
        this.reviewed = reviewed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(int riskScore) {
        this.riskScore = riskScore;
    }

    // note: normally boolean getters are named like "isReviewed" in Java,
    // but writing "getReviewed" also works fine and is easier to remember
    public boolean getReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    // just a simple method to print out the alert details for testing
    public void printAlert() {
        System.out.println("Alert ID: " + id);
        System.out.println("Transaction ID: " + transactionId);
        System.out.println("Reason: " + reason);
        System.out.println("Risk Score: " + riskScore);
        System.out.println("Reviewed: " + reviewed);
    }
}