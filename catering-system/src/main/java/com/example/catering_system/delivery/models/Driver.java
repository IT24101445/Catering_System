package com.example.catering_system.delivery.models;

import jakarta.persistence.*;

@Entity
@Table(name = "drivers", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "driver_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 320)
    private String email;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 40)
    private String status = "AVAILABLE";

    // Store a hash (e.g., SHA256), not plain text
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    public Driver() {
    }

    public Driver(Long id, String email, String name, String status, String passwordHash) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.status = status;
        this.passwordHash = passwordHash;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}