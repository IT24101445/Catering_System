package com.example.catering_system.delivery.controllers;



import com.example.catering_system.delivery.dto.DeliverySupervisor.ApiError;
import com.example.catering_system.delivery.dto.Driver.Create;
import com.example.catering_system.delivery.dto.Driver.Update;
import com.example.catering_system.delivery.dto.Driver.Response;
import com.example.catering_system.delivery.models.Driver;
import com.example.catering_system.delivery.service.DriverService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService service;

    public DriverController(DriverService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Create req) {
        try {
            System.out.println("Creating driver with data: " + req);
            Driver d = service.create(req.getEmail(), req.getName(), req.getStatus(), req.getPassword());
            Response body = toResponse(d);
            System.out.println("Driver created successfully with ID: " + d.getId());
            return ResponseEntity.created(URI.create("/api/drivers/" + d.getId())).body(body);
        } catch (IllegalArgumentException ex) {
            System.err.println("Driver creation failed: " + ex.getMessage());
            return ResponseEntity.badRequest().body(ApiError.of(ex.getMessage()));
        } catch (Exception ex) {
            System.err.println("Unexpected error creating driver: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(500).body(ApiError.of("Internal server error: " + ex.getMessage()));
        }
    }

    // READ (one)
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            Driver d = service.get(id);
            return ResponseEntity.ok(toResponse(d));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(404).body(ApiError.of("Driver not found"));
        }
    }

    // READ (all)
    @GetMapping
    public ResponseEntity<List<Response>> list() {
        List<Response> list = service.list()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // UPDATE (partial)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Update req) {
        try {
            Driver d = service.update(id, req.getEmail(), req.getName(), req.getStatus());
            return ResponseEntity.ok(toResponse(d));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(404).body(ApiError.of("Driver not found"));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiError.of(ex.getMessage()));
        }
    }

    // LOGIN (expects { "email": "...", "password": "..." } and returns { "id": ..., "email": "...", "name": "..." })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Create req) {
        try {
            Driver d = service.authenticate(req.getEmail(), req.getPassword());
            return ResponseEntity.ok(toResponse(d));
        } catch (NoSuchElementException | IllegalArgumentException ex) {
            return ResponseEntity.status(401).body(ApiError.of("Invalid credentials"));
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(404).body(ApiError.of("Driver not found"));
        }
    }

    private Response toResponse(Driver d) {
        Response dto = new Response();
        dto.setId(d.getId());
        dto.setEmail(d.getEmail());
        dto.setName(d.getName());
        dto.setStatus(d.getStatus());
        return dto;
    }
}