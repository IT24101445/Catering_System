package com.example.catering_system.delivery.controllers;

import com.example.catering_system.delivery.dto.DeliverySupervisor.ApiError;
import com.example.catering_system.delivery.models.Order;
import com.example.catering_system.delivery.repository.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/delivery-orders")
public class DeliveryOrderController {

    private final OrderRepository orderRepository;

    public DeliveryOrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PutMapping("/{id}/kitchen-status")
    public ResponseEntity<?> updateKitchenStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Order not found"));
            
            String status = request.get("status");
            if (status == null || status.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(ApiError.of("Status is required"));
            }
            
            order.setKitchenStatus(status);
            if ("COMPLETED".equals(status)) {
                order.setCompletedAt(java.time.LocalDateTime.now());
            }
            
            Order saved = orderRepository.save(order);
            return ResponseEntity.ok(Map.of(
                "id", saved.getId(),
                "kitchenStatus", saved.getKitchenStatus(),
                "completedAt", saved.getCompletedAt()
            ));
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(404).body(ApiError.of("Order not found"));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ApiError.of("Internal server error: " + ex.getMessage()));
        }
    }
}