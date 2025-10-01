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
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String role,
                               @RequestParam String fullName,
                               @RequestParam String email,
                               @RequestParam(required = false) String phone,
                               @RequestParam(required = false) String addressLine1,
                               @RequestParam(required = false) String addressLine2,
                               @RequestParam(required = false) String city,
                               @RequestParam(required = false) String state,
                               @RequestParam(required = false) String postalCode) {
        // Encrypt password
        String encodedPassword = passwordEncoder.encode(password);

        // Create a new User and save
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setRole(role); // e.g., "USER" or "ADMIN"
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddressLine1(addressLine1);
        user.setAddressLine2(addressLine2);
        user.setCity(city);
        user.setState(state);
        user.setPostalCode(postalCode);

        userService.saveUser(user);  // Save to the database

        return "redirect:/login";  // Redirect to login page after successful registration
    }
}
