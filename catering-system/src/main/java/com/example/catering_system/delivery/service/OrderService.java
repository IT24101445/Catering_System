package com.example.catering_system.delivery.service;



import com.example.catering_system.delivery.repository.OrderRepository;
import com.example.catering_system.delivery.models.Order;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service("deliveryOrderService")
public class OrderService {

    private final OrderRepository repo;

    public OrderService(@Qualifier("deliveryOrderRepository") OrderRepository repo) {
        this.repo = repo;
    }

    // CREATE
    @Transactional
    public Order create(String customerName, String pickupAddress, String dropoffAddress) {
        Order o = new Order();
        o.setCustomerName(customerName);
        o.setPickupAddress(pickupAddress);
        o.setDropoffAddress(dropoffAddress);
        return repo.save(o);
    }

    // READ (one)
    @Transactional(readOnly = true)
    public Order get(Long id) {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Order not found"));
    }

    // READ (all)
    @Transactional(readOnly = true)
    public List<Order> list() {
        return repo.findAll();
    }

    // UPDATE (partial)
    @Transactional
    public Order update(Long id, String customerName, String pickupAddress, String dropoffAddress) {
        Order o = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Order not found"));

        if (customerName != null && !customerName.isBlank()) {
            o.setCustomerName(customerName);
        }
        if (pickupAddress != null && !pickupAddress.isBlank()) {
            o.setPickupAddress(pickupAddress);
        }
        if (dropoffAddress != null && !dropoffAddress.isBlank()) {
            o.setDropoffAddress(dropoffAddress);
        }
        return repo.save(o);
    }

    // DELETE
    @Transactional
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NoSuchElementException("Order not found");
        }
        repo.deleteById(id);
    }
}
