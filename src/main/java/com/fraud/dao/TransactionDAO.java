package com.fraud.dao;

import com.fraud.model.Transaction;
import com.fraud.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    // saves a transaction and reduces the account balance
    // if it fails halfway, we cancel everything using rollback
    public boolean processTransaction(int accountId, double amount) {
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // don't save for real yet

            PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO transactions (account_id, amount, transaction_time, status) VALUES (?, ?, ?, ?)");
            insertStmt.setInt(1, accountId);
            insertStmt.setDouble(2, amount);
            insertStmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            insertStmt.setString(4, "APPROVED");
            insertStmt.executeUpdate();

            PreparedStatement updateStmt = conn.prepareStatement(
                    "UPDATE accounts SET balance = balance - ? WHERE id = ?");
            updateStmt.setDouble(1, amount);
            updateStmt.setInt(2, accountId);
            updateStmt.executeUpdate();

            conn.commit(); // both worked, save it for real
            conn.close();
            return true;

        } catch (SQLException e) {
            System.out.println("transaction failed: " + e.getMessage());
            try {
                conn.rollback(); // undo everything above
            } catch (SQLException e2) {
                System.out.println("rollback failed too");
            }
            return false;
        }
    }

    // gets all transactions belonging to one account
    public List<Transaction> findByAccountId(int accountId) {
        List<Transaction> list = new ArrayList<Transaction>();

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM transactions WHERE account_id = ?");
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Transaction t = new Transaction();
                t.setId(rs.getInt("id"));
                t.setAccountId(rs.getInt("account_id"));
                t.setAmount(rs.getDouble("amount"));
                t.setTransactionTime(rs.getTimestamp("transaction_time").toLocalDateTime());
                t.setStatus(rs.getString("status"));
                list.add(t);
            }

            conn.close();

        } catch (SQLException e) {
            System.out.println("error getting transactions: " + e.getMessage());
        }

        return list;
    }

    // gets every transaction in the database
    public List<Transaction> findAll() {
        List<Transaction> list = new ArrayList<Transaction>();

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM transactions");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Transaction t = new Transaction();
                t.setId(rs.getInt("id"));
                t.setAccountId(rs.getInt("account_id"));
                t.setAmount(rs.getDouble("amount"));
                t.setTransactionTime(rs.getTimestamp("transaction_time").toLocalDateTime());
                t.setStatus(rs.getString("status"));
                list.add(t);
            }

            conn.close();

        } catch (SQLException e) {
            System.out.println("error getting transactions: " + e.getMessage());
        }

        return list;
    }
}