package com.example.payment_system.service;

import com.example.invoicing_system.model.Invoice;
import com.example.invoicing_system.model.InvoiceStatus;
import com.example.payment_provider.*;
import com.example.payment_system.model.Payment;
import com.example.payment_system.model.PaymentRepository;
import com.example.payment_system.model.PaymentStatus;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import shared_lib.api_clients.InvoiceServiceClient;
import shared_lib.events.PaymentProcessedEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final InvoiceServiceClient invoiceServiceClient;
    private final PaymentProviderFactory paymentProviderFactory;
    private final PaymentRepository paymentRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final PaymentProviderAdvisor paymentProviderAdvisor;

    public PaymentServiceImpl(
            InvoiceServiceClient invoiceServiceClient,
            PaymentProviderFactory paymentProviderFactory,
            PaymentRepository paymentRepository,
            ApplicationEventPublisher eventPublisher,
            PaymentProviderAdvisor paymentProviderAdvisor
    ) {
        this.invoiceServiceClient = invoiceServiceClient;
        this.paymentProviderFactory = paymentProviderFactory;
        this.paymentRepository = paymentRepository;
        this.eventPublisher = eventPublisher;
        this.paymentProviderAdvisor = paymentProviderAdvisor;
    }

    @Override
    public Payment initializePayment(Long invoiceId) {
        LocalDateTime now = LocalDateTime.now();

        Payment existingPayment = paymentRepository.findByInvoiceIdAndStatusIn(invoiceId, List.of(PaymentStatus.PAID, PaymentStatus.PENDING, PaymentStatus.CREATED));
        if (existingPayment != null) {
            return existingPayment;
        }

        // TODO somewhere have a thread checking for payment due_to date and cancel the payment if it's past due

        Invoice invoiceResponse = invoiceServiceClient.getInvoiceById(invoiceId).join();
        if (invoiceResponse == null) {
            throw new IllegalArgumentException("Invoice not found");
        }
        if (invoiceResponse.getStatus() != InvoiceStatus.PENDING) {
            throw new IllegalArgumentException("Invoice is not pending");
        }

        // System will advise the payment provider based on some criteria like:
        // Customer area, fees based on amount or currency, etc
        PaymentProvider paymentProvider = paymentProviderAdvisor.advise(invoiceResponse.getTotalAmount());

        Payment payment = new Payment(invoiceResponse.getId(), invoiceResponse.getTotalAmount(), paymentProvider);

        // If payment is 0 amount, we will mark it as paid
        // and don't do call to 3rd party payment gateway
        if (Objects.equals(payment.getAmount().getPrice(), "0")) {
            payment.setStatus(PaymentStatus.PAID);
            eventPublisher.publishEvent(new PaymentProcessedEvent(this, payment.getInvoiceId(), payment.getStatus()));
        } else {
            PaymentProviderGateway gateway = paymentProviderFactory.getProvider(payment.getPaymentProvider());
            String redirectUrl = gateway.initializePayment(payment);
            // Here we are assuming that the payment provider will return a URL to redirect the user to
            payment.setStatus(PaymentStatus.PENDING);
            // We have 2 statuses, CREATED and PENDING to handle cases payment stalled in the middle
        }
        payment = paymentRepository.save(payment);

        // For demonstration we are returning payment object itself.
        // But in reality customer will be redirected to the URL returned by the payment provider
        return payment;
    }

    // This is a simulation of a webhook from the payment provider
    @Override
    public Payment handlePaymentWebhook(Long paymentId) {
        Payment existingPayment = paymentRepository.findById(paymentId).orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        if (existingPayment.getStatus() != PaymentStatus.PENDING) {
            return existingPayment;
        }

        // This is simulating the payment provider processing the payment
        // And called this endpoint with this data.
        PaymentProviderGateway provider = paymentProviderFactory.getProvider(existingPayment.getPaymentProvider());
        PaymentProviderResponse paymentResult = provider.handlePaymentWebhook(existingPayment);
        existingPayment.setStatus(paymentResult.getStatus());
        existingPayment.setTransactionReference(paymentResult.getTransactionReference());
        existingPayment.setCardSchema(paymentResult.getCardSchema());
        existingPayment = paymentRepository.save(existingPayment);


        // TODO Will be replaced with RMQ/Kafka
        eventPublisher.publishEvent(new PaymentProcessedEvent(this, existingPayment.getInvoiceId(), existingPayment.getStatus()));

        return existingPayment;
    }

    @Override
    public Payment getPaidPaymentByInvoiceId(Long invoiceId) {
        return paymentRepository.findByInvoiceIdAndStatusIn(invoiceId, List.of(PaymentStatus.PAID));
    }
}
