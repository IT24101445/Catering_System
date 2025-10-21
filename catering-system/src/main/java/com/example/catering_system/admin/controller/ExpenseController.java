package com.example.catering_system.admin.controller;

import com.example.catering_system.admin.model.Expense;
import com.example.catering_system.admin.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/admin/expenses")
    public String viewExpenses(Model model) {
        model.addAttribute("expenses", expenseService.getAllExpenses());
        model.addAttribute("expense", new Expense());
        return "admin/expenses";
    }

    @PostMapping("/admin/expenses/add")
    public String addExpense(@ModelAttribute Expense expense, RedirectAttributes redirectAttributes) {
        try {
            if (expense.getCategory() == null || expense.getCategory().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Category is required!");
                return "redirect:/admin/expenses";
            }
            if (expense.getSubcategory() == null || expense.getSubcategory().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Subcategory is required!");
                return "redirect:/admin/expenses";
            }
            if (expense.getAmount() <= 0) {
                redirectAttributes.addFlashAttribute("error", "Amount must be greater than 0!");
                return "redirect:/admin/expenses";
            }
            if (expense.getExpenseDate() == null) {
                expense.setExpenseDate(new java.util.Date());
            }
            if (expense.getStatus() == null || expense.getStatus().trim().isEmpty()) {
                expense.setStatus("PENDING");
            }
            
            expenseService.saveExpense(expense);
            redirectAttributes.addFlashAttribute("success", "Expense added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding expense: " + e.getMessage());
        }
        return "redirect:/admin/expenses";
    }

    // Endpoint to clear all expense data (use with caution)
    @GetMapping("/admin/expenses/clear-all")
    public String clearAllExpenses(RedirectAttributes redirectAttributes) {
        try {
            expenseService.deleteAllExpenses();
            redirectAttributes.addFlashAttribute("success", "All expense data cleared!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error clearing data: " + e.getMessage());
        }
        return "redirect:/admin/expenses";
    }
}
