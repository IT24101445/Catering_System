package com.example.catering_system.controller;

import com.example.catering_system.model.EventBudget;
import com.example.catering_system.service.EventBudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class EventBudgetController {

    @Autowired
    private EventBudgetService eventBudgetService;

    @GetMapping("/event-budgets")
    public String viewEventBudgets(Model model) {
        model.addAttribute("eventBudgets", eventBudgetService.getAllEventBudgets());
        return "event-budget";  // Returns event-budget.html template
    }

    @PostMapping("/event-budget/add")
    public String addEventBudget(EventBudget eventBudget) {
        eventBudgetService.saveEventBudget(eventBudget);
        return "redirect:/event-budgets";  // Redirect back to event budget list
    }
}
