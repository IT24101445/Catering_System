package com.example.catering_system.delivery.service;



import com.example.catering_system.delivery.repository.DriverRepository;
import com.example.catering_system.delivery.models.Driver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DriverService {

    private final DriverRepository repo;

    public DriverService(DriverRepository repo) {
        this.repo = repo;
    }

    // CREATE
    @Transactional
    public Driver create(String email, String name, String status, String password) {
        // Validate required fields
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        
        repo.findByEmail(email).ifPresent(d -> {
            throw new IllegalArgumentException("Driver email already exists: " + email);
        });
        
        Driver d = new Driver();
        d.setEmail(email.trim());
        d.setName(name.trim());
        d.setStatus(status == null || status.isBlank() ? "AVAILABLE" : status);
        d.setPasswordHash(sha256(password.trim()));
        return repo.save(d);
    }

    // READ (one)
    @Transactional(readOnly = true)
    public Driver get(Long id) {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Driver not found"));
    }

    // READ (all)
    @Transactional(readOnly = true)
    public List<Driver> list() {
        return repo.findAll();
    }

    // UPDATE (partial)
    @Transactional
    public Driver update(Long id, String email, String name, String status) {
        Driver d = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Driver not found"));

        if (email != null && !email.isBlank() && !email.equalsIgnoreCase(d.getEmail())) {
            repo.findByEmail(email).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new IllegalArgumentException("Driver email already exists: " + email);
                }
            });
            d.setEmail(email);
        }
        if (name != null && !name.isBlank()) {
            d.setName(name);
        }
        if (status != null && !status.isBlank()) {
            d.setStatus(status);
        }
        return repo.save(d);
    }

    // DELETE
    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NoSuchElementException("Driver not found");
        }
        repo.deleteById(id);
    }

    // Authentication method
    public Driver authenticate(String email, String rawPassword) {
        var driver = repo.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Driver not found"));

        String candidateHash = sha256(rawPassword);
        if (!candidateHash.equals(driver.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return driver;
    }

    // Simple hashing for passwords (no extra deps)
    private static String sha256(String input) {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}