package com.example.catering_system.kitchen.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;
    
    @Column(name = "Username", nullable = false, length = 100)
    private String username;
    
    @Column(name = "Password", nullable = false, length = 255)
    private String password; // In real apps, store hashed passwords
    
    @Column(name = "Role", nullable = false, length = 50)
    private String role;     // Optional: "ADMIN", "CHEF", etc.

    public User() {
        this.username = "";
        this.password = "";
        this.role = "USER";
    }

    public User(int id, String username, String password) {
        this(id, username, password, "USER");
    }

    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = (role == null || role.isBlank()) ? "USER" : role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = (username == null) ? "" : username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = (password == null) ? "" : password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = (role == null || role.isBlank()) ? "USER" : role;
    }

    // Convenience
    public boolean isAdmin() { return "ADMIN".equalsIgnoreCase(role); }
}
