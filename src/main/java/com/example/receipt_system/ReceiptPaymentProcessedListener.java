package com.example.receipt_system;

import com.example.invoicing_system.Invoice;
import com.example.payment_system.Payment;
import com.example.payment_system.PaymentStatus;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import out_of_scope_services.order_management_system.Order;
import out_of_scope_services.student_management_system.Student;
import out_of_scope_services.tenant_management_system.Tenant;
import out_of_scope_services.user_management_system.User;
import shared_lib.api_clients.*;
import shared_lib.events.PaymentProcessedEvent;

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

    @EventListener
    public void handlePaymentProcessedEvent(PaymentProcessedEvent event) {
        if (event.getStatus() != PaymentStatus.PAID) {
            return;
        }

        Invoice invoice = invoiceServiceClient.getInvoiceById(event.getInvoiceId());
        Order order = orderServiceClient.getOrderById(invoice.getOrderId());
        Student student = studentServiceClient.getStudentById(order.getStudentId());
        User user = userServiceClient.getUserById(order.getStudentId());
        Tenant tenant = tenantServiceClient.getTenantById(order.getTenantId());
        Payment payment = paymentServiceClient.getPaidPaymentByInvoiceId(event.getInvoiceId());

        try {
            Receipt receipt = new Receipt(tenant, user, student, payment, order, invoice);
            receiptRepository.save(receipt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}