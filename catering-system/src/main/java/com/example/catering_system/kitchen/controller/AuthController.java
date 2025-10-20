package com.example.catering_system.kitchen.controller;

import com.example.catering_system.kitchen.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller("kitchenAuthController")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(@Qualifier("kitchenAuthService") AuthService authService) {
        this.authService = authService;
    }

    // Login page
    @GetMapping("/kitchen/login")
    public String loginPage(@RequestParam(value = "logout", required = false) String logout,
                            @RequestParam(value = "next", required = false) String next,
                            Model model) {
        if (logout != null) {
            model.addAttribute("message", "Logged out successfully");
        }
        if (next != null && !next.isBlank()) {
            model.addAttribute("next", next);
        }
        return "kitchen/login"; // POINTS TO templates/kitchen/login.html
    }

    // Handle login
    @PostMapping("/kitchen/login")
    @ResponseBody
    public Map<String, Object> loginSubmit(@RequestParam String email,
                              @RequestParam String password,
                              @RequestParam(value = "next", required = false) String next,
                              HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String e = email == null ? "" : email.trim();
        String p = password == null ? "" : password;

        System.out.println("AuthController: Attempting login for email: " + e);
        boolean validUser = authService.authenticate(e, p);
        System.out.println("AuthController: Login result: " + validUser);
        
        if (validUser) {
            session.setAttribute("USER", e);
            System.out.println("AuthController: Login successful, returning JSON response");
            response.put("success", true);
            response.put("message", "Login successful");
            if (next != null && !next.isBlank() && next.startsWith("/") && !next.startsWith("//")) {
                response.put("redirectUrl", next);
            } else {
                response.put("redirectUrl", "/kitchen/home");
            }
            return response;
        } else {
            System.out.println("AuthController: Login failed, showing error");
            response.put("success", false);
            response.put("message", "Invalid email or password");
            return response;
        }
    }

    // Registration page
    @GetMapping("/kitchen/register")
    public String registerPage(Model model) {
        return "kitchen/register";
    }

    // Handle registration
    @PostMapping("/kitchen/register")
    @ResponseBody
    public Map<String, Object> registerSubmit(@RequestParam String firstName,
                                 @RequestParam String lastName,
                                 @RequestParam String email,
                                 @RequestParam String password,
                                 @RequestParam String position) {
        Map<String, Object> response = new HashMap<>();
        String fName = firstName == null ? "" : firstName.trim();
        String lName = lastName == null ? "" : lastName.trim();
        String e = email == null ? "" : email.trim();
        String p = password == null ? "" : password;
        String pos = position == null ? "Kitchen Staff" : position.trim();

        // Validation
        if (fName.isEmpty() || lName.isEmpty() || e.isEmpty() || p.isEmpty()) {
            response.put("success", false);
            response.put("message", "All fields are required");
            return response;
        }

        if (p.length() < 6) {
            response.put("success", false);
            response.put("message", "Password must be at least 6 characters long");
            return response;
        }

        if (!e.contains("@")) {
            response.put("success", false);
            response.put("message", "Please enter a valid email address");
            return response;
        }

        try {
            System.out.println("AuthController: Attempting registration for email: " + e);
            // Use email as username for authentication
            boolean success = authService.registerUser(e, p, "KITCHEN_STAFF");
            System.out.println("AuthController: Registration result: " + success);
            if (success) {
                System.out.println("AuthController: Registration successful, returning JSON response");
                response.put("success", true);
                response.put("message", "Registration successful! Please login.");
                response.put("redirectUrl", "/kitchen/login");
                return response;
            } else {
                System.out.println("AuthController: Registration failed, showing error");
                response.put("success", false);
                response.put("message", "Email already exists. Please use a different email address or try logging in.");
                return response;
            }
        } catch (Exception ex) {
            System.out.println("AuthController: Registration failed with exception:");
            ex.printStackTrace();
            response.put("success", false);
            response.put("message", "Registration failed. Please try again.");
            return response;
        }
    }

    // Logout
    @PostMapping("/kitchen/logout")
    public String logout(HttpSession session) {
        try { session.invalidate(); } catch (Exception ignored) {}
        return "redirect:/login-portal.html";
    }
}
