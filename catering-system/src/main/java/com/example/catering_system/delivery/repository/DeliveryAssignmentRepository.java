package com.example.catering_system.delivery.repository;


import com.example.catering_system.delivery.models.DeliveryAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryAssignmentRepository extends JpaRepository<DeliveryAssignment, Long> {
    List<DeliveryAssignment> findByDriverId(Long driverId);
    List<DeliveryAssignment> findByOrderId(Long orderId);
    boolean existsByOrderId(Long orderId);
}