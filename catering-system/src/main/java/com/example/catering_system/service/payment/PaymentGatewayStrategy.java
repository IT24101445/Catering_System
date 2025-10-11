package com.example.catering_system.service.payment;

import com.example.catering_system.model.Payment;

public interface PaymentGatewayStrategy {
    boolean process(Payment payment);
    String getName();
}


