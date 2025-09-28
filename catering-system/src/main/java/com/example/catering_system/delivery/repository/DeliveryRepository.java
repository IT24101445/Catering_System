package com.example.catering_system.delivery.repository;



import com.example.catering_system.delivery.models.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByStatus(String status);
    List<Delivery> findByDriverId(Long driverId);
}
