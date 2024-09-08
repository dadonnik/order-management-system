package com.example.payment_system;

import com.example.invoicing_system.Invoice;
import com.example.invoicing_system.InvoiceStatus;
import com.example.payment_provider.PaymentProvider;
import com.example.payment_provider.PaymentProviderFactory;
import com.example.payment_provider.PaymentProviderGateway;
import com.example.payment_provider.PaymentProviderResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import shared_lib.api_clients.InvoiceServiceClient;
import shared_lib.events.PaymentProcessedEvent;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final InvoiceServiceClient invoiceServiceClient;
    private final PaymentProviderFactory paymentProviderFactory;
    private final PaymentRepository paymentRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PaymentServiceImpl(
            InvoiceServiceClient invoiceServiceClient,
            PaymentProviderFactory paymentProviderFactory,
            PaymentRepository paymentRepository,
            ApplicationEventPublisher eventPublisher
    ) {
        this.invoiceServiceClient = invoiceServiceClient;
        this.paymentProviderFactory = paymentProviderFactory;
        this.paymentRepository = paymentRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Payment initializePayment(Long invoiceId) {
        Payment existingPayment = paymentRepository.findByInvoiceIdAndStatusIn(invoiceId, List.of(PaymentStatus.PAID, PaymentStatus.PENDING));
        if (existingPayment != null) {
            return existingPayment;
        }

        Invoice invoiceResponse = invoiceServiceClient.getInvoiceById(invoiceId);
        if (invoiceResponse == null) {
            throw new IllegalArgumentException("Invoice not found");
        }
        if (invoiceResponse.getStatus() != InvoiceStatus.PENDING) {
            throw new IllegalArgumentException("Invoice is not pending");
        }

        Payment payment = new Payment(invoiceResponse.getId(), invoiceResponse.getTotalAmount(), PaymentProvider.STRIPE);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment processPayment(Long paymentId) {
        Payment existingPayment = paymentRepository.findById(paymentId).orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        if (existingPayment.getStatus() != PaymentStatus.PENDING) {
            return existingPayment;
        }

        PaymentProviderGateway provider = paymentProviderFactory.getProvider(existingPayment.getPaymentProvider());
        PaymentProviderResponse paymentResult = provider.processPayment(existingPayment);
        existingPayment.setStatus(paymentResult.getStatus());
        existingPayment.setTransactionReference(paymentResult.getTransactionReference());
        existingPayment.setCardSchema(paymentResult.getCardSchema());
        existingPayment = paymentRepository.save(existingPayment);

        eventPublisher.publishEvent(new PaymentProcessedEvent(this, existingPayment.getInvoiceId(), existingPayment.getStatus()));

        return existingPayment;
    }

    @Override
    public Payment getPaidPaymentByInvoiceId(Long invoiceId) {
        return paymentRepository.findByInvoiceIdAndStatusIn(invoiceId, List.of(PaymentStatus.PAID));
    }
}
