package com.example.catering_system.kitchen.controller;

import com.example.catering_system.event.service.EventService;
import com.example.catering_system.event.service.UpdateService;
import com.example.catering_system.event.entity.Event;
import com.example.catering_system.event.entity.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/kitchen")
public class KitchenController {
    
    @Autowired
    private EventService eventService;
    
    @Autowired
    private UpdateService updateService;
    
    // Show kitchen dashboard
    @GetMapping("/kitchen/dashboard")
    public String dashboard(HttpSession session, Model model) {
        // Check if user is logged in (using AuthController's session attribute)
        String user = (String) session.getAttribute("USER");
        if (user == null) {
            return "redirect:/kitchen/login";
        }
        
        // Add session messages to model and clear them
        if (session.getAttribute("successMessage") != null) {
            model.addAttribute("successMessage", session.getAttribute("successMessage"));
            session.removeAttribute("successMessage");
        }
        if (session.getAttribute("errorMessage") != null) {
            model.addAttribute("errorMessage", session.getAttribute("errorMessage"));
            session.removeAttribute("errorMessage");
        }
        
        // Get real data from event system
        try {
            // Get all events
            List<Event> allEvents = eventService.getAllEvents();
            System.out.println("KitchenController: Retrieved " + allEvents.size() + " events for dashboard");
            
            // Get recent updates/notifications (last 5 for dashboard)
            List<Update> recentUpdates = updateService.getAllUpdates();
            if (recentUpdates.size() > 5) {
                recentUpdates = recentUpdates.subList(0, 5);
            }
            System.out.println("KitchenController: Retrieved " + recentUpdates.size() + " recent updates for dashboard");
            
            // Calculate kitchen statistics
            long pendingOrders = allEvents.stream().filter(e -> "NEW".equals(e.getStatus())).count();
            long inProgressOrders = allEvents.stream().filter(e -> "Planned".equals(e.getStatus())).count();
            long completedToday = allEvents.stream().filter(e -> "Completed".equals(e.getStatus())).count();
            long urgentOrders = allEvents.stream().filter(e -> e.isUpdated()).count(); // Updated events as urgent
            
            // Get today's events (events happening today)
            List<Event> todaysEvents = allEvents.stream()
                .filter(e -> e.getDateTime() != null && 
                           e.getDateTime().toLocalDate().equals(java.time.LocalDate.now()))
                .collect(java.util.stream.Collectors.toList());
            
            System.out.println("KitchenController: Dashboard statistics - Pending: " + pendingOrders + 
                             ", In Progress: " + inProgressOrders + 
                             ", Completed: " + completedToday + 
                             ", Urgent: " + urgentOrders);
            
            // Add all data to model
            model.addAttribute("pendingOrders", pendingOrders);
            model.addAttribute("inProgressOrders", inProgressOrders);
            model.addAttribute("completedToday", completedToday);
            model.addAttribute("urgentOrders", urgentOrders);
            model.addAttribute("recentUpdates", recentUpdates);
            model.addAttribute("todaysEvents", todaysEvents);
            model.addAttribute("allEvents", allEvents);
            
        } catch (Exception e) {
            System.out.println("KitchenController: Error retrieving dashboard data: " + e.getMessage());
            // Set default values if error occurs
            model.addAttribute("pendingOrders", 0);
            model.addAttribute("inProgressOrders", 0);
            model.addAttribute("completedToday", 0);
            model.addAttribute("urgentOrders", 0);
            model.addAttribute("recentUpdates", List.of());
            model.addAttribute("todaysEvents", List.of());
            model.addAttribute("allEvents", List.of());
        }
        
        // Create a simple staff object for the template
        Map<String, Object> kitchenStaff = new HashMap<>();
        kitchenStaff.put("fullName", "Kitchen Staff"); // Default name
        kitchenStaff.put("position", "Kitchen Staff"); // Default position
        kitchenStaff.put("email", user); // Use the logged-in email
        
        model.addAttribute("kitchenStaff", kitchenStaff);
        return "kitchen/head-chef-dashboard";
    }
    
    // Show kitchen events
    @GetMapping("/events")
    public String events(HttpSession session, Model model) {
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
        
        // Get all events from the main event system
        try {
            List<Event> allEvents = eventService.getAllEvents();
            model.addAttribute("events", allEvents);
            System.out.println("KitchenController: Retrieved " + allEvents.size() + " events from main system");
        } catch (Exception e) {
            System.out.println("KitchenController: Error retrieving events: " + e.getMessage());
            model.addAttribute("events", List.of()); // Empty list if error
        }
        
        model.addAttribute("kitchenStaff", kitchenStaff);
        model.addAttribute("activePage", "events");
        model.addAttribute("pageTitle", "Events | Catering System");
        return "kitchen/head-chef-events";
    }
    
    // Show kitchen notifications
    @GetMapping("/notifications")
    public String notifications(HttpSession session, Model model) {
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
        
        model.addAttribute("kitchenStaff", kitchenStaff);
        model.addAttribute("activePage", "notifications");
        model.addAttribute("pageTitle", "Notifications | Catering System");
        return "kitchen/head-chef-notifications";
    }
    
    // Show individual event details
    @GetMapping("/events/{id}")
    public String eventDetails(@PathVariable Long id, HttpSession session, Model model) {
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
        
        // Get specific event from the main event system
        try {
            Optional<Event> eventOpt = eventService.getEventById(id);
            if (eventOpt.isPresent()) {
                model.addAttribute("event", eventOpt.get());
                System.out.println("KitchenController: Retrieved event " + id + " from main system");
            } else {
                System.out.println("KitchenController: Event " + id + " not found");
                return "redirect:/kitchen/events";
            }
        } catch (Exception e) {
            System.out.println("KitchenController: Error retrieving event " + id + ": " + e.getMessage());
            return "redirect:/kitchen/events";
        }
        
        model.addAttribute("kitchenStaff", kitchenStaff);
        model.addAttribute("activePage", "events");
        model.addAttribute("pageTitle", "Event Details | Catering System");
        return "kitchen/event-details";
    }
}
