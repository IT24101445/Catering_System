package com.example.catering_system.delivery.controllers;


import com.example.catering_system.delivery.dto.DeliverySupervisor.ApiError;
import com.example.catering_system.delivery.dto.DeliverySupervisor.Create;
import com.example.catering_system.delivery.dto.DeliverySupervisor.Response;
import com.example.catering_system.delivery.dto.DeliverySupervisor.Update;
import com.example.catering_system.delivery.models.DeliverySupervisor;
import com.example.catering_system.delivery.service.DeliverySupervisorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/supervisors")
public class DeliverySupervisorController {

    private final DeliverySupervisorService service;

    public DeliverySupervisorController(DeliverySupervisorService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Create req) {
        try {
            DeliverySupervisor s = service.create(req.getEmail(), req.getPassword());
            Response body = toResponse(s);
            return ResponseEntity.created(URI.create("/api/supervisors/" + s.getId())).body(body);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiError.of(ex.getMessage()));
        }
    }

    // READ (one)
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            DeliverySupervisor s = service.get(id);
            return ResponseEntity.ok(toResponse(s));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(404).body(ApiError.of("Supervisor not found"));
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

    // UPDATE (partial: only updates provided fields)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Update req) {
        try {
            DeliverySupervisor s = service.update(id, req.getEmail(), req.getPassword());
            return ResponseEntity.ok(toResponse(s));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(404).body(ApiError.of("Supervisor not found"));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiError.of(ex.getMessage()));
        }
    }
    // LOGIN (expects { "email": "...", "password": "..." } and returns { "id": ..., "email": "..." })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Create req) {
        try {
            DeliverySupervisor s = service.authenticate(req.getEmail(), req.getPassword());
            return ResponseEntity.ok(toResponse(s));
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
            return ResponseEntity.status(404).body(ApiError.of("Supervisor not found"));
        }
    }

    private Response toResponse(DeliverySupervisor s) {
        Response dto = new Response();
        dto.setId(s.getId());
        dto.setEmail(s.getEmail());
        return dto;
    }
}