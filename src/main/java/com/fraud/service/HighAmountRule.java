package com.fraud.service;

import com.fraud.model.Transaction;

public class HighAmountRule implements FraudRule {

    // if the amount is more than this we think its suspicious
    private static final double LIMIT = 10000.0;

    public boolean isSuspicious(Transaction transaction) {

        double amount = transaction.getAmount();

        if (amount > LIMIT) {
            return true;
        } else {
            return false;
        }
    }

    public String getReason() {
        return "Transaction amount is more than " + LIMIT;
    }
}