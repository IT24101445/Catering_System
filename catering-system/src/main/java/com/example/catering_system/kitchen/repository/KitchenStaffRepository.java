package com.example.catering_system.kitchen.repository;

import com.example.catering_system.kitchen.entity.KitchenStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface KitchenStaffRepository extends JpaRepository<KitchenStaff, Long> {
    
    Optional<KitchenStaff> findByEmailAndPasswordAndIsActiveTrue(String email, String password);
    
    Optional<KitchenStaff> findByEmailAndIsActiveTrue(String email);
    
    boolean existsByEmail(String email);
    
    Optional<KitchenStaff> findByIdAndIsActiveTrue(Long id);
}
