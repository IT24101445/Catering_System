package com.example.catering_system.delivery.controllers;


import com.example.catering_system.delivery.dto.DeliverySupervisor.ApiError;
import com.example.catering_system.delivery.dto.Delivery.Create;
import com.example.catering_system.delivery.dto.Delivery.Update;
import com.example.catering_system.delivery.dto.Delivery.Response;
import com.example.catering_system.delivery.models.Delivery;
import com.example.catering_system.delivery.service.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryService service;

    public DeliveryController(DeliveryService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Create req) {
        try {
            Delivery d = service.create(req.getPickupAddress(), req.getDropoffAddress(), req.getStatus());
            Response body = toResponse(d);
            return ResponseEntity.created(URI.create("/api/deliveries/" + d.getId())).body(body);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiError.of(ex.getMessage()));
        }
    }

    // READ (one)
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            Delivery d = service.get(id);
            return ResponseEntity.ok(toResponse(d));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(404).body(ApiError.of("Delivery not found"));
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
            Delivery d = service.update(id, req.getPickupAddress(), req.getDropoffAddress(), req.getStatus());
            return ResponseEntity.ok(toResponse(d));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(404).body(ApiError.of("Delivery not found"));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiError.of(ex.getMessage()));
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(404).body(ApiError.of("Delivery not found"));
        }
    }

    private Response toResponse(Delivery d) {
        Response dto = new Response();
        dto.setId(d.getId());
        dto.setPickupAddress(d.getPickupAddress());
        dto.setDropoffAddress(d.getDropoffAddress());
        dto.setStatus(d.getStatus());
        dto.setDirectionsUrl(buildDirectionsUrl(d.getPickupAddress(), d.getDropoffAddress()));
        return dto;
    }

    // Generates a Google Maps Directions link (works without extra APIs).
    // Replace with your preferred provider later if needed.
    private String buildDirectionsUrl(String origin, String destination) {
        if (origin == null || destination == null) return null;
        String o = URLEncoder.encode(origin, StandardCharsets.UTF_8);
        String d = URLEncoder.encode(destination, StandardCharsets.UTF_8);
        return "https://www.google.com/maps/dir/?api=1&origin=" + o + "&destination=" + d;
    }
}