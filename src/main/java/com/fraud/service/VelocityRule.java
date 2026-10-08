package com.fraud.service;

import com.fraud.model.Transaction;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VelocityRule implements FraudRule {

    private static final int MAX_TRANSACTIONS = 3;
    private static final int WINDOW_MINUTES = 10;

    // keeps track of recent transaction times for each account
    private Map<Integer, List<LocalDateTime>> recentTransactions = new HashMap<Integer, List<LocalDateTime>>();

    // synchronized so only one thread can check this at a time
    public synchronized boolean isSuspicious(Transaction transaction) {

        int accountId = transaction.getAccountId();
        LocalDateTime now = transaction.getTransactionTime();

        // if we have not seen this account before, make a new empty list for it
        if (recentTransactions.containsKey(accountId) == false) {
            recentTransactions.put(accountId, new ArrayList<LocalDateTime>());
        }

        List<LocalDateTime> times = recentTransactions.get(accountId);
        times.add(now);

        // go through the list and only keep times that are within the last WINDOW_MINUTES
        List<LocalDateTime> recentOnly = new ArrayList<LocalDateTime>();

        for (int i = 0; i < times.size(); i++) {
            LocalDateTime t = times.get(i);
            long minutesAgo = ChronoUnit.MINUTES.between(t, now);

            if (minutesAgo <= WINDOW_MINUTES) {
                recentOnly.add(t);
            }
        }

        recentTransactions.put(accountId, recentOnly);

        if (recentOnly.size() > MAX_TRANSACTIONS) {
            return true;
        } else {
            return false;
        }
    }

    public String getReason() {
        return "More than " + MAX_TRANSACTIONS + " transactions within " + WINDOW_MINUTES + " minutes";
    }
}