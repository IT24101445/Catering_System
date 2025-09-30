package com.example.catering_system.controller;

import com.example.catering_system.model.User;
import com.example.catering_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Show the registration form
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register"; // Returns register.html template
    }

    // Handle registration form submission
    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password, @RequestParam String role) {
        // Encrypt password
        String encodedPassword = passwordEncoder.encode(password);

        // Create a new User and save
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setRole(role); // e.g., "USER" or "ADMIN"

        userService.saveUser(user);  // Save to the database

        return "redirect:/login";  // Redirect to login page after successful registration
    }
}
