package com.example.catering_system.event.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 * Controller for Event Notification management
 */
@Controller
@RequestMapping("/notifications")
public class EventNotificationController {
    
    @Autowired
    private EventNotificationService notificationService;
    
    /**
     * Show all notifications dashboard
     */
    @GetMapping("/dashboard")
    public String notificationsDashboard(Model model, HttpSession session, Authentication authentication) {
        // Check if user is logged in (check for various session attributes and Spring Security)
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        String username = (String) session.getAttribute("username");
        Object user = session.getAttribute("user");
        
        // Also check Spring Security authentication
        boolean isSpringSecurityAuthenticated = authentication != null && authentication.isAuthenticated();
        
        if ((isLoggedIn == null || !isLoggedIn) && username == null && user == null && !isSpringSecurityAuthenticated) {
            return "redirect:/login";
        }
        
        // Get all notifications
        List<EventNotification> allNotifications = notificationService.getAllNotifications();
        
        // Get unread counts by type
        Long unreadAdmin = notificationService.countUnreadNotifications("ADMIN");
        Long unreadKitchen = notificationService.countUnreadNotifications("KITCHEN");
        Long unreadDelivery = notificationService.countUnreadNotifications("DELIVERY");
        
        // Get urgent notifications
        List<EventNotification> urgentNotifications = notificationService.getUrgentNotifications();
        
        // Set user email for display
        String userEmail = "User";
        if (username != null) {
            userEmail = username;
        } else if (authentication != null && authentication.isAuthenticated()) {
            userEmail = authentication.getName();
        }
        
        model.addAttribute("notifications", allNotifications);
        model.addAttribute("unreadAdmin", unreadAdmin);
        model.addAttribute("unreadKitchen", unreadKitchen);
        model.addAttribute("unreadDelivery", unreadDelivery);
        model.addAttribute("urgentNotifications", urgentNotifications);
        model.addAttribute("userEmail", userEmail);
        
        return "notification-dashboard";
    }
    
    
    /**
     * Show form to create new admin message
     */
    @GetMapping("/create-message")
    public String createMessageForm(Model model, HttpSession session) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        String username = (String) session.getAttribute("username");
        Object user = session.getAttribute("user");
        
        if ((isLoggedIn == null || !isLoggedIn) && username == null && user == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("notification", new EventNotification());
        return "create-notification";
    }
    
    /**
     * Create new admin message
     */
    @PostMapping("/create-message")
    public String createMessage(@RequestParam String message,
                              @RequestParam String recipientType,
                              @RequestParam(required = false) String recipientName,
                              @RequestParam Long eventId,
                              @RequestParam String priority) {
        
        notificationService.createAdminMessage(message, recipientType, recipientName, eventId, priority);
        return "redirect:/notifications/dashboard";
    }
    
    /**
     * Mark notification as read
     */
    @GetMapping("/mark-read/{id}")
    public String markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return "redirect:/notifications/dashboard";
    }
    
    /**
     * Add response to notification
     */
    @PostMapping("/respond/{id}")
    public String addResponse(@PathVariable Long id, @RequestParam String response) {
        notificationService.addResponse(id, response);
        return "redirect:/notifications/dashboard";
    }
    
    /**
     * Show event details for a specific event
     */
    @GetMapping("/event/{eventId}")
    public String viewEvent(@PathVariable Long eventId, Model model, HttpSession session) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        String username = (String) session.getAttribute("username");
        Object user = session.getAttribute("user");
        
        if ((isLoggedIn == null || !isLoggedIn) && username == null && user == null) {
            return "redirect:/login";
        }
        
        // Get event details and related notifications
        List<EventNotification> eventNotifications = notificationService.getNotificationsByEvent(eventId);
        model.addAttribute("notifications", eventNotifications);
        model.addAttribute("eventId", eventId);
        
        return "event-notifications";
    }
    
    /**
     * Show kitchen notifications
     */
    @GetMapping("/kitchen")
    public String kitchenNotifications(Model model, HttpSession session) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        String username = (String) session.getAttribute("username");
        Object user = session.getAttribute("user");
        
        if ((isLoggedIn == null || !isLoggedIn) && username == null && user == null) {
            return "redirect:/login";
        }
        
        List<EventNotification> notifications = notificationService.getUnreadNotifications("KITCHEN");
        model.addAttribute("notifications", notifications);
        
        return "kitchen-notifications";
    }
    
    /**
     * Show delivery notifications
     */
    @GetMapping("/delivery")
    public String deliveryNotifications(Model model, HttpSession session) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        String username = (String) session.getAttribute("username");
        Object user = session.getAttribute("user");
        String supervisor = (String) session.getAttribute("supervisor");
        
        // Check for various authentication methods
        if ((isLoggedIn == null || !isLoggedIn) && username == null && user == null && supervisor == null) {
            return "redirect:/delivery/supervisor/login";
        }
        
        try {
            List<EventNotification> notifications = notificationService.getUnreadNotifications("DELIVERY");
            
            // Calculate counts
            long urgentCount = notifications != null ? notifications.stream().filter(n -> "URGENT".equals(n.getPriority())).count() : 0;
            long readCount = notifications != null ? notifications.stream().filter(n -> n.getIsRead()).count() : 0;
            
            model.addAttribute("notifications", notifications);
            model.addAttribute("userEmail", username != null ? username : "Delivery Staff");
            model.addAttribute("urgentCount", urgentCount);
            model.addAttribute("readCount", readCount);
            
            return "delivery-notifications";
        } catch (Exception e) {
            System.out.println("Error in delivery notifications: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Error loading delivery notifications: " + e.getMessage());
            return "delivery-notifications";
        }
    }
}
