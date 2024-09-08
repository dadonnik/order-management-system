package com.example.payment_provider;

import org.springframework.stereotype.Service;
import com.example.payment_system.Payment;

@Service
public class StripePaymentProvider implements PaymentProvider {

    @Override
    public String processPayment(Payment payment) {
        System.out.println("Processing payment with Stripe for invoice: " + payment.getInvoiceId() + " amount: $" + payment.getAmount());
        return "SUCCESS";
    }
}
