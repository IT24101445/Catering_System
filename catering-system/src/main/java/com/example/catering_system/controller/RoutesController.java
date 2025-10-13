package com.example.catering_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoutesController {

    @GetMapping("/driver/login")
    public String driverLoginRedirect() {
        return "redirect:/delivery/driver/login";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboardRedirect() {
        return "redirect:/finance/dashboard";
    }
}


