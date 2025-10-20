package com.example.catering_system.event.controller;

import com.example.catering_system.event.entity.User;
import com.example.catering_system.event.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/event/login")
    public String loginPage() {
        return "Event-login";
    }

    @PostMapping("/event/login")
    @ResponseBody
    public Map<String, Object> authenticate(@RequestParam String email, 
                                          @RequestParam String password,
                                          HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<User> user = userService.authenticateUser(email, password);
            if (user.isPresent()) {
                // Valid credentials
                session.setAttribute("isLoggedIn", true);
                session.setAttribute("userEmail", email);
                session.setAttribute("userId", user.get().getId());
                session.setAttribute("userName", user.get().getFullName());
                session.setAttribute("loginTime", System.currentTimeMillis());
                
                response.put("success", true);
                response.put("message", "Login successful");
                response.put("redirectUrl", "/event/dashboard");
            } else {
                // Invalid credentials
                response.put("success", false);
                response.put("message", "Invalid email or password");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Login failed. Please try again.");
        }
        
        return response;
    }

    @GetMapping("/event/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/event";
    }

    @GetMapping("/event/register")
    public String registerPage() {
        return "event-register";
    }

    @PostMapping("/event/register")
    @ResponseBody
    public Map<String, Object> register(@RequestParam String firstName,
                                       @RequestParam String lastName,
                                       @RequestParam String email,
                                       @RequestParam String password) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Check if email already exists
            if (userService.emailExists(email)) {
                response.put("success", false);
                response.put("message", "Email already exists. Please use a different email.");
                return response;
            }
            
            // Register new user
            User newUser = userService.registerUser(email, password, firstName, lastName);
            
            response.put("success", true);
            response.put("message", "Registration successful");
            response.put("userId", newUser.getId());
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Registration failed: " + e.getMessage());
        }
        
        return response;
    }

    @GetMapping("/check-auth")
    @ResponseBody
    public Map<String, Object> checkAuth(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        if (isLoggedIn != null && isLoggedIn) {
            response.put("authenticated", true);
            response.put("userEmail", session.getAttribute("userEmail"));
            response.put("userName", session.getAttribute("userName"));
        } else {
            response.put("authenticated", false);
        }
        
        return response;
    }
}
