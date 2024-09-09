package com.example.payment_system;

import com.example.invoicing_system.model.Invoice;
import com.example.invoicing_system.model.InvoiceStatus;
import com.example.payment_provider.*;
import com.example.payment_system.model.Payment;
import com.example.payment_system.model.PaymentRepository;
import com.example.payment_system.model.PaymentStatus;
import com.example.payment_system.service.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import shared_lib.api_clients.InvoiceServiceClient;
import shared_lib.events.PaymentProcessedEvent;
import shared_lib.models.Money;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private InvoiceServiceClient invoiceServiceClient;

    @Mock
    private PaymentProviderFactory paymentProviderFactory;

    @Mock
    private PaymentProviderGateway paymentProviderGateway;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Mock
    private PaymentProviderAdvisor paymentProviderAdvisor;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        paymentService = new PaymentServiceImpl(invoiceServiceClient, paymentProviderFactory, paymentRepository, eventPublisher, paymentProviderAdvisor);
    }

    @Test
    public void testInitializePayment_ExistingPaymentFound() {
        Payment existingPayment = new Payment(1L, new Money("50000"), PaymentProvider.STRIPE);
        existingPayment.setStatus(PaymentStatus.PENDING);

        when(paymentRepository.findByInvoiceIdAndStatusIn(eq(1L), anyList())).thenReturn(existingPayment);

        Payment result = paymentService.initializePayment(1L);

        assertNotNull(result);
        assertEquals(PaymentStatus.PENDING, result.getStatus());
        assertEquals(existingPayment, result);

        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    public void testInitializePayment_InvoiceNotFound() {
        when(paymentRepository.findByInvoiceIdAndStatusIn(eq(1L), anyList())).thenReturn(null);
        when(invoiceServiceClient.getInvoiceById(1L)).thenReturn(CompletableFuture.completedFuture(null)); // Simulating invoice not found

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentService.initializePayment(1L);
        });

        assertEquals("Invoice not found", exception.getMessage());

        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    public void testInitializePayment_InvoiceNotPending() {

        when(paymentRepository.findByInvoiceIdAndStatusIn(eq(1L), anyList())).thenReturn(null);

        Invoice invoice = new Invoice(1L, List.of(1L, 2L), new Money("0"));
        invoice.setStatus(InvoiceStatus.PAID);
        when(invoiceServiceClient.getInvoiceById(1L)).thenReturn(CompletableFuture.completedFuture(invoice));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentService.initializePayment(1L);
        });

        assertEquals("Invoice is not pending", exception.getMessage());

        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    public void testInitializePayment_SuccessfulNewPayment() {
        when(paymentRepository.findByInvoiceIdAndStatusIn(eq(1L), anyList())).thenReturn(null);

        Invoice invoice = new Invoice(1L, List.of(1L, 2L), new Money("50000"));
        invoice.setStatus(InvoiceStatus.PENDING);
        when(invoiceServiceClient.getInvoiceById(1L)).thenReturn(CompletableFuture.completedFuture(invoice));

        Payment savedPayment = new Payment(1L, new Money("50000"), PaymentProvider.STRIPE);
        savedPayment.setStatus(PaymentStatus.PENDING);
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        // Mocking the paymentProviderFactory to return the mocked PaymentProviderGateway
        when(paymentProviderFactory.getProvider(PaymentProvider.STRIPE)).thenReturn(paymentProviderGateway);

        // Mocking the payment provider gateway to return a fake redirect URL
        when(paymentProviderGateway.initializePayment(any(Payment.class))).thenReturn("https://fake-url.com/payment");

        when(paymentProviderAdvisor.advise(savedPayment.getAmount())).thenReturn(PaymentProvider.STRIPE);

        Payment result = paymentService.initializePayment(1L);

        // Validate the result
        assertNotNull(result);
        assertEquals(PaymentStatus.PENDING, result.getStatus());
        assertEquals(new Money("50000"), result.getAmount());
        assertEquals(PaymentProvider.STRIPE, result.getPaymentProvider());

        verify(paymentRepository, times(2)).save(any(Payment.class));

        verify(paymentProviderFactory).getProvider(PaymentProvider.STRIPE);
        verify(paymentProviderGateway).initializePayment(any(Payment.class));
    }

    @Test
    public void testInitializePayment_ExistingPaidPaymentFound() {
        Payment existingPayment = new Payment(1L, new Money("50000"), PaymentProvider.STRIPE);
        existingPayment.setStatus(PaymentStatus.PAID);

        when(paymentRepository.findByInvoiceIdAndStatusIn(eq(1L), anyList())).thenReturn(existingPayment);

        Payment result = paymentService.initializePayment(1L);

        assertNotNull(result);
        assertEquals(PaymentStatus.PAID, result.getStatus());
        assertEquals(existingPayment, result);

        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    public void testHandlePayment_PaymentWebhookNotFound() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentService.handlePaymentWebhook(1L);
        });

        assertEquals("Payment not found", exception.getMessage());
    }

    @Test
    public void testHandlePayment_PaymentWebhookAlreadyProcessed() {
        Payment existingPayment = new Payment(1L, new Money("50000"), PaymentProvider.STRIPE);
        existingPayment.setStatus(PaymentStatus.PAID);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(existingPayment));

        Payment result = paymentService.handlePaymentWebhook(1L);

        assertNotNull(result);
        assertEquals(PaymentStatus.PAID, result.getStatus());
        verify(paymentProviderFactory, never()).getProvider(any(PaymentProvider.class));
        verify(paymentRepository, never()).save(any(Payment.class));
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    public void testHandlePayment_ZeroAmountPaymentWebhook() {
        Payment existingPayment = new Payment(1L, new Money("0"), PaymentProvider.STRIPE);
        existingPayment.setStatus(PaymentStatus.PENDING);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(existingPayment));

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.handlePaymentWebhook(1L);

        assertNotNull(result);
        assertEquals(PaymentStatus.PAID, result.getStatus());
        assertEquals(new Money("0"), result.getAmount());

        verify(paymentRepository).save(existingPayment);
        verify(eventPublisher).publishEvent(any(PaymentProcessedEvent.class));
        verify(paymentProviderFactory, never()).getProvider(any(PaymentProvider.class));
    }

    @Test
    public void testHandlePayment_Webhook_SuccessfulProcessing() {
        Payment existingPayment = new Payment(1L, new Money("50000"), PaymentProvider.STRIPE);
        existingPayment.setStatus(PaymentStatus.PENDING);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(existingPayment));

        PaymentProviderResponse providerResponse = new PaymentProviderResponse(PaymentStatus.PAID, "txn123", "VISA");

        when(paymentProviderFactory.getProvider(PaymentProvider.STRIPE)).thenReturn(paymentProviderGateway);
        when(paymentProviderGateway.handlePaymentWebhook(existingPayment)).thenReturn(providerResponse);

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.handlePaymentWebhook(1L);

        assertNotNull(result);
        assertEquals(PaymentStatus.PAID, result.getStatus());
        assertEquals("txn123", result.getTransactionReference());
        assertEquals("VISA", result.getCardSchema());

        verify(paymentProviderFactory).getProvider(PaymentProvider.STRIPE);
        verify(paymentProviderGateway).handlePaymentWebhook(existingPayment);
        verify(paymentRepository).save(existingPayment);
        verify(eventPublisher).publishEvent(any(PaymentProcessedEvent.class));
    }

    @Test
    public void testHandlePayment_Webhook_ProcessingFailure() {
        Payment existingPayment = new Payment(1L, new Money("50000"), PaymentProvider.STRIPE);
        existingPayment.setStatus(PaymentStatus.PENDING);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(existingPayment));

        PaymentProviderResponse providerResponse = new PaymentProviderResponse(PaymentStatus.FAILED, "txn123", null);

        when(paymentProviderFactory.getProvider(PaymentProvider.STRIPE)).thenReturn(paymentProviderGateway);
        when(paymentProviderGateway.handlePaymentWebhook(existingPayment)).thenReturn(providerResponse);

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.handlePaymentWebhook(1L);

        assertNotNull(result);
        assertEquals(PaymentStatus.FAILED, result.getStatus());
        assertEquals("txn123", result.getTransactionReference());

        verify(paymentProviderFactory).getProvider(PaymentProvider.STRIPE);
        verify(paymentProviderGateway).handlePaymentWebhook(existingPayment);
        verify(paymentRepository).save(existingPayment);
        verify(eventPublisher).publishEvent(any(PaymentProcessedEvent.class));
    }
}