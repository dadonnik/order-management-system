package com.example.invoicing_system.listener;

import com.example.invoicing_system.model.InvoiceStatus;
import com.example.invoicing_system.service.InvoiceService;
import com.example.payment_system.model.PaymentStatus;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import shared_lib.events.PaymentProcessedEvent;

@Component
public class InvoicePaymentProcessedListener {
    private final InvoiceService invoiceService;

    InvoicePaymentProcessedListener(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    // TODO will be replaced with RMQ/Kafka
    @EventListener
    public void handlePaymentProcessedEvent(PaymentProcessedEvent event) {
        if (event.getStatus() == PaymentStatus.PAID) {
            invoiceService.updateInvoiceStatus(event.getInvoiceId(), InvoiceStatus.PAID);
        }
    }
}