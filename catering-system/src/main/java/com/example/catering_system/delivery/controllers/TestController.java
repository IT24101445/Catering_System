package com.example.catering_system.delivery.controllers;

import com.example.catering_system.delivery.models.Driver;
import com.example.catering_system.delivery.models.Order;
import com.example.catering_system.delivery.repository.DriverRepository;
import com.example.catering_system.delivery.repository.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final DriverRepository driverRepository;
    private final OrderRepository orderRepository;

    public TestController(DriverRepository driverRepository, OrderRepository orderRepository) {
        this.driverRepository = driverRepository;
        this.orderRepository = orderRepository;
    }

    @GetMapping("/drivers")
    public ResponseEntity<List<Driver>> getAllDrivers() {
        List<Driver> drivers = driverRepository.findAll();
        return ResponseEntity.ok(drivers);
    }

    @GetMapping("/drivers/by-name/{name}")
    public ResponseEntity<?> getDriverByName(@PathVariable String name) {
        return driverRepository.findByNameIgnoreCase(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/by-customer/{customerName}")
    public ResponseEntity<?> getOrderByCustomerName(@PathVariable String customerName) {
        return orderRepository.findByCustomerNameIgnoreCase(customerName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
