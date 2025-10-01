package com.example.catering_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DeliveryDashboardController {

    @GetMapping("/delivery-dashboard")
    public String deliveryDashboard() {
        return "delivery-dashboard"; // Renders delivery-dashboard.html
    }
}
