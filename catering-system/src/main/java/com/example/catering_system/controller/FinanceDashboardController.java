package com.example.catering_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FinanceDashboardController {

    // Existing Finance Dashboard page
    @GetMapping("/finance-dashboard")
    public String financeDashboard() {
        return "finance-Dashboard";  // Redirects to finance-Dashboard.html page
    }

    // New Mapping for the Add Staff Payment page
    @GetMapping("/add-staff-payment")
    public String addStaffPayment() {
        return "add-staff-payment";  // Returns add-staff-payment.html page
    }

    @GetMapping("/staff-payments")
    public String StaffPayment() {
        return "staff-payments";  // Returns add-staff-payment.html page
    }


}
