package com.example.catering_system.customer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Home Controller for serving the main landing page
 */
@Controller
public class CustomerHomeController {
    
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("appName", "Golden Dish Catering");
        model.addAttribute("version", "2.0.0");
        return "home";
    }
}
