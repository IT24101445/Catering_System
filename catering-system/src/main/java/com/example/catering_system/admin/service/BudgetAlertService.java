package com.example.catering_system.admin.service;

import com.example.catering_system.admin.model.EventBudget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BudgetAlertService {

    @Autowired
    private EventBudgetService eventBudgetService;

    @Autowired
    private ExpenseService expenseService;

    public List<String> getBudgetAlerts() {
        List<String> alerts = new ArrayList<>();
        List<EventBudget> budgets = eventBudgetService.getAllEventBudgets();

        for (EventBudget budget : budgets) {
            // Check if expenses exceed budget
            if (budget.getAmountSpent() > budget.getBudgetAmount()) {
                alerts.add("⚠️ ALERT: " + budget.getEventName() + " has exceeded budget by $" + 
                          String.format("%.2f", (budget.getAmountSpent() - budget.getBudgetAmount())));
            }
            
            // Check if expenses are close to budget (90% threshold)
            double threshold = budget.getBudgetAmount() * 0.9;
            if (budget.getAmountSpent() >= threshold && budget.getAmountSpent() < budget.getBudgetAmount()) {
                alerts.add("⚠️ WARNING: " + budget.getEventName() + " is at " + 
                          String.format("%.1f", (budget.getAmountSpent() / budget.getBudgetAmount()) * 100) + 
                          "% of budget");
            }
        }

        return alerts;
    }

    public boolean isBudgetExceeded(Long budgetId) {
        EventBudget budget = eventBudgetService.getAllEventBudgets().stream()
                .filter(b -> b.getId().equals(budgetId))
                .findFirst()
                .orElse(null);
        
        return budget != null && budget.getAmountSpent() > budget.getBudgetAmount();
    }

    public double getBudgetUtilizationPercentage(Long budgetId) {
        EventBudget budget = eventBudgetService.getAllEventBudgets().stream()
                .filter(b -> b.getId().equals(budgetId))
                .findFirst()
                .orElse(null);
        
        if (budget == null || budget.getBudgetAmount() == 0) {
            return 0.0;
        }
        
        return (budget.getAmountSpent() / budget.getBudgetAmount()) * 100;
    }
}
