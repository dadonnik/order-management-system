package com.example.payment_provider;

import com.example.payment_system.Payment;

public interface PaymentProvider {
    String processPayment(Payment payment);
}