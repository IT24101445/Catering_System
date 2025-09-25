package com.example.catering_system.delivery.repository;



import com.example.catering_system.delivery.models.DeliveryRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRouteRepository extends JpaRepository<DeliveryRoute, Long> {
    DeliveryRoute findByDeliveryId(Long deliveryId);
}
