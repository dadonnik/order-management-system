package com.example.invoicing_system;

import com.example.order_management_system.Order;
import com.example.order_management_system.OrderItem;
import com.example.payment_system.Payment;
import org.springframework.stereotype.Service;
import out_of_scope_services.student_management_system.Student;
import out_of_scope_services.tenant_management_system.Tenant;
import out_of_scope_services.user_management_system.User;
import shared_lib.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final OrderServiceClient orderServiceClient;
    private final StudentServiceClient studentServiceClient;
    private final UserServiceClient userServiceClient;
    private final TenantServiceClient tenantServiceClient;
    private final PaymentServiceClient paymentServiceClient;

    public InvoiceServiceImpl(
            InvoiceRepository invoiceRepository,
            OrderServiceClient orderServiceClient,
            StudentServiceClient studentServiceClient,
            UserServiceClient userServiceClient,
            TenantServiceClient tenantServiceClient,
            PaymentServiceClient paymentServiceClient
    ) {
        this.invoiceRepository = invoiceRepository;
        this.orderServiceClient = orderServiceClient;
        this.studentServiceClient = studentServiceClient;
        this.userServiceClient = userServiceClient;
        this.tenantServiceClient = tenantServiceClient;
        this.paymentServiceClient = paymentServiceClient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Invoice createInvoice(Long orderId, List<Long> selectedItems) {
        Order order = orderServiceClient.getOrderById(orderId);

        if(order == null) {
            throw new IllegalArgumentException("Order not found");
        }
        if(selectedItems.isEmpty()) {
            throw new IllegalArgumentException("No items selected");
        }

        if(selectedItems.stream().anyMatch(selectedItem -> order.getItems().stream().noneMatch(orderItem -> orderItem.getId().equals(selectedItem)))) {
            throw new IllegalArgumentException("Selected item(s) not part of the order");
        }

        List<Invoice> existingInvoices = invoiceRepository.findByOrderIdAndStatusIn(orderId, List.of(InvoiceStatus.PENDING, InvoiceStatus.PAID));

        Set<Long> existingOrderItemIds = existingInvoices.stream()
                .flatMap(invoice -> invoice.getItems().stream())
                .collect(Collectors.toSet());

        for (Long selectedItemId : selectedItems) {
            if (existingOrderItemIds.contains(selectedItemId)) {
                throw new IllegalArgumentException("Selected item(s) already part of an existing pending/paid invoice");
            }
        }

        Invoice newInvoice = new Invoice(orderId, selectedItems);
        double totalAmount = order.getItems().stream()
                .filter(item -> selectedItems.contains(item.getId()))
                .mapToDouble(OrderItem::getPrice)
                .sum();
        newInvoice.setTotalAmount(totalAmount);

        return invoiceRepository.save(newInvoice);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Invoice getInvoice(Long invoiceId) {
        return invoiceRepository.findById(invoiceId).orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Invoice updateInvoiceStatus(Long invoiceId, InvoiceStatus status) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
        invoice.setStatus(status);
        return invoiceRepository.save(invoice);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReceiptResponse getInvoiceReceipt(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
        Order order = orderServiceClient.getOrderById(invoice.getOrderId());
        Student student = studentServiceClient.getStudentById(order.getStudentId());
        User user = userServiceClient.getUserById(order.getStudentId());
        Tenant tenant = tenantServiceClient.getTenantById(order.getTenantId());
        Payment payment = paymentServiceClient.getPaidPaymentByInvoiceId(invoiceId);

        List<OrderItem> orderItems = order.getItems().stream()
                .filter(item -> invoice.getItems().contains(item.getId()))
                .collect(Collectors.toList());


        ReceiptResponse receiptResponse = new ReceiptResponse();
        receiptResponse.setTenant(tenant);
        receiptResponse.setUser(user);
//        receiptResponse.setInvoice(invoice);
        receiptResponse.setPayment(payment);
        receiptResponse.setStudent(student);
        receiptResponse.setOrderItems(orderItems);

        return receiptResponse;
    }
}