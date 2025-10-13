package com.example.catering_system.delivery.repository;



import com.example.catering_system.delivery.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("deliveryOrderRepository")
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByCustomerNameIgnoreCase(String customerName);
}
