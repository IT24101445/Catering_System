package com.example.catering_system.event.repository;

import com.example.catering_system.event.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository("unusedEventUserRepository")
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Find the user by email
    Optional<User> findByEmail(String email);
    
    // Check if a user exists by email
    boolean existsByEmail(String email);
    
    // Find an active user by email
    Optional<User> findByEmailAndIsActiveTrue(String email);
    
    // Find the user by email and password (In login)
    Optional<User> findByEmailAndPasswordAndIsActiveTrue(String email, String password);
}
