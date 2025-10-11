package com.example.catering_system.service;

import com.example.catering_system.model.Payment;
import com.example.catering_system.repository.PaymentRepository;
import com.example.catering_system.service.payment.PaymentGatewayStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    private final Map<String, PaymentGatewayStrategy> strategyByName;

    @Autowired
    public PaymentService(List<PaymentGatewayStrategy> strategies) {
        this.strategyByName = strategies.stream()
                .collect(Collectors.toMap(PaymentGatewayStrategy::getName, s -> s));
    }

    public Payment recordPayment(Payment payment) {
        // Persist payment record first (or after successful process depending on business rule)
        return paymentRepository.save(payment);
    }

    public boolean processViaGateway(Payment payment, String gatewayName) {
        PaymentGatewayStrategy strategy = strategyByName.get(gatewayName);
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported payment gateway: " + gatewayName);
        }
        return strategy.process(payment);
    }
}
