package com.example.catering_system.customer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerDashboardController {

    @GetMapping("/dashboard-customer")
    public String dashboardCustomer(Model model) {
        model.addAttribute("pageTitle", "Customer Dashboard");
        return "dashboard-customer";
    }
}
