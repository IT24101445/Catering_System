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
public class LoginController {

    @Autowired
    private UserService userService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Show the login form
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Returns login.html template
    }

    // Handle login logic
    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password) {
        User user = userService.findUserByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // Login successful
            return "redirect:/home"; // Redirect to home page
        }
        // Login failed
        return "redirect:/login"; // Redirect to login page with error message
    }
}
