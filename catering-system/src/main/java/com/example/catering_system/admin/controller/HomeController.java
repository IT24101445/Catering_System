package com.example.catering_system.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("adminHomeController")
public class HomeController {

    @GetMapping("/admin")
    public String home() {
        return "redirect:/admin/login"; // Redirect to admin login page
    }

    @GetMapping("/admin/home")
    public String homePage(Model model) {
        // Calculate monthly revenue (placeholder for now)
        double monthlyRevenue = calculateMonthlyRevenue();
        model.addAttribute("monthlyRevenue", monthlyRevenue);
        
        return "admin/home"; // Returns admin/home.html template
    }
    
    /**
     * Calculate monthly revenue for the current month
     * This is a placeholder implementation - should be replaced with actual service call
     */
    private double calculateMonthlyRevenue() {
        try {
            // TODO: Implement actual monthly revenue calculation from orderService
            // For now, return a placeholder value
            return 18750.00; // Placeholder monthly revenue for admin
        } catch (Exception e) {
            return 0.00;
        }
    }
}
