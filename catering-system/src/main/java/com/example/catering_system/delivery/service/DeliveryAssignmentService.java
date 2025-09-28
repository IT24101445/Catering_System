package com.example.catering_system.delivery.service;


import com.example.catering_system.delivery.models.*;
import com.example.catering_system.delivery.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DeliveryAssignmentService {

    private final DeliveryAssignmentRepository repo;
    private final DriverRepository driverRepo;
    private final OrderRepository orderRepo;

    public DeliveryAssignmentService(
            DeliveryAssignmentRepository repo,
            DriverRepository driverRepo,
            OrderRepository orderRepo) {
        this.repo = repo;
        this.driverRepo = driverRepo;
        this.orderRepo = orderRepo;
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
        return repo.save(a);
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

    // Optional filtered reads
    @Transactional(readOnly = true)
    public List<DeliveryAssignment> listByDriver(Long driverId) {
        return repo.findByDriverId(driverId);
    }

    @Transactional(readOnly = true)
    public List<DeliveryAssignment> listByOrder(Long orderId) {
        return repo.findByOrderId(orderId);
    }
}