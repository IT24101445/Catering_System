package com.example.catering_system.admin.controller;

import com.example.catering_system.admin.model.StaffPayment;
import com.example.catering_system.admin.service.StaffPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/staff-payments")
public class StaffPaymentController {

    @Autowired
    private StaffPaymentService staffPaymentService;

    // Display all staff payments
    @GetMapping
    public String showStaffPayments(Model model) {
        model.addAttribute("staffPayments", staffPaymentService.getAllStaffPayments());
        return "admin/staff-payments";  // View the staff payment list
    }

    // Show the form for adding a new payment
    @GetMapping("/add")
    public String showAddPaymentForm(Model model) {
        model.addAttribute("staffPayment", new StaffPayment());
        return "admin/add-staff-payment";  // Form for adding payment
    }

    // Handle the form submission for a new staff payment
    @PostMapping("/add")
    public String addStaffPayment(@ModelAttribute StaffPayment staffPayment) {
        staffPaymentService.addStaffPayment(staffPayment);
        return "redirect:/staff-payments";  // Redirect to the list of staff payments
    }

    // Show the form to edit an existing staff payment
    @GetMapping("/edit/{id}")
    public String showEditPaymentForm(@PathVariable Long id, Model model) {
        model.addAttribute("staffPayment", staffPaymentService.getStaffPaymentById(id).orElse(null));
        return "admin/edite-staff-payment";  // Form for editing payment
    }

    // Handle the form submission for updating an existing staff payment
    @PostMapping("/edit/{id}")
    public String updateStaffPayment(@PathVariable Long id, @ModelAttribute StaffPayment staffPayment) {
        staffPaymentService.updateStaffPayment(id, staffPayment);
        return "redirect:/staff-payments";  // Redirect to the list of staff payments
    }

    // Handle deleting a staff payment
    @GetMapping("/delete/{id}")
    public String deleteStaffPayment(@PathVariable Long id) {
        staffPaymentService.deleteStaffPayment(id);
        return "redirect:/staff-payments";  // Redirect to the list of staff payments
    }

    // API endpoint to get all staff payments as JSON
    @GetMapping("/api")
    @ResponseBody
    public java.util.List<StaffPayment> getAllStaffPaymentsApi() {
        return staffPaymentService.getAllStaffPayments();
    }

    // Endpoint to clear all staff payment data (use with caution)
    @GetMapping("/staff-payments/clear-all")
    public String clearAllStaffPayments(RedirectAttributes redirectAttributes) {
        try {
            staffPaymentService.deleteAllStaffPayments();
            redirectAttributes.addFlashAttribute("success", "All staff payment data cleared!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error clearing data: " + e.getMessage());
        }
        return "redirect:/staff-payments";
    }
}
