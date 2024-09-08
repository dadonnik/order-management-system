package com.example.payment_provider;

import com.example.payment_system.Payment;

public interface PaymentProviderGateway {
    PaymentProviderResponse processPayment(Payment payment);
}