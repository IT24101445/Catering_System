package com.example.catering_system.service;

import com.example.catering_system.model.Order;
import com.example.catering_system.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order save(Order order) { return orderRepository.save(order); }

    public List<Order> getPending() { return orderRepository.findByStatus("Pending"); }
    public List<Order> getKitchenPending() {
        return orderRepository.findAll().stream()
                .filter(o -> Boolean.TRUE.equals(o.getPaymentConfirmed()))
                .filter(o -> "Pending".equalsIgnoreCase(o.getStatus()))
                .toList();
    }

    public List<Order> getAll() { return orderRepository.findAll(); }

    public Optional<Order> getById(Long id) { return orderRepository.findById(id); }

    public void markStatus(Long id, String status) {
        orderRepository.findById(id).ifPresent(o -> { o.setStatus(status); orderRepository.save(o); });
    }

    public void confirmPayment(Long id) {
        orderRepository.findById(id).ifPresent(o -> { o.setPaymentConfirmed(true); orderRepository.save(o); });
    }
}


