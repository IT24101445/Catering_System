package com.example.catering_system.controller;

import com.example.catering_system.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashboardController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/admin-dashboard")
    public String showAdminDashboard(Model model) {
        // Add the menu items to the model
        model.addAttribute("menuItems", menuService.getAllMenuItems());
        return "admin-dashboard";  // Update this to the actual template name you have (admin-dashboard.html)
    }
}
