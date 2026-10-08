CREATE DATABASE IF NOT EXISTS fraud_detection;
USE fraud_detection;

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100),
                       email VARCHAR(100),
                       password VARCHAR(100),
                       role VARCHAR(20)
);

CREATE TABLE accounts (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          user_id INT,
                          balance DECIMAL(12,2),
                          FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE transactions (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              account_id INT,
                              amount DECIMAL(12,2),
                              transaction_time DATETIME,
                              status VARCHAR(20),
                              FOREIGN KEY (account_id) REFERENCES accounts(id)
);

CREATE TABLE fraud_alerts (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              transaction_id INT,
                              reason VARCHAR(255),
                              risk_score INT,
                              reviewed BOOLEAN DEFAULT FALSE,
                              FOREIGN KEY (transaction_id) REFERENCES transactions(id)
);

-- test data so the login works
INSERT INTO users (name, email, password, role) VALUES ('Test User', 'test@test.com', 'pass123', 'CUSTOMER');
INSERT INTO users (name, email, password, role) VALUES ('Admin', 'admin@test.com', 'admin123', 'ADMIN');
INSERT INTO accounts (user_id, balance) SELECT id, 50000 FROM users WHERE email = 'test@test.com';