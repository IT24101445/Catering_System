package com.example.catering_system.delivery.repository;



import com.example.catering_system.delivery.models.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Driver> findByStatus(String status); // e.g., "AVAILABLE"
    Optional<Driver> findByNameIgnoreCase(String name);
}
