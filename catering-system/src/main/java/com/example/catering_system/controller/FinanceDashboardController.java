package com.example.catering_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FinanceDashboardController {

    // This method will handle the request to /finance-dashboard
    @GetMapping("/finance-dashboard")
    public String financeDashboard() {
        return "finance-dashboard-page"; // Ensure this matches your template file name
    }
}
