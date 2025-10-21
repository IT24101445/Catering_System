package com.example.catering_system.admin.controller;

import com.example.catering_system.admin.model.EventBudget;
import com.example.catering_system.admin.service.EventBudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Controller
public class EventBudgetController {

    @Autowired
    private EventBudgetService eventBudgetService;

    @GetMapping("/admin/event-budgets")
    public String viewEventBudgets(Model model) {
        model.addAttribute("eventBudgets", eventBudgetService.getAllEventBudgets());
        model.addAttribute("eventBudget", new EventBudget());
        return "admin/event-budget";  // Returns admin/event-budget.html template
    }

    @GetMapping("/admin/event-budget")
    public String viewEventBudget(Model model) {
        return viewEventBudgets(model);
    }

    @PostMapping("/admin/event-budget/add")
    public String addEventBudget(@ModelAttribute EventBudget eventBudget, 
                                RedirectAttributes redirectAttributes) {
        try {
            System.out.println("Received event budget: " + eventBudget.getEventName());
            
            // Validate required fields
            if (eventBudget.getEventName() == null || eventBudget.getEventName().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Event name is required!");
                return "redirect:/admin/event-budgets";
            }
            
            if (eventBudget.getBudgetAmount() <= 0) {
                redirectAttributes.addFlashAttribute("error", "Budget amount must be greater than 0!");
                return "redirect:/admin/event-budgets";
            }
            
            // Set default values if needed
            if (eventBudget.getAmountSpent() == 0) {
                eventBudget.setAmountSpent(0.0);
            }
            
            // Save the event budget
            EventBudget savedBudget = eventBudgetService.saveEventBudget(eventBudget);
            System.out.println("Saved event budget with ID: " + savedBudget.getId());
            
            redirectAttributes.addFlashAttribute("success", "Event budget '" + eventBudget.getEventName() + "' added successfully!");
            
        } catch (Exception e) {
            System.err.println("Error adding event budget: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error adding event budget: " + e.getMessage());
        }
        
        return "redirect:/admin/event-budgets";  // Redirect back to event budget list
    }

    // Alternative mapping for better compatibility
    @PostMapping("/admin/event-budgets/add")
    public String addEventBudgetAlternative(@ModelAttribute EventBudget eventBudget, 
                                          RedirectAttributes redirectAttributes) {
        return addEventBudget(eventBudget, redirectAttributes);
    }

    // Add a mapping for the add form page
    @GetMapping("/admin/event-budget/add")
    public String showAddEventBudgetForm(Model model) {
        model.addAttribute("eventBudgets", eventBudgetService.getAllEventBudgets());
        model.addAttribute("eventBudget", new EventBudget());
        return "admin/event-budget";
    }
}
