package com.example.receipt_system;

import com.example.invoicing_system.model.Invoice;
import com.example.payment_provider.PaymentProvider;
import com.example.payment_system.model.Payment;
import com.example.payment_system.model.PaymentStatus;
import com.example.receipt_system.listener.ReceiptPaymentProcessedListener;
import com.example.receipt_system.model.Receipt;
import com.example.receipt_system.model.ReceiptRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import out_of_scope_services.order_management_system.Order;
import out_of_scope_services.order_management_system.OrderItem;
import out_of_scope_services.student_management_system.Student;
import out_of_scope_services.tenant_management_system.Tenant;
import out_of_scope_services.user_management_system.User;
import shared_lib.api_clients.*;
import shared_lib.events.PaymentProcessedEvent;
import shared_lib.models.Money;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReceiptPaymentProcessedListenerTest {

    @Mock
    private InvoiceServiceClient invoiceServiceClient;

    @Mock
    private OrderServiceClient orderServiceClient;

    @Mock
    private StudentServiceClient studentServiceClient;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private TenantServiceClient tenantServiceClient;

    @Mock
    private PaymentServiceClient paymentServiceClient;

    @Mock
    private ReceiptRepository receiptRepository;

    @InjectMocks
    private ReceiptPaymentProcessedListener receiptPaymentProcessedListener;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandlePaymentProcessedEvent_PaymentStatusPaid() {
        PaymentProcessedEvent event = new PaymentProcessedEvent(this,1L, PaymentStatus.PAID);

        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setOrderId(1L);
        invoice.setItems(List.of(1L));
        when(invoiceServiceClient.getInvoiceById(1L)).thenReturn(CompletableFuture.completedFuture(invoice));

        Order order = new Order();
        order.setStudentId(1L);
        order.setTenantId(1L);
        OrderItem orderItem = new OrderItem("item", new Money("100"));
        orderItem.setId(1L);
        order.setItems(List.of(orderItem));
        when(orderServiceClient.getOrderById(1L)).thenReturn(CompletableFuture.completedFuture(order));

        Student student = new Student();
        when(studentServiceClient.getStudentById(1L)).thenReturn(CompletableFuture.completedFuture(student));

        User user = new User();
        when(userServiceClient.getUserById(1L)).thenReturn(CompletableFuture.completedFuture(user));

        Tenant tenant = new Tenant();
        when(tenantServiceClient.getTenantById(1L)).thenReturn(CompletableFuture.completedFuture(tenant));

        Payment payment = new Payment(1L, new Money("100"), PaymentProvider.STRIPE);
        when(paymentServiceClient.getPaidPaymentByInvoiceId(1L)).thenReturn(CompletableFuture.completedFuture(payment));

        receiptPaymentProcessedListener.handlePaymentProcessedEvent(event);

        verify(receiptRepository, times(1)).save(any(Receipt.class));
    }

    @Test
    public void testHandlePaymentProcessedEvent_PaymentStatusNotPaid() {
        PaymentProcessedEvent event = new PaymentProcessedEvent(this, 1L, PaymentStatus.PENDING);

        receiptPaymentProcessedListener.handlePaymentProcessedEvent(event);

        verifyNoInteractions(invoiceServiceClient);
        verifyNoInteractions(orderServiceClient);
        verifyNoInteractions(studentServiceClient);
        verifyNoInteractions(userServiceClient);
        verifyNoInteractions(tenantServiceClient);
        verifyNoInteractions(paymentServiceClient);
        verifyNoInteractions(receiptRepository);
    }
}
