package com.example.catering_system.event.repository;

import com.example.catering_system.event.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository("eventUserRepository")
public interface EventUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByEmailAndIsActiveTrue(String email);
    Optional<User> findByEmailAndPasswordAndIsActiveTrue(String email, String password);
}
