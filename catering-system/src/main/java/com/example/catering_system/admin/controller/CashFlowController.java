package com.example.catering_system.admin.controller;

import com.example.catering_system.admin.model.CashFlow;
import com.example.catering_system.admin.service.CashFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CashFlowController {

    @Autowired
    private CashFlowService cashFlowService;

    @GetMapping("/admin/cashflow")
    public String viewCashFlow(Model model) {
        model.addAttribute("cashFlows", cashFlowService.getAllCashFlows());
        model.addAttribute("cashFlow", new CashFlow());
        return "admin/cashflow";
    }

    @PostMapping("/cashflow/add")
    public String addCashFlow(@ModelAttribute CashFlow cashFlow, RedirectAttributes redirectAttributes) {
        try {
            if (cashFlow.getType() == null || cashFlow.getType().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Type is required!");
                return "redirect:/admin/cashflow";
            }
            if (cashFlow.getAmount() <= 0) {
                redirectAttributes.addFlashAttribute("error", "Amount must be greater than 0!");
                return "redirect:/admin/cashflow";
            }
            if (cashFlow.getFlowDate() == null) {
                cashFlow.setFlowDate(new java.util.Date());
            }
            
            cashFlowService.saveCashFlow(cashFlow);
            redirectAttributes.addFlashAttribute("success", "Cash flow entry added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding cash flow: " + e.getMessage());
        }
        return "redirect:/admin/cashflow";
    }

    // Endpoint to clear all cash flow data (use with caution)
    @GetMapping("/admin/cashflow/clear-all")
    public String clearAllCashFlow(RedirectAttributes redirectAttributes) {
        try {
            cashFlowService.deleteAllCashFlows();
            redirectAttributes.addFlashAttribute("success", "All cash flow data cleared!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error clearing data: " + e.getMessage());
        }
        return "redirect:/admin/cashflow";
    }
}
