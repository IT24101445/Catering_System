package com.example.catering_system.admin.service;

import com.example.catering_system.admin.model.Payment;
import com.example.catering_system.admin.model.Invoice;
import com.example.catering_system.admin.repository.PaymentRepository;
import com.example.catering_system.admin.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private InvoiceRepository invoiceRepository;

    public Payment recordPayment(Payment payment) {
        // Set payment date if not provided
        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(new Date());
        }
        
        // Save the payment
        Payment savedPayment = paymentRepository.save(payment);
        
        // Update the related invoice status if invoiceId is provided
        if (payment.getInvoiceId() != null) {
            Optional<Invoice> invoiceOpt = invoiceRepository.findById(payment.getInvoiceId());
            if (invoiceOpt.isPresent()) {
                Invoice invoice = invoiceOpt.get();
                invoice.setStatus("Paid");
                invoiceRepository.save(invoice);
            }
        }
        
        return savedPayment;
    }
    
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
    
    public List<Payment> getPaymentsByInvoiceId(Long invoiceId) {
        return paymentRepository.findByInvoiceId(invoiceId);
    }
}
