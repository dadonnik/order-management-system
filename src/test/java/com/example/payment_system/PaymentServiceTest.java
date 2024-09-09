package com.example.payment_system;

import com.example.invoicing_system.Invoice;
import com.example.invoicing_system.InvoiceStatus;
import com.example.payment_provider.PaymentProvider;
import com.example.payment_provider.PaymentProviderFactory;
import com.example.payment_provider.PaymentProviderGateway;
import com.example.payment_provider.PaymentProviderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import shared_lib.api_clients.InvoiceServiceClient;
import shared_lib.events.PaymentProcessedEvent;
import shared_lib.models.Price;

import java.util.List;
import java.util.Optional;

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

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        paymentService = new PaymentServiceImpl(invoiceServiceClient, paymentProviderFactory, paymentRepository, eventPublisher);
    }

    @Test
    public void testInitializePayment_ExistingPaymentFound() {
        // Mock an existing payment
        Payment existingPayment = new Payment(1L, new Price("50000"), PaymentProvider.STRIPE);
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
        when(invoiceServiceClient.getInvoiceById(1L)).thenReturn(null); // Simulating invoice not found

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentService.initializePayment(1L);
        });

        assertEquals("Invoice not found", exception.getMessage());

        // Verify that the payment repository's save method was never called
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    public void testInitializePayment_InvoiceNotPending() {

        when(paymentRepository.findByInvoiceIdAndStatusIn(eq(1L), anyList())).thenReturn(null);

        Invoice invoice = new Invoice(1L, List.of(1L, 2L));
        invoice.setStatus(InvoiceStatus.PAID);
        when(invoiceServiceClient.getInvoiceById(1L)).thenReturn(invoice);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentService.initializePayment(1L);
        });

        assertEquals("Invoice is not pending", exception.getMessage());

        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    public void testInitializePayment_SuccessfulNewPayment() {
        when(paymentRepository.findByInvoiceIdAndStatusIn(eq(1L), anyList())).thenReturn(null);

        Invoice invoice = new Invoice(1L, List.of(1L, 2L));
        invoice.setStatus(InvoiceStatus.PENDING); // Invoice is pending
        when(invoiceServiceClient.getInvoiceById(1L)).thenReturn(invoice);

        Payment savedPayment = new Payment(1L, new Price("50000"), PaymentProvider.STRIPE);
        savedPayment.setStatus(PaymentStatus.PENDING);
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        Payment result = paymentService.initializePayment(1L);

        assertNotNull(result);
        assertEquals(PaymentStatus.PENDING, result.getStatus());
        assertEquals(new Price("50000"), result.getAmount());
        assertEquals(PaymentProvider.STRIPE, result.getPaymentProvider());

        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    public void testInitializePayment_ExistingPaidPaymentFound() {
        Payment existingPayment = new Payment(1L, new Price("50000"), PaymentProvider.STRIPE);
        existingPayment.setStatus(PaymentStatus.PAID);

        when(paymentRepository.findByInvoiceIdAndStatusIn(eq(1L), anyList())).thenReturn(existingPayment);

        Payment result = paymentService.initializePayment(1L);

        assertNotNull(result);
        assertEquals(PaymentStatus.PAID, result.getStatus());
        assertEquals(existingPayment, result);

        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    public void testProcessPayment_PaymentNotFound() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentService.processPayment(1L);
        });

        assertEquals("Payment not found", exception.getMessage());
    }

    @Test
    public void testProcessPayment_PaymentAlreadyProcessed() {
        Payment existingPayment = new Payment(1L, new Price("50000"), PaymentProvider.STRIPE);
        existingPayment.setStatus(PaymentStatus.PAID);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(existingPayment));

        Payment result = paymentService.processPayment(1L);

        assertNotNull(result);
        assertEquals(PaymentStatus.PAID, result.getStatus());
        verify(paymentProviderFactory, never()).getProvider(any(PaymentProvider.class));
        verify(paymentRepository, never()).save(any(Payment.class));
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    public void testProcessPayment_ZeroAmountPayment() {
        Payment existingPayment = new Payment(1L, new Price("0"), PaymentProvider.STRIPE);
        existingPayment.setStatus(PaymentStatus.PENDING);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(existingPayment));

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.processPayment(1L);

        assertNotNull(result);
        assertEquals(PaymentStatus.PAID, result.getStatus());
        assertEquals(new Price("0"), result.getAmount());

        verify(paymentRepository).save(existingPayment);
        verify(eventPublisher).publishEvent(any(PaymentProcessedEvent.class));
        verify(paymentProviderFactory, never()).getProvider(any(PaymentProvider.class));
    }

    @Test
    public void testProcessPayment_SuccessfulProcessing() {
        Payment existingPayment = new Payment(1L, new Price("50000"), PaymentProvider.STRIPE);
        existingPayment.setStatus(PaymentStatus.PENDING);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(existingPayment));

        PaymentProviderResponse providerResponse = new PaymentProviderResponse(PaymentStatus.PAID, "txn123", "VISA");

        when(paymentProviderFactory.getProvider(PaymentProvider.STRIPE)).thenReturn(paymentProviderGateway);
        when(paymentProviderGateway.processPayment(existingPayment)).thenReturn(providerResponse);

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.processPayment(1L);

        assertNotNull(result);
        assertEquals(PaymentStatus.PAID, result.getStatus());
        assertEquals("txn123", result.getTransactionReference());
        assertEquals("VISA", result.getCardSchema());

        verify(paymentProviderFactory).getProvider(PaymentProvider.STRIPE);
        verify(paymentProviderGateway).processPayment(existingPayment);
        verify(paymentRepository).save(existingPayment);
        verify(eventPublisher).publishEvent(any(PaymentProcessedEvent.class));
    }

    @Test
    public void testProcessPayment_ProcessingFailure() {
        Payment existingPayment = new Payment(1L, new Price("50000"), PaymentProvider.STRIPE);
        existingPayment.setStatus(PaymentStatus.PENDING);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(existingPayment));

        PaymentProviderResponse providerResponse = new PaymentProviderResponse(PaymentStatus.FAILED, "txn123", null);

        when(paymentProviderFactory.getProvider(PaymentProvider.STRIPE)).thenReturn(paymentProviderGateway);
        when(paymentProviderGateway.processPayment(existingPayment)).thenReturn(providerResponse);

        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.processPayment(1L);

        assertNotNull(result);
        assertEquals(PaymentStatus.FAILED, result.getStatus());
        assertEquals("txn123", result.getTransactionReference());

        verify(paymentProviderFactory).getProvider(PaymentProvider.STRIPE);
        verify(paymentProviderGateway).processPayment(existingPayment);
        verify(paymentRepository).save(existingPayment);
        verify(eventPublisher).publishEvent(any(PaymentProcessedEvent.class));
    }
}