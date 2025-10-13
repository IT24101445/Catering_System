package com.example.catering_system.repository;

import com.example.catering_system.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository("mainOrderRepository")
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(String status);
    List<Order> findAllByOrderByDeliveryTimeAsc();
}


