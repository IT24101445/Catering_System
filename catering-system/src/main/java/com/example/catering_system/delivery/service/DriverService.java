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
    public Driver create(String email, String name, String status) {
        repo.findByEmail(email).ifPresent(d -> {
            throw new IllegalArgumentException("Driver email already exists: " + email);
        });
        Driver d = new Driver();
        d.setEmail(email);
        d.setName(name);
        d.setStatus(status == null || status.isBlank() ? "AVAILABLE" : status);
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
}