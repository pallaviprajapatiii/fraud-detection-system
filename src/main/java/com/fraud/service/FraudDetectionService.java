package com.fraud.service;

import com.fraud.dao.FraudAlertDAO;
import com.fraud.model.FraudAlert;
import com.fraud.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class FraudDetectionService {

    // this list holds all the rules we want to check
    private List<FraudRule> ruleList = new ArrayList<FraudRule>();

    private FraudAlertDAO fraudAlertDAO = new FraudAlertDAO();

    // constructor - when we make a new FraudDetectionService, add the rules we want
    public FraudDetectionService() {
        ruleList.add(new HighAmountRule());
        ruleList.add(new VelocityRule());
        System.out.println("fraud service started with " + ruleList.size() + " rules");
    }

    // this method starts checking a transaction on a new thread
    // so the main program does not have to stop and wait
    public void scanTransaction(Transaction transaction) {

        final Transaction txn = transaction;

        Thread t = new Thread(new Runnable() {
            public void run() {
                checkAllRules(txn);
            }
        });

        t.start();

        System.out.println("started checking transaction " + transaction.getId());
    }

    // goes through every rule one by one to see if transaction is suspicious
    private void checkAllRules(Transaction transaction) {

        for (int i = 0; i < ruleList.size(); i++) {

            FraudRule rule = ruleList.get(i);

            boolean isBad = rule.isSuspicious(transaction);

            if (isBad == true) {

                int riskScore = 50;

                FraudAlert alert = new FraudAlert();
                alert.setTransactionId(transaction.getId());
                alert.setReason(rule.getReason());
                alert.setRiskScore(riskScore);
                alert.setReviewed(false);

                fraudAlertDAO.save(alert);

                System.out.println("WARNING - transaction number " + transaction.getId() + " looks suspicious");
                System.out.println("reason: " + rule.getReason());
            }
        }
    }
}