package com.example.catering_system.event.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingController {
    
    @GetMapping("/event")
    public String landing() {
        return "forward:/index.html";
    }
    
    // Helper entry point distinct from /event/login to avoid collision with LoginController
    @GetMapping("/event/login-entry")
    public String eventLoginEntry() {
        return "redirect:/?next=event-login";
    }

    @GetMapping("/event/home")
    public String home() {
        return "redirect:/event/dashboard";
    }
}
