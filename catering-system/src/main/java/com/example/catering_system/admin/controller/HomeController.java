package com.example.catering_system.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/login"; // Redirect to login page
    }

    @GetMapping("/home")
    public String homePage() {
        return "admin/home"; // Returns admin/home.html template
    }
}
