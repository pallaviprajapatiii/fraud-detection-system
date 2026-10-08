package com.fraud.dao;

import com.fraud.model.FraudAlert;
import com.fraud.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FraudAlertDAO {

    // saves a new alert into the database
    public boolean save(FraudAlert alert) {

        String sql = "INSERT INTO fraud_alerts (transaction_id, reason, risk_score, reviewed) VALUES (?, ?, ?, ?)";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, alert.getTransactionId());
            stmt.setString(2, alert.getReason());
            stmt.setInt(3, alert.getRiskScore());
            stmt.setBoolean(4, alert.getReviewed());

            stmt.executeUpdate();

            System.out.println("alert saved!");

            conn.close();
            return true;

        } catch (SQLException e) {
            System.out.println("could not save alert");
            System.out.println(e.getMessage());
            return false;
        }
    }

    // gets all the alerts from the database
    public List<FraudAlert> findAll() {

        List<FraudAlert> alertList = new ArrayList<FraudAlert>();

        String sql = "SELECT * FROM fraud_alerts";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                FraudAlert a = new FraudAlert();
                a.setId(rs.getInt("id"));
                a.setTransactionId(rs.getInt("transaction_id"));
                a.setReason(rs.getString("reason"));
                a.setRiskScore(rs.getInt("risk_score"));
                a.setReviewed(rs.getBoolean("reviewed"));

                alertList.add(a);
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("error getting alerts: " + e.getMessage());
        }

        return alertList;
    }

    // marks one alert as reviewed
    public boolean markReviewed(int alertId) {

        String sql = "UPDATE fraud_alerts SET reviewed = true WHERE id = ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, alertId);

            int rows = stmt.executeUpdate();

            stmt.close();
            conn.close();

            if (rows > 0) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            System.out.println("error marking alert as reviewed: " + e.getMessage());
            return false;
        }
    }
}