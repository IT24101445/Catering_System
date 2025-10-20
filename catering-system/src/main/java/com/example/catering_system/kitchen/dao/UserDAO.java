package com.example.catering_system.kitchen.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    private final Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    // Simple credential check (demo). In production, store hashed passwords and verify hashes.
    public boolean isValidUser(String username, String password) throws Exception {
        String u = username == null ? "" : username.trim();
        String p = password == null ? "" : password;

        System.out.println("UserDAO.isValidUser() called with:");
        System.out.println("  Username: " + u);
        System.out.println("  Password: " + (p.isEmpty() ? "[EMPTY]" : "[PROVIDED]"));

        final String sql = "SELECT COUNT(*) FROM Users WHERE username = ? AND password = ?";
        System.out.println("UserDAO: Executing SQL: " + sql);
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u);
            ps.setString(2, p);
            try (ResultSet rs = ps.executeQuery()) {
                boolean valid = rs.next() && rs.getInt(1) > 0;
                System.out.println("UserDAO: Credential check result: " + valid);
                return valid;
            }
        }
    }

    // Create new user
    public boolean createUser(String username, String password, String role) throws Exception {
        String u = username == null ? "" : username.trim();
        String p = password == null ? "" : password;
        String r = (role == null || role.isBlank()) ? "USER" : role.trim();

        System.out.println("UserDAO.createUser() called with:");
        System.out.println("  Username: " + u);
        System.out.println("  Password: " + (p.isEmpty() ? "[EMPTY]" : "[PROVIDED]"));
        System.out.println("  Role: " + r);

        if (u.isEmpty() || p.isEmpty()) {
            System.out.println("UserDAO: Empty username or password");
            return false;
        }

        // Check if user already exists
        boolean exists = userExists(u);
        System.out.println("UserDAO: User exists check: " + exists);
        if (exists) {
            System.out.println("UserDAO: User already exists, returning false");
            return false;
        }

        // Use email as username for kitchen staff
        final String sql = "INSERT INTO Users (Username, Password, Role, Email) VALUES (?, ?, ?, ?)";
        System.out.println("UserDAO: Executing SQL: " + sql);
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u);
            ps.setString(2, p); // In production, hash the password
            ps.setString(3, r);
            ps.setString(4, u); // Use username as email for kitchen staff
            int result = ps.executeUpdate();
            System.out.println("UserDAO: executeUpdate() returned: " + result);
            return result == 1;
        }
    }

    // Check if user exists
    public boolean userExists(String username) throws Exception {
        String u = username == null ? "" : username.trim();
        final String sql = "SELECT COUNT(*) FROM Users WHERE username = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u);
            try (ResultSet rs = ps.executeQuery()) {
                boolean exists = rs.next() && rs.getInt(1) > 0;
                if (exists) {
                    System.out.println("UserDAO: User '" + u + "' already exists in database");
                }
                return exists;
            }
        }
    }

    // Optional: fetch user role after login
    public String getUserRole(String username) throws Exception {
        String u = username == null ? "" : username.trim();
        final String sql = "SELECT role FROM Users WHERE username = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, u);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("role") : null;
            }
        }
    }
}
