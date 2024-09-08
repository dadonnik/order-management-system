package com.example.payment_provider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class PaymentConfig {
    private final StripePaymentProviderGateway stripePaymentProvider;
    private final PayPalPaymentProviderGateway payPalPaymentProvider;

    PaymentConfig(StripePaymentProviderGateway stripePaymentProvider, PayPalPaymentProviderGateway payPalPaymentProvider) {
        this.stripePaymentProvider = stripePaymentProvider;
        this.payPalPaymentProvider = payPalPaymentProvider;
    }

    @Bean
    public Map<PaymentProvider, PaymentProviderGateway> paymentProviders() {
        return Map.of(
                PaymentProvider.STRIPE, stripePaymentProvider,
                PaymentProvider.PAYPAL, payPalPaymentProvider
        );
    }
}
