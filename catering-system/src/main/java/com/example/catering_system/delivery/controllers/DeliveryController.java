package com.example.catering_system.delivery.controllers;


import com.example.catering_system.delivery.models.Delivery;
import com.example.catering_system.delivery.models.DeliveryRoute;
import com.example.catering_system.delivery.models.Driver;
import com.example.catering_system.delivery.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@CrossOrigin(origins = "*") // Allow frontend access (remove in prod)
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;

    // Get all deliveries by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Delivery>> getDeliveriesByStatus(@PathVariable String status) {
        List<Delivery> deliveries = deliveryService.getDeliveriesByStatus(status);
        return ResponseEntity.ok(deliveries);
    }

    // Get available drivers
    @GetMapping("/drivers/available")
    public ResponseEntity<List<Driver>> getAvailableDrivers() {
        List<Driver> drivers = deliveryService.getAvailableDrivers();
        return ResponseEntity.ok(drivers);
    }

    // Assign driver to delivery
    @PutMapping("/{deliveryId}/assign/{driverId}")
    public ResponseEntity<Boolean> assignDriver(
            @PathVariable Long deliveryId,
            @PathVariable Long driverId) {
        boolean success = deliveryService.assignDriver(deliveryId, driverId);
        return ResponseEntity.ok(success);
    }

    // Start delivery
    @PutMapping("/{deliveryId}/start")
    public ResponseEntity<Boolean> startDelivery(@PathVariable Long deliveryId) {
        boolean success = deliveryService.startDelivery(deliveryId);
        return ResponseEntity.ok(success);
    }

    // Complete delivery
    @PutMapping("/{deliveryId}/complete")
    public ResponseEntity<Boolean> completeDelivery(@PathVariable Long deliveryId) {
        boolean success = deliveryService.completeDelivery(deliveryId);
        return ResponseEntity.ok(success);
    }

    // Save route (mock)
    @PostMapping("/{deliveryId}/route")
    public ResponseEntity<Void> saveRoute(
            @PathVariable Long deliveryId,
            @RequestParam String startPoint,
            @RequestParam String endPoint,
            @RequestParam double distanceKm,
            @RequestParam int estimatedTimeMin) {
        deliveryService.saveRoute(deliveryId, startPoint, endPoint, distanceKm, estimatedTimeMin);
        return ResponseEntity.ok().build();
    }

    // Get route for delivery
    @GetMapping("/{deliveryId}/route")
    public ResponseEntity<DeliveryRoute> getRoute(@PathVariable Long deliveryId) {
        DeliveryRoute route = deliveryService.getRouteByDeliveryId(deliveryId);
        if (route == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(route);
    }
}