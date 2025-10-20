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
    
    @GetMapping("/dashboard")
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
            model.addAttribute("todaysRevenue", 2347.00); // Placeholder for now
        } catch (Exception e) {
            // If there's an error getting stats, use default values
            model.addAttribute("totalCustomers", 0);
            model.addAttribute("totalMenuItems", 0);
            model.addAttribute("activeOrders", 0);
            model.addAttribute("todaysRevenue", 0.00);
        }
        
        return "dashboard";
    }
}
