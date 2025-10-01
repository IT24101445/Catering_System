package com.example.catering_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FinanceDashboardController {

    // Existing Finance Dashboard page
    @GetMapping("/finance-dashboard")
    public String financeDashboard() {
        return "finance-dashboard";  // Renders finance-dashboard.html
    }

}
