package com.example.catering_system.operationManager.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    // Updated to match application.properties database settings
    public static Connection connect() {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=cateringDB;encrypt=true;trustServerCertificate=true";
        String user = "AD123";
        String password = "finance123";

        Connection con = null;

        try {
            // Load SQL Server driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Connect to DB
            con = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        return con;
    }

    public static Connection getConnection() {
        return connect();
    }
}