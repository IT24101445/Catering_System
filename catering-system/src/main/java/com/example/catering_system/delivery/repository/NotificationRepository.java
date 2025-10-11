package com.example.catering_system.delivery.repository;

import com.example.catering_system.delivery.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByDriverIdOrderByCreatedAtDesc(Long driverId);
    List<Notification> findByDriverIdAndIsReadFalseOrderByCreatedAtDesc(Long driverId);
    Long countByDriverIdAndIsReadFalse(Long driverId);
}
