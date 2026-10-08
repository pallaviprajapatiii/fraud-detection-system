package com.fraud.service;

import com.fraud.model.Transaction;

public interface FraudRule {
    boolean isSuspicious(Transaction transaction);
    String getReason();
}