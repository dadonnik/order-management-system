package com.example.payment_provider;

import com.example.payment_system.model.Payment;

public interface PaymentProviderGateway {
    // In real app will be triggered by Webhook from real payment gateway
    PaymentProviderResponse handlePaymentWebhook(Payment payment);

    String initializePayment(Payment payment);
}