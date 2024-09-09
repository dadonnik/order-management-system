package com.example.payment_system.service;

import com.example.payment_system.model.Payment;

public interface PaymentService {
    Payment initializePayment(Long invoiceId);

    Payment processPayment(Long paymentId);

    Payment getPaidPaymentByInvoiceId(Long invoiceId);
}