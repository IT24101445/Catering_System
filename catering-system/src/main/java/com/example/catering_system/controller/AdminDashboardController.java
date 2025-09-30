package com.example.catering_system.controller;

import com.example.catering_system.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminDashboardController {

    private final MenuService menuService;

    @Autowired
    public AdminDashboardController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/admin-dashboard")
    public String showAdminDashboard(Model model) {
        model.addAttribute("menuItems", menuService.getAllMenuItems());
        return "admin-dashboard";  // Corresponds to admin-dashboard.html in templates folder
    }
}
