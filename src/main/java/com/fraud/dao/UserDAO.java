package com.fraud.dao;

import com.fraud.model.Admin;
import com.fraud.model.Customer;
import com.fraud.model.User;
import com.fraud.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // checks the email and password
    // gives back the user if correct, otherwise gives back null
    public User login(String email, String password) {

        User user = null;

        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String role = rs.getString("role");

                // make an Admin or a Customer depending on the role
                if (role.equals("ADMIN")) {
                    user = new Admin(id, name, email, password);
                } else {
                    user = new Customer(id, name, email, password);
                }
            }

            conn.close();

        } catch (SQLException e) {
            System.out.println("login error: " + e.getMessage());
        }

        return user;
    }
}