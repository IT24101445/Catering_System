package com.example.catering_system.delivery.service;


import com.example.catering_system.delivery.models.*;
import com.example.catering_system.delivery.repository.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DeliveryAssignmentService {

    private final DeliveryAssignmentRepository repo;
    private final DriverRepository driverRepo;
    private final OrderRepository orderRepo;
    private final NotificationService notificationService;

    public DeliveryAssignmentService(
            DeliveryAssignmentRepository repo,
            DriverRepository driverRepo,
            @Qualifier("deliveryOrderRepository") OrderRepository orderRepo,
            @Lazy NotificationService notificationService) {
        this.repo = repo;
        this.driverRepo = driverRepo;
        this.orderRepo = orderRepo;
        this.notificationService = notificationService;
    }

    // CREATE
    @Transactional
    public DeliveryAssignment create(Long orderId, Long driverId, String route) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
        Driver driver = driverRepo.findById(driverId)
                .orElseThrow(() -> new NoSuchElementException("Driver not found"));

        DeliveryAssignment a = new DeliveryAssignment();
        a.setOrder(order);
        a.setDriver(driver);
        a.setRoute(route);
        DeliveryAssignment saved = repo.save(a);
        
        // Create notification for driver
        notificationService.notifyAssignmentCreated(saved);
        
        return saved;
    }

    // CREATE using names
    @Transactional
    public DeliveryAssignment createByName(String customerName, String driverName, String route) {
        // Validate input parameters
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be null or empty");
        }
        if (driverName == null || driverName.trim().isEmpty()) {
            throw new IllegalArgumentException("Driver name cannot be null or empty");
        }
        if (route == null || route.trim().isEmpty()) {
            throw new IllegalArgumentException("Route cannot be null or empty");
        }

        // Find order by customer name (case insensitive)
        Order order = orderRepo.findByCustomerNameIgnoreCase(customerName.trim())
                .orElseThrow(() -> new NoSuchElementException("Order not found for customer: " + customerName));
        
        // Find driver by name (case insensitive)
        Driver driver = driverRepo.findByNameIgnoreCase(driverName.trim())
                .orElseThrow(() -> new NoSuchElementException("Driver not found: " + driverName));

        // Check if driver is available
        if (!"AVAILABLE".equals(driver.getStatus())) {
            throw new IllegalStateException("Driver " + driverName + " is not available. Current status: " + driver.getStatus());
        }

        // Create assignment
        DeliveryAssignment a = new DeliveryAssignment();
        a.setOrder(order);
        a.setDriver(driver);
        a.setRoute(route.trim());
        a.setStatus("PENDING");
        DeliveryAssignment saved = repo.save(a);
        
        // Create notification for driver
        try {
            notificationService.notifyAssignmentCreated(saved);
        } catch (Exception e) {
            // Log the error but don't fail the assignment creation
            System.err.println("Failed to create notification: " + e.getMessage());
        }
        
        return saved;
    }

    // READ (one)
    @Transactional(readOnly = true)
    public DeliveryAssignment get(Long id) {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Assignment not found"));
    }

    // READ (all)
    @Transactional(readOnly = true)
    public List<DeliveryAssignment> list() {
        return repo.findAll();
    }

    // UPDATE (partial; any of the params can be null to skip)
    @Transactional
    public DeliveryAssignment update(Long assignmentId, Long driverId, Long orderId, String route) {
        DeliveryAssignment a = repo.findById(assignmentId)
                .orElseThrow(() -> new NoSuchElementException("Assignment not found"));

        if (driverId != null) {
            Driver d = driverRepo.findById(driverId)
                    .orElseThrow(() -> new NoSuchElementException("Driver not found"));
            a.setDriver(d);
        }
        if (orderId != null) {
            Order o = orderRepo.findById(orderId)
                    .orElseThrow(() -> new NoSuchElementException("Order not found"));
            a.setOrder(o);
        }
        if (route != null && !route.isBlank()) {
            a.setRoute(route);
        }
        return repo.save(a);
    }

    // DELETE
    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NoSuchElementException("Assignment not found");
        }
        repo.deleteById(id);
    }

    @Transactional
    public DeliveryAssignment startRoute(Long assignmentId) {
        DeliveryAssignment a = get(assignmentId);
        if (!"ACCEPTED".equals(a.getStatus())) {
            throw new IllegalStateException("Assignment must be ACCEPTED to start");
        }
        Driver d = a.getDriver();
        if (d == null) throw new IllegalStateException("Assignment has no driver");
        d.setStatus("ON_ROUTE");
        a.setStatus("IN_PROGRESS");
        driverRepo.save(d);
        return repo.save(a);
    }

    // Complete the route: set driver back to AVAILABLE
    @Transactional
    public DeliveryAssignment completeRoute(Long assignmentId) {
        DeliveryAssignment a = get(assignmentId);
        if (!"IN_PROGRESS".equals(a.getStatus())) {
            throw new IllegalStateException("Assignment must be IN_PROGRESS to complete");
        }
        Driver d = a.getDriver();
        if (d == null) throw new IllegalStateException("Assignment has no driver");
        d.setStatus("AVAILABLE");
        a.setStatus("COMPLETED");
        a.setCompletedAt(java.time.LocalDateTime.now());
        driverRepo.save(d);
        return repo.save(a);
    }

    // Reassign to another driver
    @Transactional
    public DeliveryAssignment reassign(Long assignmentId, Long newDriverId) {
        DeliveryAssignment a = get(assignmentId);
        Driver newDriver = driverRepo.findById(newDriverId)
                .orElseThrow(() -> new NoSuchElementException("New driver not found"));
        a.setDriver(newDriver);
        return repo.save(a);
    }

    // Optional filtered reads
    @Transactional(readOnly = true)
    public List<DeliveryAssignment> listByDriver(Long driverId) {
        return repo.findByDriverId(driverId);
    }

    @Transactional(readOnly = true)
    public List<DeliveryAssignment> listByOrder(Long orderId) {
        return repo.findByOrderId(orderId);
    }

    // Accept assignment
    @Transactional
    public DeliveryAssignment acceptAssignment(Long assignmentId) {
        DeliveryAssignment a = get(assignmentId);
        if (!"PENDING".equals(a.getStatus())) {
            throw new IllegalStateException("Assignment is not in PENDING status");
        }
        a.setStatus("ACCEPTED");
        return repo.save(a);
    }

    // Decline assignment
    @Transactional
    public DeliveryAssignment declineAssignment(Long assignmentId) {
        DeliveryAssignment a = get(assignmentId);
        if (!"PENDING".equals(a.getStatus())) {
            throw new IllegalStateException("Assignment is not in PENDING status");
        }
        a.setStatus("DECLINED");
        return repo.save(a);
    }
}