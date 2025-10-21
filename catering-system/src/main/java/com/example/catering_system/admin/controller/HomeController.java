package com.example.catering_system.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("adminHomeController")
public class HomeController {

    @GetMapping("/admin")
    public String home() {
        return "redirect:/admin/login"; // Redirect to admin login page
    }

    @GetMapping("/admin/home")
    public String homePage() {
        return "admin/home"; // Returns admin/home.html template
    }
}
