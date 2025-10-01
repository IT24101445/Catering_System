package com.example.catering_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KitchenDashboardController {

    @GetMapping("/kitchen-dashboard")
    public String kitchenDashboard() {
        return "kitchen-dashboard"; // Renders kitchen-dashboard.html
    }
}
