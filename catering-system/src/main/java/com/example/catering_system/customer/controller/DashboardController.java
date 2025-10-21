package com.example.catering_system.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.catering_system.customer.service.CustomerService;
import com.example.catering_system.customer.service.MenuItemService;
import com.example.catering_system.customer.service.OrderService;

/**
 * Dashboard Controller for the main application dashboard
 */
@Controller
public class DashboardController {
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private MenuItemService menuItemService;
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping("/customer/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        // Get current user info
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("currentUser", authentication.getName());
        }
        
        // Get dashboard statistics
        try {
            long totalCustomers = customerService.getTotalCustomers();
            long totalMenuItems = menuItemService.getTotalMenuItems();
            long activeOrders = orderService.getActiveOrdersCount();
            
            model.addAttribute("totalCustomers", totalCustomers);
            model.addAttribute("totalMenuItems", totalMenuItems);
            model.addAttribute("activeOrders", activeOrders);
            
            // Calculate monthly revenue (placeholder for now - should be implemented in service)
            double monthlyRevenue = calculateMonthlyRevenue();
            model.addAttribute("monthlyRevenue", monthlyRevenue);
        } catch (Exception e) {
            // If there's an error getting stats, use default values
            model.addAttribute("totalCustomers", 0);
            model.addAttribute("totalMenuItems", 0);
            model.addAttribute("activeOrders", 0);
            model.addAttribute("monthlyRevenue", 0.00);
        }
        
        return "dashboard-customer";
    }
    
    /**
     * Calculate monthly revenue for the current month
     * This is a placeholder implementation - should be replaced with actual service call
     */
    private double calculateMonthlyRevenue() {
        try {
            // TODO: Implement actual monthly revenue calculation from orderService
            // For now, return a placeholder value
            return 12500.00; // Placeholder monthly revenue
        } catch (Exception e) {
            return 0.00;
        }
    }
}
