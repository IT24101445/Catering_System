package com.example.catering_system.service.payment;

import com.example.catering_system.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class BankTransferPaymentStrategy implements PaymentGatewayStrategy {
    @Override
    public boolean process(Payment payment) {
        return true;
    }

    @Override
    public String getName() {
        return "BANK_TRANSFER";
    }
}


