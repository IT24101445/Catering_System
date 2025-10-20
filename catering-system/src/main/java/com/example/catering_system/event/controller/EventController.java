package com.example.catering_system.event.controller;

import com.example.catering_system.event.entity.Event;
import com.example.catering_system.event.entity.Update;
import com.example.catering_system.event.service.EventService;
import com.example.catering_system.event.service.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class EventController {
    @Autowired
    private EventService eventService;
    
    @Autowired
    private UpdateService updateService;
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDateTime.class, new org.springframework.beans.propertyeditors.CustomDateEditor(
            new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm"), true));
    }

    // Dashboard: List of all events (authenticated users only)
    @GetMapping("/event/dashboard")
    public String dashboard(Model model, HttpSession session) {
        // Check if user is logged in
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        if (isLoggedIn == null || !isLoggedIn) {
            return "redirect:/login";
        }
        
        // Get all events
        List<Event> events = eventService.getAllEvents();
        System.out.println("=== Events Retrieved ===");
        System.out.println("Total events: " + events.size());
        for (Event event : events) {
            System.out.println("Event ID: " + event.getId() + 
                             ", Name: " + event.getName() + 
                             ", Venue: " + event.getVenue() + 
                             ", Guests: " + event.getGuestCount() +
                             ", Status: " + event.getStatus());
        }
        System.out.println("========================");
        
        // Get recent updates/notifications (last 10)
        List<Update> recentUpdates = updateService.getAllUpdates();
        if (recentUpdates.size() > 10) {
            recentUpdates = recentUpdates.subList(0, 10);
        }
        System.out.println("=== Recent Updates Retrieved ===");
        System.out.println("Total updates: " + recentUpdates.size());
        for (Update update : recentUpdates) {
            System.out.println("Update ID: " + update.getId() + 
                             ", Description: " + update.getDescription() + 
                             ", Date: " + update.getDate());
        }
        System.out.println("===============================");
        
        // Calculate system statistics
        long totalEvents = events.size();
        long newEvents = events.stream().filter(e -> "NEW".equals(e.getStatus())).count();
        long updatedEvents = events.stream().filter(e -> e.isUpdated()).count();
        long completedEvents = events.stream().filter(e -> "Completed".equals(e.getStatus())).count();
        long totalGuests = events.stream().mapToInt(Event::getGuestCount).sum();
        
        System.out.println("=== System Statistics ===");
        System.out.println("Total Events: " + totalEvents);
        System.out.println("New Events: " + newEvents);
        System.out.println("Updated Events: " + updatedEvents);
        System.out.println("Completed Events: " + completedEvents);
        System.out.println("Total Guests: " + totalGuests);
        System.out.println("=========================");
        
        // Add all data to model
        model.addAttribute("events", events);
        model.addAttribute("recentUpdates", recentUpdates);
        model.addAttribute("totalEvents", totalEvents);
        model.addAttribute("newEvents", newEvents);
        model.addAttribute("updatedEvents", updatedEvents);
        model.addAttribute("completedEvents", completedEvents);
        model.addAttribute("totalGuests", totalGuests);
        model.addAttribute("userEmail", session.getAttribute("userEmail"));
        return "index";
    }
    
    @GetMapping("/event/bookings")
    public String viewBookings(Model model, HttpSession session) {
        // Check if user is logged in
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        if (isLoggedIn == null || !isLoggedIn) {
            return "redirect:/event/login";
        }
        
        model.addAttribute("userEmail", session.getAttribute("userEmail"));
        return "event-bookings";
    }

    // View event details
    @GetMapping("/events/{id}")
    public String viewEvent(@PathVariable Long id, Model model, HttpSession session) {
        // Check if user is logged in
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        if (isLoggedIn == null || !isLoggedIn) {
            return "redirect:/login";
        }
        
        Optional<Event> event = eventService.getEventById(id);
        if (event.isPresent()) {
            model.addAttribute("event", event.get());
            return "event-details";
        }
        return "redirect:/event/dashboard";
    }

    // Form for create/update
    @GetMapping("/events/form")
    public String eventForm(@RequestParam(required = false) Long id, Model model, HttpSession session) {
        // Check if user is logged in
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        if (isLoggedIn == null || !isLoggedIn) {
            return "redirect:/login";
        }
        
        if (id != null) {
            Optional<Event> event = eventService.getEventById(id);
            model.addAttribute("event", event.orElse(new Event()));
        } else {
            model.addAttribute("event", new Event());
        }
        return "event-form";
    }

    // Save (create/update)
    @PostMapping("/events/save")
    public String saveEvent(@ModelAttribute Event event) {
        if (event.getId() == null) {
            // New event - change all existing NEW events to Planned first
            eventService.changeAllNewToPlanned();
            // Set this new event as NEW
            event.setStatus("NEW");
            event.setUpdated(false);
        } else {
            // For existing events, keep their current status or set to "Planned" if null
            if (event.getStatus() == null || event.getStatus().trim().isEmpty()) {
                event.setStatus("Planned");
            }
            event.setUpdated(true);
        }
        eventService.saveEvent(event);
        return "redirect:/event/dashboard";
    }

    // Delete
    @GetMapping("/events/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return "redirect:/event/dashboard";
    }

    // Mark completed
    @GetMapping("/events/complete/{id}")
    public String markCompleted(@PathVariable Long id) {
        eventService.markCompleted(id);
        return "redirect:/events/" + id;
    }

    // Fix existing events without status
    @GetMapping("/events/fix-status")
    public String fixEventStatus() {
        eventService.fixEventStatus();
        return "redirect:/event/dashboard";
    }

    // Change NEW status to Planned
    @GetMapping("/events/mark-planned/{id}")
    public String markNewAsPlanned(@PathVariable Long id) {
        eventService.markNewAsPlanned(id);
        return "redirect:/event/dashboard";
    }
}