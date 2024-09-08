package com.example.payment_system;

public interface PaymentService {
    Payment initializePayment(Long invoiceId);
    Payment processPayment(Long paymentId);
    Payment getPaidPaymentByInvoiceId(Long invoiceId);
}