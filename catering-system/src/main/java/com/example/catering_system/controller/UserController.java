package com.example.catering_system.controller;

import com.example.catering_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // Example for showing the user profile or any other user-related logic
    @GetMapping("/profile")
    public String showUserProfile() {
        // Logic to retrieve user profile (e.g., based on logged-in user)
        // Using the userService to demonstrate it's being used
        userService.userExists("example"); // This shows the service is being used
        return "user-profile"; // Returns user-profile.html
    }
}
