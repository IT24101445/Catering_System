package com.example.catering_system.admin.controller;

import com.example.catering_system.admin.service.InvoiceService;
import com.example.catering_system.admin.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;
    
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/invoices")
    public String viewInvoices(Model model) {
        try {
            model.addAttribute("invoices", invoiceService.getAllInvoices());
            model.addAttribute("payments", paymentService.getAllPayments());
        } catch (Exception e) {
            model.addAttribute("error", "Error loading invoices: " + e.getMessage());
            model.addAttribute("invoices", java.util.Collections.emptyList());
            model.addAttribute("payments", java.util.Collections.emptyList());
        }
        return "admin/invoice";  // Returns admin/invoice.html
    }
    
    @PostMapping("/invoices/create-sample")
    public String createSampleInvoices(RedirectAttributes redirectAttributes) {
        try {
            // Create sample invoices if none exist
            if (invoiceService.getAllInvoices().isEmpty()) {
                invoiceService.createInvoice(1L, 500.0, "Catering Service - Wedding");
                invoiceService.createInvoice(2L, 750.0, "Catering Service - Corporate Event");
                invoiceService.createInvoice(3L, 300.0, "Catering Service - Birthday Party");
                redirectAttributes.addFlashAttribute("success", "Sample invoices created successfully!");
            } else {
                redirectAttributes.addFlashAttribute("info", "Invoices already exist.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating sample invoices: " + e.getMessage());
        }
        return "redirect:/invoices";
    }

    @PostMapping("/invoice/mark-paid")
    public String markInvoiceAsPaid(@RequestParam Long invoiceId, RedirectAttributes redirectAttributes) {
        try {
            invoiceService.markAsPaid(invoiceId);
            redirectAttributes.addFlashAttribute("success", "Invoice marked as paid successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error marking invoice as paid: " + e.getMessage());
        }
        return "redirect:/invoices";  // Redirect back to invoice list
    }
}
