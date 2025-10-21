package com.example.catering_system.event.controller;

import com.example.catering_system.booking.entity.EventBooking;
import com.example.catering_system.booking.service.EventBookingService;
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
    
    @Autowired
    private EventBookingService eventBookingService;
    
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

    // Print single event details with assigned resources
    @GetMapping("/events/print/{id}")
    public String printEvent(@PathVariable Long id, Model model, HttpSession session) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        if (isLoggedIn == null || !isLoggedIn) {
            return "redirect:/login";
        }
        Optional<Event> eventOpt = eventService.getEventById(id);
        if (eventOpt.isEmpty()) {
            return "redirect:/event/dashboard";
        }
        model.addAttribute("event", eventOpt.get());
        return "event-print";
    }

    // Generate Events Report (similar to admin report)
    @GetMapping("/event/report")
    public String generateEventReport(Model model, HttpSession session) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        if (isLoggedIn == null || !isLoggedIn) {
            return "redirect:/login";
        }

        List<Event> events = eventService.getAllEvents();
        long totalEvents = events.size();
        long completedEvents = events.stream().filter(e -> "Completed".equals(e.getStatus())).count();
        long plannedEvents = events.stream().filter(e -> "Planned".equals(e.getStatus())).count();
        long newEvents = events.stream().filter(e -> "NEW".equals(e.getStatus())).count();
        long inProgressEvents = events.stream().filter(e -> "In Progress".equalsIgnoreCase(e.getStatus())).count();
        int totalGuests = events.stream().mapToInt(Event::getGuestCount).sum();

        model.addAttribute("reportType", "Events");
        model.addAttribute("reportDate", java.time.LocalDate.now().toString());
        model.addAttribute("events", events);
        model.addAttribute("totalEvents", totalEvents);
        model.addAttribute("completedEvents", completedEvents);
        model.addAttribute("plannedEvents", plannedEvents);
        model.addAttribute("newEvents", newEvents);
        model.addAttribute("inProgressEvents", inProgressEvents);
        model.addAttribute("totalGuests", totalGuests);

        return "event-report";
    }

    // Event: Confirmed bookings view
    @GetMapping("/event/confirmed-bookings")
    public String eventConfirmedBookings(Model model, HttpSession session) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        if (isLoggedIn == null || !isLoggedIn) {
            return "redirect:/login";
        }
        try {
            java.util.List<EventBooking> confirmed = eventBookingService.getConfirmedBookings();
            model.addAttribute("bookings", confirmed);
            model.addAttribute("canManage", true); // event module can approve/cancel
        } catch (Exception e) {
            model.addAttribute("error", "Error loading confirmed bookings: " + e.getMessage());
            model.addAttribute("bookings", java.util.Collections.emptyList());
        }
        return "event-confirmed";
    }
    
    // Event Calendar Page
    @GetMapping("/event/calendar")
    public String eventCalendar(Model model, HttpSession session) {
        // Check if user is logged in
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        if (isLoggedIn == null || !isLoggedIn) {
            return "redirect:/login";
        }
        
        // Get all events
        List<Event> events = eventService.getAllEvents();
        
        // Calculate statistics
        long totalEvents = events.size();
        long newEvents = events.stream().filter(e -> "NEW".equals(e.getStatus())).count();
        long updatedEvents = events.stream().filter(e -> e.isUpdated()).count();
        long completedEvents = events.stream().filter(e -> "Completed".equals(e.getStatus())).count();
        
        // Get upcoming events (next 30 days)
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.LocalDate thirtyDaysFromNow = today.plusDays(30);
        
        List<Event> upcomingEvents = events.stream()
            .filter(e -> e.getDateTime() != null && 
                        e.getDateTime().toLocalDate().isAfter(today) && 
                        e.getDateTime().toLocalDate().isBefore(thirtyDaysFromNow))
            .sorted((e1, e2) -> e1.getDateTime().compareTo(e2.getDateTime()))
            .collect(java.util.stream.Collectors.toList());
        
        // Get events for this month
        List<Event> thisMonthEvents = events.stream()
            .filter(e -> e.getDateTime() != null && 
                        e.getDateTime().toLocalDate().getMonth() == today.getMonth() &&
                        e.getDateTime().toLocalDate().getYear() == today.getYear())
            .collect(java.util.stream.Collectors.toList());
        
        // Get events for next month
        java.time.LocalDate nextMonth = today.plusMonths(1);
        List<Event> nextMonthEvents = events.stream()
            .filter(e -> e.getDateTime() != null && 
                        e.getDateTime().toLocalDate().getMonth() == nextMonth.getMonth() &&
                        e.getDateTime().toLocalDate().getYear() == nextMonth.getYear())
            .collect(java.util.stream.Collectors.toList());
        
        // Get events for this quarter
        int currentQuarter = (today.getMonthValue() - 1) / 3 + 1;
        List<Event> quarterEvents = events.stream()
            .filter(e -> e.getDateTime() != null) 
            .filter(e -> {
                int eventQuarter = (e.getDateTime().toLocalDate().getMonthValue() - 1) / 3 + 1;
                return eventQuarter == currentQuarter && 
                       e.getDateTime().toLocalDate().getYear() == today.getYear();
            })
            .collect(java.util.stream.Collectors.toList());
        
        // Set user email for display
        String userEmail = (String) session.getAttribute("username");
        if (userEmail == null) {
            userEmail = "User";
        }
        
        // Add all data to model
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("totalEvents", totalEvents);
        model.addAttribute("newEvents", newEvents);
        model.addAttribute("updatedEvents", updatedEvents);
        model.addAttribute("completedEvents", completedEvents);
        model.addAttribute("upcomingEvents", upcomingEvents);
        model.addAttribute("thisMonthEvents", thisMonthEvents);
        model.addAttribute("nextMonthEvents", nextMonthEvents);
        model.addAttribute("quarterEvents", quarterEvents);
        
        return "event-calendar";
    }
}