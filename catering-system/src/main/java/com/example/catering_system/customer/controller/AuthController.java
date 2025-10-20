package com.example.catering_system.customer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Authentication Controller for login/logout handling
 */
@Controller("customerAuthController")
public class AuthController {
    
    private final com.example.catering_system.customer.service.UserService customerUserService;

    public AuthController(com.example.catering_system.customer.service.UserService customerUserService) {
        this.customerUserService = customerUserService;
    }
    
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                       @RequestParam(value = "logout", required = false) String logout,
                       Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password!");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }
        return "login";
    }

    @GetMapping("/login/customer")
    public String loginCustomer(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                @RequestParam(value = "message", required = false) String message,
                                Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password!");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }
        if (message != null) {
            model.addAttribute("message", message);
        }
        return "login-customer";
    }
    
    @GetMapping("/customer")
    public String customerLanding() {
        return "index-customer";
    }

    @GetMapping("/register/customer")
    public String showCustomerRegister(Model model) {
        model.addAttribute("registerRequest", new com.example.catering_system.customer.model.User());
        return "register-customer";
    }

    @PostMapping("/register/customer")
    public String handleCustomerRegister(@ModelAttribute("registerRequest") com.example.catering_system.customer.model.User request,
                                         Model model) {
        // Use email as username for login compatibility
        if (request.getUsername() == null || request.getUsername().isBlank()) {
            request.setUsername(request.getEmail());
        }
        try {
            customerUserService.createUser(request);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register-customer";
        } catch (Exception ex) {
            model.addAttribute("error", "Registration failed: " + ex.getMessage());
            return "register-customer";
        }
        return "redirect:/login/customer?message=registered";
    }
    
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login/customer?logout";
    }
    
    @GetMapping("/book-event")
    public String bookEvent() {
        return "book-event";
    }
    
    @GetMapping("/login/customer-admin")
    public String loginCustomerAdmin(@RequestParam(value = "error", required = false) String error,
                                     @RequestParam(value = "logout", required = false) String logout,
                                     Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid admin credentials!");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }
        return "login-customer-admin";
    }
}

















