package com.example.catering_system.delivery.controllers;


import com.example.catering_system.delivery.dto.DeliverySupervisor.ApiError;
import com.example.catering_system.delivery.dto.Order.Create;
import com.example.catering_system.delivery.dto.Order.Update;
import com.example.catering_system.delivery.dto.Order.Response;
import com.example.catering_system.delivery.models.Order;
import com.example.catering_system.delivery.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Create req) {
        try {
            Order o = service.create(req.getCustomerName(), req.getPickupAddress(), req.getDropoffAddress());
            Response body = toResponse(o);
            return ResponseEntity.created(URI.create("/api/orders/" + o.getId())).body(body);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ApiError.of(ex.getMessage()));
        }
    }

    // READ (one)
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        try {
            Order o = service.get(id);
            return ResponseEntity.ok(toResponse(o));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(404).body(ApiError.of("Order not found"));
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
            Order o = service.update(id, req.getCustomerName(), req.getPickupAddress(), req.getDropoffAddress());
            return ResponseEntity.ok(toResponse(o));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(404).body(ApiError.of("Order not found"));
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
            return ResponseEntity.status(404).body(ApiError.of("Order not found"));
        }
    }

    private Response toResponse(Order o) {
        Response dto = new Response();
        dto.setId(o.getId());
        dto.setCustomerName(o.getCustomerName());
        dto.setPickupAddress(o.getPickupAddress());
        dto.setDropoffAddress(o.getDropoffAddress());
        return dto;
    }
}