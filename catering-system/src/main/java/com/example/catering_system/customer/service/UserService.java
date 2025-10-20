package com.example.catering_system.customer.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.catering_system.customer.model.User;
import com.example.catering_system.customer.repository.UserRepository;

/**
 * Service class for User management
 */
@Service("customerUserService")
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public User createUser(User user) {
        // Use email as the unique identifier for login
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        return userRepository.save(user);
    }
    
    public User updateUser(User user) {
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
    
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setIsActive(false);
        userRepository.save(user);
    }
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> findByUsername(String username) {
        // Backward-compatibility: treat the provided value as email to avoid username column
        return userRepository.findByEmail(username);
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public List<User> findAllActiveUsers() {
        return userRepository.findByIsActiveTrue();
    }
    
    public List<User> findUsersByRole(String roleName) {
        return userRepository.findActiveUsersByRole(roleName);
    }
    
    public User updateLastLogin(String usernameOrEmail) {
        User user = userRepository.findByEmail(usernameOrEmail)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setLastLogin(LocalDateTime.now());
        return userRepository.save(user);
    }
    
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return true;
    }
    
    // Additional methods for user management
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    
    public User updateUserProfile(Long userId, String fullName, String email, String phone) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setUpdatedAt(LocalDateTime.now());
        
        return userRepository.save(user);
    }
    
    public User toggleUserStatus(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        user.setIsActive(!user.getIsActive());
        user.setUpdatedAt(LocalDateTime.now());
        
        return userRepository.save(user);
    }
    
    public long getTotalUsers() {
        return userRepository.count();
    }
    
    public long getActiveUsersCount() {
        return userRepository.findByIsActiveTrue().size();
    }
}
