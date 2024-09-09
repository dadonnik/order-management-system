package com.example.invoicing_system.service;

import com.example.invoicing_system.model.Invoice;
import com.example.invoicing_system.model.InvoiceRepository;
import com.example.invoicing_system.model.InvoiceStatus;
import org.springframework.stereotype.Service;
import out_of_scope_services.order_management_system.Order;
import shared_lib.api_clients.OrderServiceClient;
import shared_lib.models.Price;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final OrderServiceClient orderServiceClient;

    public InvoiceServiceImpl(
            InvoiceRepository invoiceRepository,
            OrderServiceClient orderServiceClient
    ) {
        this.invoiceRepository = invoiceRepository;
        this.orderServiceClient = orderServiceClient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Invoice createInvoice(Long orderId, List<Long> selectedItems) {
        Order order = orderServiceClient.getOrderById(orderId);

        if (order == null) {
            throw new IllegalArgumentException("Order not found");
        }
        if (selectedItems.isEmpty()) {
            throw new IllegalArgumentException("No items selected");
        }

        if (selectedItems.stream().anyMatch(selectedItem -> order.getItems().stream().noneMatch(orderItem -> orderItem.getId().equals(selectedItem)))) {
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
        int totalAmount = order.getItems().stream()
                .filter(item -> selectedItems.contains(item.getId()))
                .mapToInt(value -> value.getPrice().toInt())
                .sum();
        newInvoice.setTotalAmount(new Price(String.valueOf(totalAmount)));

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
    public void updateInvoiceStatus(Long invoiceId, InvoiceStatus status) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
        invoice.setStatus(status);
        invoiceRepository.save(invoice);
    }
}