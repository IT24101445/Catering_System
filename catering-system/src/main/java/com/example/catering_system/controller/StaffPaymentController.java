package com.example.catering_system.controller;

import com.example.catering_system.model.StaffPayment;
import com.example.catering_system.service.StaffPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/staff-payments")
public class StaffPaymentController {

    @Autowired
    private StaffPaymentService staffPaymentService;

    // Display all staff payments
    @GetMapping
    public String showStaffPayments(Model model) {
        model.addAttribute("staffPayments", staffPaymentService.getAllStaffPayments());
        return "staff-payments";  // View the staff payment list
    }

    // Show the form for adding a new payment
    @GetMapping("/add")
    public String showAddPaymentForm(Model model) {
        model.addAttribute("staffPayment", new StaffPayment());
        return "add-staff-payment";  // Form for adding payment
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
        return "edit-staff-payment";  // Form for editing payment
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
}
