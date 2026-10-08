package com.fraud.ui;

import com.fraud.dao.UserDAO;
import com.fraud.model.User;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame() {

        setTitle("Fraud Detection System - Login");
        setSize(350, 220);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(30, 30, 80, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(120, 30, 180, 25);
        add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 70, 80, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(120, 70, 180, 25);
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(120, 115, 100, 30);
        add(loginButton);

        // this runs when the login button is clicked
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doLogin();
            }
        });

        setVisible(true);
    }

    private void doLogin() {

        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (email.equals("") || password.equals("")) {
            JOptionPane.showMessageDialog(this, "Please fill in both fields");
            return;
        }

        UserDAO userDAO = new UserDAO();
        User user = userDAO.login(email, password);

        if (user == null) {
            JOptionPane.showMessageDialog(this, "Wrong email or password");
        }
        else {
        JOptionPane.showMessageDialog(this, "Welcome " + user.getName() + " (" + user.getRole() + ")");

        // open the right screen depending on the role
        if (user.getRole().equals("ADMIN")) {
            new AdminDashboard(user);
        } else {
            new CustomerDashboard(user);
        }

        dispose(); // close the login window
    }
    }
}