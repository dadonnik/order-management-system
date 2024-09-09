package com.example.receipt_system.listener;

import com.example.invoicing_system.model.Invoice;
import com.example.payment_system.model.Payment;
import com.example.payment_system.model.PaymentStatus;
import com.example.receipt_system.model.Receipt;
import com.example.receipt_system.model.ReceiptRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import out_of_scope_services.order_management_system.Order;
import out_of_scope_services.student_management_system.Student;
import out_of_scope_services.tenant_management_system.Tenant;
import out_of_scope_services.user_management_system.User;
import shared_lib.api_clients.*;
import shared_lib.events.PaymentProcessedEvent;

import java.util.concurrent.CompletableFuture;

@Component
public class ReceiptPaymentProcessedListener {
    private final InvoiceServiceClient invoiceServiceClient;
    private final OrderServiceClient orderServiceClient;
    private final StudentServiceClient studentServiceClient;
    private final UserServiceClient userServiceClient;
    private final TenantServiceClient tenantServiceClient;
    private final PaymentServiceClient paymentServiceClient;
    private final ReceiptRepository receiptRepository;

    public ReceiptPaymentProcessedListener(
            InvoiceServiceClient invoiceServiceClient,
            OrderServiceClient orderServiceClient,
            StudentServiceClient studentServiceClient,
            UserServiceClient userServiceClient,
            TenantServiceClient tenantServiceClient,
            PaymentServiceClient paymentServiceClient,
            ReceiptRepository receiptRepository
    ) {
        this.invoiceServiceClient = invoiceServiceClient;
        this.orderServiceClient = orderServiceClient;
        this.studentServiceClient = studentServiceClient;
        this.userServiceClient = userServiceClient;
        this.tenantServiceClient = tenantServiceClient;
        this.paymentServiceClient = paymentServiceClient;
        this.receiptRepository = receiptRepository;
    }

    // Will be replaced with RMQ/Kafka
    @EventListener
    public void handlePaymentProcessedEvent(PaymentProcessedEvent event) {
        if (event.getStatus() != PaymentStatus.PAID) {
            return;
        }

        CompletableFuture<Invoice> invoiceFuture = invoiceServiceClient.getInvoiceById(event.getInvoiceId());
        CompletableFuture<Order> orderFuture = invoiceFuture.thenCompose(invoice -> orderServiceClient.getOrderById(invoice.getOrderId()));
        CompletableFuture<Student> studentFuture = orderFuture.thenCompose(order -> studentServiceClient.getStudentById(order.getStudentId()));
        CompletableFuture<User> userFuture = orderFuture.thenCompose(order -> userServiceClient.getUserById(order.getStudentId()));
        CompletableFuture<Tenant> tenantFuture = orderFuture.thenCompose(order -> tenantServiceClient.getTenantById(order.getTenantId()));
        CompletableFuture<Payment> paymentFuture = paymentServiceClient.getPaidPaymentByInvoiceId(event.getInvoiceId());

        CompletableFuture.allOf(invoiceFuture, orderFuture, studentFuture, userFuture, tenantFuture, paymentFuture)
                .thenRun(() -> {
                    try {
                        Invoice invoice = invoiceFuture.join();
                        Order order = orderFuture.join();
                        Student student = studentFuture.join();
                        User user = userFuture.join();
                        Tenant tenant = tenantFuture.join();
                        Payment payment = paymentFuture.join();

                        Receipt receipt = new Receipt(tenant, user, student, payment, order, invoice);
                        receiptRepository.save(receipt);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }
}