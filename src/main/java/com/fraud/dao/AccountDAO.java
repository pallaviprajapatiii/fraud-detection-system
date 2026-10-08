package com.fraud.dao;

import com.fraud.model.Account;
import com.fraud.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    // this method adds a new account to database
    public boolean save(Account account) {

        boolean success = false;

        String sql = "INSERT INTO accounts (user_id, balance) VALUES (?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {

            conn = DBConnection.getConnection();

            stmt = conn.prepareStatement(sql);

            int userId = account.getUserId();
            double balance = account.getBalance();

            stmt.setInt(1, userId);
            stmt.setDouble(2, balance);

            stmt.executeUpdate();

            System.out.println("account saved!");

            success = true;

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("something went wrong while saving account");
            System.out.println(e.getMessage());
            success = false;
        }

        return success;
    }

    // this method finds one account using the id
    public Account findById(int id) {

        Account account = null;

        String sql = "SELECT * FROM accounts WHERE id = ?";

        try {

            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next() == true) {

                int accId = rs.getInt("id");
                int userId = rs.getInt("user_id");
                double balance = rs.getDouble("balance");

                account = new Account();
                account.setId(accId);
                account.setUserId(userId);
                account.setBalance(balance);
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("error finding account");
            System.out.println(e);
        }

        return account;
    }

    // this method gets all accounts from database and puts them in a list
    public List<Account> findAll() {

        List<Account> myList = new ArrayList<Account>();

        String sql = "SELECT * FROM accounts";

        try {

            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                int accId = rs.getInt("id");
                int userId = rs.getInt("user_id");
                double balance = rs.getDouble("balance");

                Account a = new Account();
                a.setId(accId);
                a.setUserId(userId);
                a.setBalance(balance);

                myList.add(a);
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("error getting all accounts: " + e);
        }

        return myList;
    }

    // this method changes the balance amount of an account
    public boolean updateBalance(int accountId, double newBalance) {

        boolean didItWork = false;

        String sql = "UPDATE accounts SET balance = ? WHERE id = ?";

        try {

            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setDouble(1, newBalance);
            stmt.setInt(2, accountId);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                didItWork = true;
            } else {
                didItWork = false;
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("error updating balance");
            e.printStackTrace();
        }

        return didItWork;
    }

    // this method deletes an account using its id
    public boolean delete(int id) {

        boolean didItWork = false;

        String sql = "DELETE FROM accounts WHERE id = ?";

        try {

            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                didItWork = true;
            }

            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("could not delete account :(");
        }

        return didItWork;
    }
    // finds the first account that belongs to a user
    public Account findByUserId(int userId) {

        Account account = null;

        String sql = "SELECT * FROM accounts WHERE user_id = ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                account = new Account();
                account.setId(rs.getInt("id"));
                account.setUserId(rs.getInt("user_id"));
                account.setBalance(rs.getDouble("balance"));
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("error finding account by user: " + e.getMessage());
        }

        return account;
    }

}