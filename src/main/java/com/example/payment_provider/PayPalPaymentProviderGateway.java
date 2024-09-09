package com.example.payment_provider;

import com.example.payment_system.Payment;
import com.example.payment_system.PaymentStatus;
import org.springframework.stereotype.Service;

@Service
public class PayPalPaymentProviderGateway implements PaymentProviderGateway {

    @Override
    // In real app will be triggered by Webhook from real payment gateway
    public PaymentProviderResponse processPayment(Payment payment) {
        System.out.println("Processing payment with PayPal for invoice: " + payment.getInvoiceId() + " amount: $" + payment.getAmount());
        String transactionReference = "PAYPAL-" + (int) (Math.random() * 1000000);
        String cardSchema = Math.random() > 0.5 ? "VISA" : "MASTERCARD";
        return new PaymentProviderResponse(PaymentStatus.PAID, transactionReference, cardSchema);
    }
}