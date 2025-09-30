package com.example.catering_system.controller;

import com.example.catering_system.model.Payment;
import com.example.catering_system.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Add to PaymentController.java
    @GetMapping("/payments")
    public String showPaymentForm() {
        return "payment"; // This matches your payment.html
    }

    @PostMapping("/payment/record")
    public String recordPayment(@RequestParam Long invoiceId, @RequestParam double amount,
                                @RequestParam String paymentMethod) {
        Payment payment = new Payment();
        payment.setInvoiceId(invoiceId);
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);

        paymentService.recordPayment(payment);

        return "redirect:/invoices";  // Redirect to invoices page after recording the payment
    }
}
