package com.example.catering_system.customer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Simple Test Controller to verify the application is working
 */
@Controller("customerTestPageController")
public class TestController {
    
    @GetMapping("/test")
    public String test(Model model) {
        model.addAttribute("message", "Hello! The application is working!");
        model.addAttribute("timestamp", java.time.LocalDateTime.now());
        return "test";
    }
    
}
