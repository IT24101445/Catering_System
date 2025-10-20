package com.example.catering_system.admin.controller;

import com.example.catering_system.admin.model.Payment;
import com.example.catering_system.admin.service.PaymentService;
import com.example.catering_system.admin.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/payments")
    public String showPaymentForm(Model model) {
        model.addAttribute("invoices", invoiceService.getAllInvoices());
        return "admin/payment"; // This matches your admin/payment.html
    }

    @PostMapping("/payment/record")
    public String recordPayment(@RequestParam Long invoiceId, @RequestParam double amount,
                                @RequestParam String paymentMethod, RedirectAttributes redirectAttributes) {
        try {
            // Validate inputs
            if (invoiceId == null) {
                redirectAttributes.addFlashAttribute("error", "Invoice ID is required!");
                return "redirect:/payments";
            }
            if (amount <= 0) {
                redirectAttributes.addFlashAttribute("error", "Amount must be greater than 0!");
                return "redirect:/payments";
            }
            if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Payment method is required!");
                return "redirect:/payments";
            }

            Payment payment = new Payment();
            payment.setInvoiceId(invoiceId);
            payment.setAmount(amount);
            payment.setPaymentMethod(paymentMethod);

            paymentService.recordPayment(payment);
            redirectAttributes.addFlashAttribute("success", "Payment recorded successfully! Invoice status updated to Paid.");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error recording payment: " + e.getMessage());
        }

        return "redirect:/invoices";  // Redirect to invoices page after recording the payment
    }
}
