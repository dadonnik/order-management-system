package com.example.payment_provider;

import org.springframework.stereotype.Service;
import shared_lib.models.Money;

@Service
public class PaymentProviderAdvisor {
    public PaymentProvider advise(Money amount) {
        if ("AED".equalsIgnoreCase(amount.getCurrency()) && amount.getAmount().compareTo(new java.math.BigDecimal(1000)) > 0) {
            return PaymentProvider.STRIPE;
        } else {
            return PaymentProvider.PAYPAL;
        }
    }
}
