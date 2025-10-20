package com.example.catering_system.event.controller;

import com.example.catering_system.event.entity.Update;
import com.example.catering_system.event.service.EventService;
import com.example.catering_system.event.service.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/updates")
public class UpdateController {
    @Autowired
    private UpdateService updateService;      // Service to handle Update operations
    @Autowired
    private EventService eventService;        // Service to handle Event operations

    // Display list of all updates
    @GetMapping
    public String listUpdates(Model model, HttpSession session) {
        // Check if user is logged in
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        if (isLoggedIn == null || !isLoggedIn) {
            return "redirect:/login";
        }
        
        model.addAttribute("updates", updateService.getAllUpdates());
        return "update-list";
    }

    // Show form for create or edit
    @GetMapping("/form")
    public String updateForm(@RequestParam(required = false) Long id, @RequestParam(required = false) Long eventId, Model model, HttpSession session) {
        // Check if user is logged in
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        if (isLoggedIn == null || !isLoggedIn) {
            return "redirect:/login";
        }
        
        Update update = id != null ? updateService.getUpdateById(id).orElse(new Update()) : new Update();
        update.setDate(LocalDateTime.now());  // Auto-set
        if (eventId != null) {
            eventService.getEventById(eventId).ifPresent(update::setEvent);
        }
        model.addAttribute("update", update);
        model.addAttribute("events", eventService.getAllEvents());
        return "update-form";
    }

    // Save update
    @PostMapping("/save")
    public String saveUpdate(@ModelAttribute Update update) {
        updateService.saveUpdate(update);
        return "redirect:/updates";
    }

    // Delete update by ID
    @GetMapping("/delete/{id}")
    public String deleteUpdate(@PathVariable Long id) {
        updateService.deleteUpdate(id);
        return "redirect:/updates";
    }
}