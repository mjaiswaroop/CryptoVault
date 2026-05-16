
CREATE DATABASE IF NOT EXISTS cryptovault_db;
USE cryptovault_db;


CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS portfolios (
    portfolio_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS assets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    portfolio_id INT NOT NULL,
    symbol VARCHAR(10) NOT NULL, -- e.g., BTC, ETH
    name VARCHAR(50) NOT NULL,   -- e.g., Bitcoin
    quantity DECIMAL(18, 8) NOT NULL,
    average_purchase_price DECIMAL(18, 2) NOT NULL,
    asset_type VARCHAR(20) DEFAULT 'CRYPTO',
    
    
    FOREIGN KEY (portfolio_id) REFERENCES portfolios(portfolio_id) ON DELETE CASCADE,
    
    UNIQUE KEY portfolio_coin_unique (portfolio_id, symbol)
);