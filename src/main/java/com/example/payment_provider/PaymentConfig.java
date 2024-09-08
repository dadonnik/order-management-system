package com.example.payment_provider;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Configuration
public class PaymentConfig {

    @Autowired
    private StripePaymentProviderGateway stripePaymentProvider;

    @Autowired
    private PayPalPaymentProviderGateway payPalPaymentProvider;

    @Bean
    public Map<PaymentProvider, PaymentProviderGateway> paymentProviders() {
        return Map.of(
                PaymentProvider.STRIPE, stripePaymentProvider,
                PaymentProvider.PAYPAL, payPalPaymentProvider
        );
    }
}
