package com.example.catering_system.delivery.service;



import com.example.catering_system.delivery.models.DeliverySupervisor;
import com.example.catering_system.delivery.repository.DeliverySupervisorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DeliverySupervisorService {

    private final DeliverySupervisorRepository repo;

    public DeliverySupervisorService(DeliverySupervisorRepository repo) {
        this.repo = repo;
    }

    // CREATE
    @Transactional
    public DeliverySupervisor create(String email, String name, String rawPassword) {
        repo.findByEmail(email).ifPresent(s -> {
            throw new IllegalArgumentException("Email already in use: " + email);
        });
        DeliverySupervisor s = new DeliverySupervisor();
        s.setEmail(email);
        s.setName(name);
        s.setPasswordHash(sha256(rawPassword));
        return repo.save(s);
    }

    // READ (one)
    @Transactional(readOnly = true)
    public DeliverySupervisor get(Long id) {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Supervisor not found"));
    }

    // READ (all)
    @Transactional(readOnly = true)
    public List<DeliverySupervisor> list() {
        return repo.findAll();
    }

    // UPDATE (partial: only non-null fields are updated)
    @Transactional
    public DeliverySupervisor update(Long id, String email, String newRawPassword) {
        DeliverySupervisor s = repo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Supervisor not found"));

        if (email != null && !email.isBlank() && !email.equalsIgnoreCase(s.getEmail())) {
            repo.findByEmail(email).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new IllegalArgumentException("Email already in use: " + email);
                }
            });
            s.setEmail(email);
        }
        if (newRawPassword != null && !newRawPassword.isBlank()) {
            s.setPasswordHash(sha256(newRawPassword));
        }
        return repo.save(s);
    }

    public com.example.catering_system.delivery.models.DeliverySupervisor authenticate(String email, String rawPassword) {
        var supervisor = repo.findByEmail(email)
                .orElseThrow(() -> new java.util.NoSuchElementException("Supervisor not found"));

        String candidateHash = sha256(rawPassword);
        if (!candidateHash.equals(supervisor.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return supervisor;
    }

    // DELETE
    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NoSuchElementException("Supervisor not found");
        }
        repo.deleteById(id);
    }

    // Simple hashing for passwords (no extra deps)
    private static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(hash.length * 2);
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}