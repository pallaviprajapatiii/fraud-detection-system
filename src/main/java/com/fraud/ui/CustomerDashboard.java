package com.fraud.ui;

import com.fraud.dao.AccountDAO;
import com.fraud.dao.TransactionDAO;
import com.fraud.model.Account;
import com.fraud.model.Transaction;
import com.fraud.model.User;
import com.fraud.service.FraudDetectionService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CustomerDashboard extends JFrame {

    private User user;
    private Account account;

    private AccountDAO accountDAO = new AccountDAO();
    private TransactionDAO transactionDAO = new TransactionDAO();

    // we only make ONE fraud service, so the velocity rule can remember old transactions
    private FraudDetectionService fraudService = new FraudDetectionService();

    private JLabel balanceLabel;
    private JTextField amountField;
    private DefaultTableModel tableModel;

    public CustomerDashboard(User user) {

        this.user = user;

        // find the account that belongs to this user
        account = accountDAO.findByUserId(user.getId());

        if (account == null) {
            JOptionPane.showMessageDialog(null, "You do not have an account yet");
            dispose();
            new LoginFrame();
            return;
        }

        setTitle("Customer Dashboard - " + user.getName());
        setSize(600, 450);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        balanceLabel = new JLabel("Balance: " + account.getBalance());
        balanceLabel.setBounds(20, 15, 300, 25);
        add(balanceLabel);

        JLabel amountLabel = new JLabel("Amount to send:");
        amountLabel.setBounds(20, 55, 120, 25);
        add(amountLabel);

        amountField = new JTextField();
        amountField.setBounds(140, 55, 120, 25);
        add(amountField);

        JButton sendButton = new JButton("Send Money");
        sendButton.setBounds(280, 53, 130, 30);
        add(sendButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(470, 15, 90, 30);
        add(logoutButton);

        JLabel historyLabel = new JLabel("My Transactions");
        historyLabel.setBounds(20, 100, 200, 25);
        add(historyLabel);

        String[] columns = {"ID", "Amount", "Time", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 130, 540, 250);
        add(scrollPane);

        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMoney();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginFrame();
            }
        });

        refreshScreen();
        setVisible(true);
    }

    private void sendMoney() {

        double amount = 0;

        // make sure the user typed a real number
        try {
            amount = Double.parseDouble(amountField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please type a number");
            return;
        }

        if (amount <= 0) {
            JOptionPane.showMessageDialog(this, "Amount must be more than 0");
            return;
        }

        // get the newest balance from the database before checking
        account = accountDAO.findById(account.getId());

        if (amount > account.getBalance()) {
            JOptionPane.showMessageDialog(this, "Not enough balance");
            return;
        }

        boolean worked = transactionDAO.processTransaction(account.getId(), amount);

        if (worked == true) {

            // the newest transaction is the last one in the list
            List<Transaction> list = transactionDAO.findByAccountId(account.getId());
            Transaction lastOne = list.get(list.size() - 1);

            // check it for fraud on a background thread
            fraudService.scanTransaction(lastOne);

            JOptionPane.showMessageDialog(this, "Money sent!");
            amountField.setText("");
            refreshScreen();

        } else {
            JOptionPane.showMessageDialog(this, "Transaction failed");
        }
    }

    // updates the balance and the table
    private void refreshScreen() {

        account = accountDAO.findById(account.getId());
        balanceLabel.setText("Balance: " + account.getBalance());

        tableModel.setRowCount(0);

        List<Transaction> list = transactionDAO.findByAccountId(account.getId());

        for (int i = 0; i < list.size(); i++) {
            Transaction t = list.get(i);

            Object[] row = {t.getId(), t.getAmount(), t.getTransactionTime().toString(), t.getStatus()};
            tableModel.addRow(row);
        }
    }
}