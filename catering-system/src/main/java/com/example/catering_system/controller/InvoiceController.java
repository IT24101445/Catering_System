package com.example.catering_system.controller;

import com.example.catering_system.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/invoices")
    public String viewInvoices() {
        return "invoice";  // Returns invoice.html
    }


    @PostMapping("/invoice/mark-paid")
    public String markInvoiceAsPaid(Long invoiceId) {
        // Logic to mark invoice as paid in the database
        invoiceService.markPaid(invoiceId);
        return "redirect:/invoices";  // Redirect back to invoice list
    }
}
