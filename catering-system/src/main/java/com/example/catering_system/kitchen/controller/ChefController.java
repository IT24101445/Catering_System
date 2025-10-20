package com.example.catering_system.kitchen.controller;

import com.example.catering_system.kitchen.model.Menu;
import com.example.catering_system.kitchen.model.Schedule;
import com.example.catering_system.kitchen.service.ChefService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
public class ChefController {

    private final ChefService chefService;

    public ChefController(ChefService chefService) {
        this.chefService = chefService;
    }

    // ---------------- Menus (no class-level "/menus"; map each explicitly) ----------------

    // List menus (newest first)
    @GetMapping("/kitchen/menus")
    public String listMenus(HttpSession session, Model model) {
        // Check if user is logged in
        String user = (String) session.getAttribute("USER");
        if (user == null) {
            return "redirect:/kitchen/login";
        }
        try {
            System.out.println("ChefController: Fetching menus...");
            List<Menu> menus = chefService.getAllMenusNewestFirst();
            System.out.println("ChefController: Retrieved " + menus.size() + " menus");
            model.addAttribute("menus", menus);
            
            // Add a message if no menus found
            if (menus.isEmpty()) {
                model.addAttribute("infoMessage", "No menus found. The Menus table may not exist in the database.");
            }
        } catch (Exception e) {
            System.out.println("ChefController: Error fetching menus: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("menus", List.of());
            model.addAttribute("errorMessage", "Error loading menus: " + e.getMessage());
        }
        model.addAttribute("activePage", "menu");
        model.addAttribute("pageTitle", "Menu Management");
        return "kitchen/menus";
    }

    // Create menu (POST)
    @PostMapping("/kitchen/menus")
    public String createMenu(@RequestParam String name,
                             @RequestParam String status,
                             @RequestParam(required = false, defaultValue = "0") Integer eventId,
                             Model model) {
        try {
            System.out.println("ChefController: Creating menu with name=" + name + ", status=" + status + ", eventId=" + eventId);
            int menuId = chefService.createMenu(name, status, eventId);
            System.out.println("ChefController: Menu created successfully with ID: " + menuId);
        } catch (Exception e) {
            System.out.println("ChefController: Error creating menu: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "Failed to create menu: " + e.getMessage());
            // Return to the form with error message
            model.addAttribute("activePage", "menu");
            model.addAttribute("pageTitle", "Menu Management");
            return "kitchen/menus";
        }
        return "redirect:/kitchen/menus";
    }

    // Menu details
    @GetMapping("/kitchen/menus/{id}")
    public String getMenu(@PathVariable int id, Model model) {
        try {
            Menu menu = chefService.getMenuById(id);
            model.addAttribute("menu", menu);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("menu", null);
        }
        model.addAttribute("activePage", "menu");
        model.addAttribute("pageTitle", "Menu Details");
        // Optional guest count
        // try { model.addAttribute("guestCount", chefService.getGuestCountByMenu(id)); } catch (Exception ignore) {}
        return "kitchen/menu-detail";
    }

    // Edit form
    @GetMapping("/kitchen/menus/{id}/edit")
    public String editMenuForm(@PathVariable int id, Model model) {
        try {
            Menu menu = chefService.getMenuById(id);
            model.addAttribute("menu", menu);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("menu", null);
        }
        model.addAttribute("activePage", "menu");
        model.addAttribute("pageTitle", "Edit Menu");
        return "kitchen/menu-edit";
    }

    // Update menu (POST) - FIX: include "/menus" in mapping to match form action
    @PostMapping("/kitchen/menus/{id}/edit")
    public String updateMenu(@PathVariable int id,
                             @RequestParam String name,
                             @RequestParam String status) {
        try {
            chefService.updateMenu(id, name, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/kitchen/menus";
    }

    // Delete menu (POST)
    @PostMapping("/kitchen/menus/{id}/delete")
    public String deleteMenu(@PathVariable int id) {
        try {
            chefService.deleteMenu(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/kitchen/menus";
    }

    // ---------------- Schedules ----------------

    @GetMapping("/kitchen/schedule-list")
    public String scheduleList(Model model) {
        try {
            List<Schedule> schedules = chefService.getSchedules();
            model.addAttribute("schedules", schedules);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("schedules", Collections.emptyList());
        }
        model.addAttribute("activePage", "schedules");
        model.addAttribute("pageTitle", "Schedules");
        return "kitchen/schedule-list";
    }

    // Create form
    @GetMapping("/kitchen/schedule-form")
    public String newScheduleForm(Model model) {
        model.addAttribute("schedule", null); // create mode
        model.addAttribute("actionUrl", "/kitchen/schedule-form");
        model.addAttribute("activePage", "schedules");
        model.addAttribute("pageTitle", "New Schedule");
        return "kitchen/schedule-form";
    }

    // Edit form
    @GetMapping("/kitchen/schedule-form/{id}")
    public String editScheduleForm(@PathVariable int id, Model model) {
        try {
            Schedule s = chefService.getScheduleById(id);
            model.addAttribute("schedule", s);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("schedule", null);
        }
        model.addAttribute("actionUrl", "/kitchen/schedule-form/" + id);
        model.addAttribute("activePage", "schedules");
        model.addAttribute("pageTitle", "Edit Schedule");
        return "kitchen/schedule-form";
    }

    // Create submit
    @PostMapping("/kitchen/schedule-form")
    public String createSchedule(@RequestParam int eventId,
                                 @RequestParam int chefId,
                                 @RequestParam String plan,
                                 @RequestParam(required = false, defaultValue = "draft") String status,
                                 Model model) {
        try {
            System.out.println("ChefController: Creating schedule with eventId=" + eventId + ", chefId=" + chefId + ", plan=" + plan + ", status=" + status);
            boolean success = chefService.saveSchedule(eventId, chefId, plan, status);
            System.out.println("ChefController: Schedule creation result: " + success);
            
            if (success) {
                return "redirect:/kitchen/schedule-list";
            } else {
                model.addAttribute("error", "Failed to create schedule. The ChefSchedules table may not exist in the database.");
                model.addAttribute("schedule", null);
                model.addAttribute("actionUrl", "/kitchen/schedule-form");
                model.addAttribute("activePage", "schedules");
                model.addAttribute("pageTitle", "New Schedule");
                return "kitchen/schedule-form";
            }
        } catch (Exception e) {
            System.out.println("ChefController: Exception during schedule creation: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Failed to create schedule: " + e.getMessage());
            model.addAttribute("schedule", null);
            model.addAttribute("actionUrl", "/kitchen/schedule-form");
            model.addAttribute("activePage", "schedules");
            model.addAttribute("pageTitle", "New Schedule");
            return "kitchen/schedule-form";
        }
    }

    // Update submit
    @PostMapping("/kitchen/schedule-form/{id}")
    public String updateSchedule(@PathVariable int id,
                                 @RequestParam int eventId,
                                 @RequestParam int chefId,
                                 @RequestParam String plan,
                                 @RequestParam(required = false, defaultValue = "draft") String status,
                                 Model model) {
        try {
            chefService.updateSchedule(id, eventId, chefId, plan, status);
            return "redirect:/kitchen/schedule-list";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to update schedule");
            try { model.addAttribute("schedule", chefService.getScheduleById(id)); } catch (Exception ignore) {}
            model.addAttribute("actionUrl", "/kitchen/schedule-form/" + id);
            model.addAttribute("activePage", "schedules");
            model.addAttribute("pageTitle", "Edit Schedule");
            return "kitchen/schedule-form";
        }
    }

    // Delete schedule (POST) - optional if used in list page
    @PostMapping("/kitchen/schedule-delete/{id}")
    public String deleteSchedule(@PathVariable int id) {
        try {
            chefService.deleteSchedule(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/schedule-list";
    }
}
