package com.example.catering_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Redirect root ("/") to /home
    @GetMapping("/")
    public String homeRedirect() {
        return "home";  // Redirect to /home which is the landing page
    }

    // Render the home page
    @GetMapping("/home")
    public String homePage() {
        return "home";  // Ensure this matches the name of the home.html template
    }
}
