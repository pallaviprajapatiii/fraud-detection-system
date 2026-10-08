package com.fraud.ui;

import com.fraud.dao.FraudAlertDAO;
import com.fraud.model.FraudAlert;
import com.fraud.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminDashboard extends JFrame {

    private JTable alertTable;
    private DefaultTableModel tableModel;
    private FraudAlertDAO fraudAlertDAO = new FraudAlertDAO();

    public AdminDashboard(User admin) {

        setTitle("Admin Dashboard - " + admin.getName());
        setSize(700, 400);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel heading = new JLabel("Fraud Alerts");
        heading.setBounds(20, 10, 300, 25);
        add(heading);

        // the table columns
        String[] columns = {"Alert ID", "Transaction ID", "Reason", "Risk Score", "Reviewed"};
        tableModel = new DefaultTableModel(columns, 0);
        alertTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(alertTable);
        scrollPane.setBounds(20, 45, 640, 250);
        add(scrollPane);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBounds(20, 310, 100, 30);
        add(refreshButton);

        JButton reviewedButton = new JButton("Mark as Reviewed");
        reviewedButton.setBounds(140, 310, 160, 30);
        add(reviewedButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(560, 310, 100, 30);
        add(logoutButton);

        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadAlerts();
            }
        });

        reviewedButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                markSelectedAsReviewed();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LoginFrame();
            }
        });

        loadAlerts();
        setVisible(true);
    }

    // gets the alerts from the database and puts them in the table
    private void loadAlerts() {

        tableModel.setRowCount(0); // clear the old rows first

        List<FraudAlert> alerts = fraudAlertDAO.findAll();

        for (int i = 0; i < alerts.size(); i++) {
            FraudAlert a = alerts.get(i);

            Object[] row = {a.getId(), a.getTransactionId(), a.getReason(), a.getRiskScore(), a.getReviewed()};
            tableModel.addRow(row);
        }

        System.out.println("loaded " + alerts.size() + " alerts");
    }

    private void markSelectedAsReviewed() {

        int selectedRow = alertTable.getSelectedRow();

        // -1 means the user did not click any row
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please click on an alert first");
            return;
        }

        int alertId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());

        boolean worked = fraudAlertDAO.markReviewed(alertId);

        if (worked == true) {
            JOptionPane.showMessageDialog(this, "Alert marked as reviewed");
            loadAlerts();
        } else {
            JOptionPane.showMessageDialog(this, "Something went wrong");
        }
    }
}