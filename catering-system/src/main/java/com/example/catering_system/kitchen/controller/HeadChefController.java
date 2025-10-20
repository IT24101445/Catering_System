package com.example.catering_system.kitchen.controller;

import com.example.catering_system.kitchen.model.EventChef;
import com.example.catering_system.kitchen.model.Menu;
import com.example.catering_system.kitchen.model.NotificationChef;
import com.example.catering_system.kitchen.model.Schedule;
import com.example.catering_system.kitchen.service.HeadChefService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

@Controller
public class HeadChefController {

    private final HeadChefService headChefService;

    public HeadChefController(HeadChefService headChefService) {
        this.headChefService = headChefService;
    }

    // ------------- Dashboard -------------

    @GetMapping("/head-chef")
    public String dashboard(Model model, HttpSession session) {
        // Check authentication
        String username = (String) session.getAttribute("USER");
        if (username == null || username.trim().isEmpty()) {
            return "redirect:/login?next=/head-chef";
        }

        try {
            HeadChefService.HeadChefDashboardData data = headChefService.getDashboardData();
            model.addAttribute("dashboardData", data);
            model.addAttribute("username", username);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("dashboardData", null);
        }
        model.addAttribute("activePage", "head-chef");
        model.addAttribute("pageTitle", "Head Chef Dashboard");
        return "head-chef-dashboard";
    }

    // Create sample data for testing
    @GetMapping("/head-chef/create-sample-data")
    public String createSampleData(HttpSession session, RedirectAttributes redirect) {
        // Check authentication
        String username = (String) session.getAttribute("USER");
        if (username == null || username.trim().isEmpty()) {
            return "redirect:/login?next=/head-chef";
        }

        try {
            headChefService.createSampleData();
            redirect.addFlashAttribute("message", "Sample data created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirect.addFlashAttribute("error", "Failed to create sample data: " + e.getMessage());
        }
        return "redirect:/head-chef";
    }

    // ------------- Confirmed Menus -------------

    @GetMapping("/head-chef/confirmed-menus")
    public String confirmedMenus(Model model, HttpSession session) {
        // Check authentication
        String username = (String) session.getAttribute("USER");
        if (username == null || username.trim().isEmpty()) {
            return "redirect:/login?next=/head-chef/confirmed-menus";
        }

        try {
            List<Menu> confirmedMenus = headChefService.getConfirmedMenus();
            model.addAttribute("menus", confirmedMenus);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("menus", Collections.emptyList());
        }
        model.addAttribute("activePage", "head-chef");
        model.addAttribute("pageTitle", "Confirmed Menus");
        return "head-chef-confirmed-menus";
    }

    @GetMapping("/head-chef/confirmed-menus/{id}")
    public String confirmedMenuDetail(@PathVariable int id, Model model) {
        try {
            Menu menu = headChefService.getConfirmedMenuById(id);
            if (menu != null) {
                model.addAttribute("menu", menu);
                // Get guest count for this menu
                int guestCount = headChefService.getGuestCountForMenu(id);
                model.addAttribute("guestCount", guestCount);
            } else {
                model.addAttribute("menu", null);
                model.addAttribute("error", "Menu not found or not confirmed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("menu", null);
            model.addAttribute("error", "Error loading menu details");
        }
        model.addAttribute("activePage", "head-chef");
        model.addAttribute("pageTitle", "Menu Details");
        return "head-chef-menu-detail";
    }

    // ------------- Events and Guest Counts -------------

    @GetMapping("/head-chef/events")
    public String events(Model model) {
        try {
            List<EventChef> confirmedEvents = headChefService.getConfirmedEvents();
            model.addAttribute("events", confirmedEvents);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("events", Collections.emptyList());
        }
        model.addAttribute("activePage", "head-chef");
        model.addAttribute("pageTitle", "Events & Guest Counts");
        return "head-chef-events";
    }

    @GetMapping("/head-chef/events/{id}")
    public String eventDetail(@PathVariable int id, Model model) {
        try {
            EventChef event = headChefService.getEventById(id);
            if (event != null) {
                model.addAttribute("event", event);
            } else {
                model.addAttribute("event", null);
                model.addAttribute("error", "Event not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("event", null);
            model.addAttribute("error", "Error loading event details");
        }
        model.addAttribute("activePage", "head-chef");
        model.addAttribute("pageTitle", "Event Details");
        return "head-chef-event-detail";
    }

    // ------------- Delivery Times and Cooking Schedules -------------

    @GetMapping("/head-chef/delivery-times")
    public String deliveryTimes(Model model) {
        try {
            List<EventChef> upcomingEvents = headChefService.getEventsWithDeliveryTimes();
            model.addAttribute("events", upcomingEvents);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("events", Collections.emptyList());
        }
        model.addAttribute("activePage", "head-chef");
        model.addAttribute("pageTitle", "Delivery Times & Cooking Schedules");
        return "head-chef-delivery-times";
    }

    @GetMapping("/head-chef/cooking-schedules")
    public String cookingSchedules(Model model) {
        try {
            List<Schedule> schedules = headChefService.getCookingSchedules();
            model.addAttribute("schedules", schedules);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("schedules", Collections.emptyList());
        }
        model.addAttribute("activePage", "head-chef");
        model.addAttribute("pageTitle", "Cooking Schedules");
        return "head-chef-cooking-schedules";
    }

    // ------------- Notifications -------------

    @GetMapping("/head-chef/notifications")
    public String notifications(Model model) {
        try {
            List<NotificationChef> notifications = headChefService.getAllNotifications();
            int unreadCount = headChefService.getUnreadNotificationCount();
            model.addAttribute("notifications", notifications);
            model.addAttribute("unreadCount", unreadCount);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("notifications", Collections.emptyList());
            model.addAttribute("unreadCount", 0);
        }
        model.addAttribute("activePage", "head-chef");
        model.addAttribute("pageTitle", "Notifications");
        return "head-chef-notifications";
    }

    @GetMapping("/head-chef/notifications/menu-changes")
    public String menuChangeNotifications(Model model) {
        try {
            List<NotificationChef> notifications = headChefService.getMenuChangeNotifications();
            model.addAttribute("notifications", notifications);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("notifications", Collections.emptyList());
        }
        model.addAttribute("activePage", "head-chef");
        model.addAttribute("pageTitle", "Menu Change Notifications");
        return "head-chef-menu-notifications";
    }

    @PostMapping("/head-chef/notifications/{id}/mark-read")
    public String markNotificationAsRead(@PathVariable int id) {
        try {
            headChefService.markNotificationAsRead(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/head-chef/notifications";
    }

    @PostMapping("/head-chef/notifications/mark-all-read")
    public String markAllNotificationsAsRead() {
        try {
            headChefService.markAllNotificationsAsRead();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/head-chef/notifications";
    }

    // ------------- AJAX Endpoints for Real-time Updates -------------

    @GetMapping("/head-chef/api/unread-count")
    @ResponseBody
    public int getUnreadNotificationCount() {
        try {
            return headChefService.getUnreadNotificationCount();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @GetMapping("/head-chef/api/notifications")
    @ResponseBody
    public List<NotificationChef> getUnreadNotifications() {
        try {
            return headChefService.getUnreadNotifications();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
