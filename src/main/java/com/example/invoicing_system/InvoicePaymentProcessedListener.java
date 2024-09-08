package com.example.invoicing_system;

import com.example.payment_system.PaymentStatus;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import shared_lib.events.PaymentProcessedEvent;

@Component
public class InvoicePaymentProcessedListener {
    private final InvoiceService invoiceService;

    InvoicePaymentProcessedListener(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @EventListener
    public void handlePaymentProcessedEvent(PaymentProcessedEvent event) {
        if (event.getStatus() != PaymentStatus.PAID) {
            return;
        }

        InvoiceStatus status = event.getStatus() == PaymentStatus.PAID ? InvoiceStatus.PAID : InvoiceStatus.PENDING;
        invoiceService.updateInvoiceStatus(event.getInvoiceId(), status);
    }
}