package com.example.catering_system.kitchen.service;

import com.example.catering_system.kitchen.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;

@Service("kitchenAuthService")
public class AuthService {

    private final DataSource dataSource;

    @Autowired
    public AuthService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean authenticate(String username, String password) {
        String u = (username == null) ? "" : username.trim();
        String p = (password == null) ? "" : password;

        System.out.println("AuthService.authenticate() called with:");
        System.out.println("  Username: " + u);
        System.out.println("  Password: " + (p.isEmpty() ? "[EMPTY]" : "[PROVIDED]"));

        if (u.isEmpty() || p.isEmpty()) {
            System.out.println("AuthService: Empty username or password");
            return false;
        }

        try (Connection conn = dataSource.getConnection()) {
            System.out.println("AuthService: Database connection established");
            UserDAO userDAO = new UserDAO(conn);
            boolean result = userDAO.isValidUser(u, p);
            System.out.println("AuthService: UserDAO.isValidUser() returned: " + result);
            return result;
        } catch (Exception e) {
            System.out.println("AuthService: Authentication failed with exception:");
            e.printStackTrace();
            return false;
        }
    }

    // Register new user
    public boolean registerUser(String username, String password, String role) {
        String u = (username == null) ? "" : username.trim();
        String p = (password == null) ? "" : password;
        String r = (role == null || role.isBlank()) ? "USER" : role.trim();

        System.out.println("AuthService.registerUser() called with:");
        System.out.println("  Username: " + u);
        System.out.println("  Password: " + (p.isEmpty() ? "[EMPTY]" : "[PROVIDED]"));
        System.out.println("  Role: " + r);

        if (u.isEmpty() || p.isEmpty()) {
            System.out.println("Registration failed: Empty username or password");
            return false;
        }

        try (Connection conn = dataSource.getConnection()) {
            System.out.println("Database connection established");
            UserDAO userDAO = new UserDAO(conn);
            boolean result = userDAO.createUser(u, p, r);
            System.out.println("UserDAO.createUser() returned: " + result);
            return result;
        } catch (Exception e) {
            System.out.println("Registration failed with exception:");
            e.printStackTrace();
            return false;
        }
    }

    // Optional: get role after successful login
    public String roleOf(String username) {
        String u = (username == null) ? "" : username.trim();
        if (u.isEmpty()) return null;
        try (Connection conn = dataSource.getConnection()) {
            UserDAO userDAO = new UserDAO(conn);
            return userDAO.getUserRole(u);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
