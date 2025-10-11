package com.example.catering_system.service.payment;

import com.example.catering_system.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PayPalPaymentStrategy implements PaymentGatewayStrategy {
    @Override
    public boolean process(Payment payment) {
        return true;
    }

    @Override
    public String getName() {
        return "PAYPAL";
    }
}


