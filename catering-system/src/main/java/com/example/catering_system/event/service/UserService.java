package com.example.catering_system.event.service;

import com.example.catering_system.event.entity.User;
import com.example.catering_system.event.repository.EventUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private EventUserRepository userRepository;
    
    // Register a new user
    public User registerUser(String email, String password, String firstName, String lastName) {
        // Check if a user already exists
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("User with email " + email + " already exists");
        }
        
        // Create a new user
        User user = new User(email, password, firstName, lastName);
        return userRepository.save(user);
    }
    
    // Authenticate user login
    public Optional<User> authenticateUser(String email, String password) {
        return userRepository.findByEmailAndPasswordAndIsActiveTrue(email, password);
    }
    
    // Get user by email and ID
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmailAndIsActiveTrue(email);
    }
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    // Update user
    public User updateUser(User user) {
        return userRepository.save(user);
    }
    
    // Delete user
    public void deleteUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setIsActive(false);
            userRepository.save(user.get());
        }
    }
    
    // Check if email exists
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
