package com.example.payment_provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentProviderFactory {
    private final Map<PaymentProvider, PaymentProviderGateway> paymentProviders;

    @Autowired
    public PaymentProviderFactory(Map<PaymentProvider, PaymentProviderGateway> paymentProviders) {
        this.paymentProviders = paymentProviders;
    }

    public PaymentProviderGateway getProvider(PaymentProvider paymentMethod) {
        PaymentProviderGateway provider = paymentProviders.get(paymentMethod);
        if (provider == null) {
            throw new IllegalArgumentException("Unsupported payment method: " + paymentMethod);
        }
        return provider;
    }
}