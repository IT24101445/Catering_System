package com.example.catering_system.delivery.repository;



import com.example.catering_system.delivery.models.DeliverySupervisor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliverySupervisorRepository extends JpaRepository<DeliverySupervisor, Long> {
    Optional<DeliverySupervisor> findByEmail(String email);
    boolean existsByEmail(String email);
}