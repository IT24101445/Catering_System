package com.example.catering_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // Redirect root ("/") to /home
    @GetMapping("/")
    public String homeRedirect() {
        return "forward:/index.html";  // Serve the static welcome page
    }

    // Render the home page
    @GetMapping("/home")
    public String homePage() {
        return "forward:/index.html";  // Align with static landing page
    }
}
