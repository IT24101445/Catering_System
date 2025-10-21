package com.example.catering_system.admin.controller;

import com.example.catering_system.admin.model.Revenue;
import com.example.catering_system.admin.service.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Controller
public class RevenueController {

    @Autowired
    private RevenueService revenueService;

    @GetMapping("/admin/revenue")
    public String viewRevenue(Model model) {
        model.addAttribute("revenues", revenueService.getAllRevenues());
        model.addAttribute("revenue", new Revenue());
        return "admin/revenue";
    }

    @PostMapping("/revenue/add")
    public String addRevenue(@ModelAttribute Revenue revenue, RedirectAttributes redirectAttributes) {
        try {
            if (revenue.getSource() == null || revenue.getSource().trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Source is required!");
                return "redirect:/admin/revenue";
            }
            if (revenue.getAmount() <= 0) {
                redirectAttributes.addFlashAttribute("error", "Amount must be greater than 0!");
                return "redirect:/admin/revenue";
            }
            if (revenue.getRevenueDate() == null) {
                revenue.setRevenueDate(new java.util.Date());
            }
            if (revenue.getStatus() == null || revenue.getStatus().trim().isEmpty()) {
                revenue.setStatus("RECEIVED");
            }
            
            revenueService.saveRevenue(revenue);
            redirectAttributes.addFlashAttribute("success", "Revenue added successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding revenue: " + e.getMessage());
        }
        return "redirect:/admin/revenue";
    }

    // Endpoint to clear all revenue data (use with caution)
    @GetMapping("/admin/revenue/clear-all")
    public String clearAllRevenue(RedirectAttributes redirectAttributes) {
        try {
            revenueService.deleteAllRevenues();
            redirectAttributes.addFlashAttribute("success", "All revenue data cleared!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error clearing data: " + e.getMessage());
        }
        return "redirect:/admin/revenue";
    }
}
