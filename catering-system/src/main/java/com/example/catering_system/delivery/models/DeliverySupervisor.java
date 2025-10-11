package com.example.catering_system.delivery.models;


import jakarta.persistence.*;

@Entity
@Table(
        name = "delivery_supervisors",
        uniqueConstraints = @UniqueConstraint(columnNames = "email")
)
public class DeliverySupervisor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 320)
    private String email;

    @Column(nullable = false, length = 120)
    private String name;

    // Store a hash (e.g., BCrypt), not plain text
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    public DeliverySupervisor() {
    }

    public DeliverySupervisor(Long id, String email, String name, String passwordHash) {
        this.id = id;
        this.email = email;
        this.name = name;
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
