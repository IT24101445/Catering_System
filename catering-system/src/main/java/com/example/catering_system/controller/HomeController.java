package com.example.catering_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Redirects to the /menu page
    @GetMapping("/")
    public String home() {
        return "redirect:/menu";  // Redirects to the /menu route
    }

    // Optional: /home page that renders the home template
    @GetMapping("/home")
    public String homePage() {
        return "home";  // Ensure this matches your home.html template name
    }
}
