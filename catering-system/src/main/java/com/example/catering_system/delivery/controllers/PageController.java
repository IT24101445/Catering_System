package com.example.catering_system.delivery.controllers;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping({"/", "/home"})
    public String home() {
        return "supervisor-login"; // default to login
    }

    @GetMapping("/supervisor/login")
    public String supervisorLogin() {
        return "supervisor-login";
    }

    @GetMapping("/supervisor/dashboard")
    public String dashboard() {
        return "supervisor-dashboard";
    }

    @GetMapping("/drivers")
    public String drivers() {
        return "drivers";
    }

    @GetMapping("/orders")
    public String orders() {
        return "orders";
    }

    @GetMapping("/deliveries")
    public String deliveries() {
        return "deliveries";
    }

    @GetMapping("/assignments")
    public String assignments() {
        return "assignments";
    }
}