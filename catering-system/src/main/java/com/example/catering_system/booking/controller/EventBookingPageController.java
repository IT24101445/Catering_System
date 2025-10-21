package com.example.catering_system.booking.controller;

import com.example.catering_system.booking.service.EventBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/booking")
public class EventBookingPageController {

    @Autowired
    private EventBookingService bookingService;

    @GetMapping("/{id}")
    public String viewBooking(@PathVariable Long id, Model model) {
        try {
            var booking = bookingService.getBookingById(id);
            model.addAttribute("booking", booking);
            model.addAttribute("canManage", false); // customers see read-only view
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "booking-details";
    }

    @PostMapping("/{id}/cancel")
    public String cancelBooking(@PathVariable Long id) {
        bookingService.updateBookingStatus(id, com.example.catering_system.booking.entity.EventBooking.BookingStatus.CANCELLED);
        return "redirect:/booking/" + id;
    }

    @PostMapping("/{id}/approve")
    public String approveBooking(@PathVariable Long id) {
        bookingService.updateBookingStatus(id, com.example.catering_system.booking.entity.EventBooking.BookingStatus.CONFIRMED);
        return "redirect:/booking/" + id;
    }
}


