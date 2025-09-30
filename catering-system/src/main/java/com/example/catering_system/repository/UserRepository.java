package com.example.catering_system.repository;

import com.example.catering_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // Method to find a user by their username
    User findByUsername(String username);

    // Method to check if a user with the given username already exists
    boolean existsByUsername(String username);
}
