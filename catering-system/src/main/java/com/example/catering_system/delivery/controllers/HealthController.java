package com.example.catering_system.delivery.controllers;

import com.example.catering_system.delivery.models.Driver;
import com.example.catering_system.delivery.repository.DriverRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    private final DriverRepository driverRepository;

    public HealthController(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @GetMapping("/database")
    public ResponseEntity<?> checkDatabase() {
        try {
            // Try to count drivers to test database connection
            long driverCount = driverRepository.count();
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "UP");
            response.put("database", "Connected");
            response.put("driverCount", driverCount);
            response.put("message", "Database connection successful");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "DOWN");
            response.put("database", "Disconnected");
            response.put("error", e.getMessage());
            response.put("message", "Database connection failed");
            
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/test-driver-creation")
    public ResponseEntity<?> testDriverCreation() {
        try {
            // Try to create a test driver
            Driver testDriver = new Driver();
            testDriver.setEmail("test@example.com");
            testDriver.setName("Test Driver");
            testDriver.setStatus("AVAILABLE");
            testDriver.setPasswordHash("test-hash");
            
            Driver saved = driverRepository.save(testDriver);
            
            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("message", "Driver creation test successful");
            response.put("driverId", saved.getId());
            
            // Clean up test driver
            driverRepository.deleteById(saved.getId());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "FAILED");
            response.put("error", e.getMessage());
            response.put("message", "Driver creation test failed");
            
            return ResponseEntity.status(500).body(response);
        }
    }
}
