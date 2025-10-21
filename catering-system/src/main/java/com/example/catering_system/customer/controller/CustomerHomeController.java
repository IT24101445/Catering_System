package com.example.catering_system.customer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.catering_system.booking.service.EventBookingService;

/**
 * Home Controller for serving the main landing page
 */
@Controller
public class CustomerHomeController {
    
    @Autowired
    private EventBookingService bookingService;

    @GetMapping("/customer")
    public String home(Model model) {
        model.addAttribute("appName", "Golden Dish Catering");
        model.addAttribute("version", "2.0.0");
        return "home";
    }

    // Fallback root route to serve the same customer home page
    @GetMapping("/")
    public String root(Model model) {
        model.addAttribute("appName", "Golden Dish Catering");
        model.addAttribute("version", "2.0.0");
        return "home";
    }

    @GetMapping("/customer/my-bookings")
    public String myBookings(Model model, HttpSession session, @RequestParam(required = false) String email) {
        String userEmail = email != null ? email : (String) session.getAttribute("userEmail");
        if (userEmail == null || userEmail.isEmpty()) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !("anonymousUser".equals(auth.getPrincipal()))) {
                userEmail = auth.getName();
            }
        }
        if (userEmail == null || userEmail.isEmpty()) {
            model.addAttribute("bookings", java.util.Collections.emptyList());
            model.addAttribute("error", "Please log in to view your bookings.");
            return "customer-my-bookings";
        }
        model.addAttribute("bookings", bookingService.getBookingsByEmail(userEmail));
        model.addAttribute("userEmail", userEmail);
        return "customer-my-bookings";
    }
}
