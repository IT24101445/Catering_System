package com.example.catering_system.service;

import com.example.catering_system.model.User;
import com.example.catering_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Save a new user or update an existing user
    public void saveUser(User user) {
        userRepository.save(user);  // Saves or updates the user in the database
    }

    // Find a user by their username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);  // Fetch the user by username
    }

    // Check if a user exists by their username
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);  // Check if the user already exists
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
