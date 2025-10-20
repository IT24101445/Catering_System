package com.example.catering_system.customer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.catering_system.customer.model.User;

/**
 * Repository interface for User entity
 */
@Repository("customerUserRepository")
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    List<User> findByIsActiveTrue();
    
    List<User> findByRolesName(String roleName);
    
    @Query("SELECT u FROM CustomerUser u JOIN u.roles r WHERE r.name = :roleName AND u.isActive = true")
    List<User> findActiveUsersByRole(@Param("roleName") String roleName);
    
    boolean existsByEmail(String email);
}
