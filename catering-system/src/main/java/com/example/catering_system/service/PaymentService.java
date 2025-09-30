package com.example.catering_system.service;

import com.example.catering_system.model.Payment;
import com.example.catering_system.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment recordPayment(Payment payment) {
        return paymentRepository.save(payment);
    }
}
