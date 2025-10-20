package com.example.catering_system.kitchen.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {

    // New home page that shows after login
    @GetMapping("/kitchen/home")
    public String homePage(HttpSession session, Model model){
        // Check if user is logged in
        String user = (String) session.getAttribute("USER");
        if (user == null) {
            return "redirect:/kitchen/login";
        }
        
        // Create a simple staff object for the template
        Map<String, Object> kitchenStaff = new HashMap<>();
        kitchenStaff.put("fullName", "Kitchen Staff");
        kitchenStaff.put("position", "Kitchen Staff");
        kitchenStaff.put("email", user);
        
        model.addAttribute("activePage", "home");
        model.addAttribute("pageTitle", "Home | Catering System");
        model.addAttribute("kitchenStaff", kitchenStaff);
        // No layout usage here; home.html is a full page
        return "kitchen/home";
    }

    // Menus entry (alias to ChefController list)
    @GetMapping("/kitchen/menu")
    public String menu(Model model) {
        model.addAttribute("pageTitle", "Menu | Catering System");
        model.addAttribute("activePage", "menu");
        return "redirect:/kitchen/menus"; // route handled by ChefController -> layouts/menus.html
    }

    // Schedules entry (alias)
    @GetMapping("/kitchen/orders")
    public String orders(Model model) {
        model.addAttribute("pageTitle", "Schedules | Catering System");
        model.addAttribute("activePage", "schedules");
        return "redirect:/kitchen/schedule-list"; // route handled by ChefController -> layouts/schedule-list.html
    }
}

