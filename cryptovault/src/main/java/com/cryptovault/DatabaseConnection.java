package com.cryptovault;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/cryptovault"
            + "?useSSL=false"
            + "&serverTimezone=UTC"
            + "&allowPublicKeyRetrieval=true";

    private static final String USER = "root";
    
    // ←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←
    // CHANGE THIS to your actual MySQL root password
    private static final String PASSWORD = "Tiger"; 
    // ←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←←

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}