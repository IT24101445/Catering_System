package com.example.catering_system.admin.service;

import com.example.catering_system.admin.model.Invoice;
import com.example.catering_system.admin.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public Invoice saveInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceRepository.findById(id);
    }

    public Invoice createInvoice(Long customerId, double amount, String description) {
        Invoice invoice = new Invoice();
        invoice.setCustomerId(customerId);
        invoice.setAmount(amount);
        invoice.setIssueDate(new Date());
        
        // Set due date to 30 days from issue date
        Date dueDate = new Date();
        dueDate.setTime(dueDate.getTime() + (30L * 24 * 60 * 60 * 1000));
        invoice.setDueDate(dueDate);
        
        invoice.setStatus("Unpaid");
        return invoiceRepository.save(invoice);
    }

    public Invoice markAsPaid(Long invoiceId) {
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(invoiceId);
        if (invoiceOpt.isPresent()) {
            Invoice invoice = invoiceOpt.get();
            invoice.setStatus("Paid");
            return invoiceRepository.save(invoice);
        }
        return null;
    }

    public List<Invoice> getUnpaidInvoices() {
        return invoiceRepository.findAll().stream()
                .filter(invoice -> "Unpaid".equals(invoice.getStatus()))
                .toList();
    }

    public List<Invoice> getOverdueInvoices() {
        Date today = new Date();
        return invoiceRepository.findAll().stream()
                .filter(invoice -> "Unpaid".equals(invoice.getStatus()) && 
                                 invoice.getDueDate().before(today))
                .toList();
    }

    public void sendPaymentReminders() {
        List<Invoice> overdueInvoices = getOverdueInvoices();
        // Implementation for sending email/SMS reminders
        // This would integrate with email service or SMS service
        System.out.println("Sending payment reminders for " + overdueInvoices.size() + " overdue invoices");
    }
}
