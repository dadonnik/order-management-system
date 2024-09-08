package com.example.invoicing_system;

import org.springframework.stereotype.Service;
import com.example.order_management_system.OrderItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final Map<Long, List<OrderItem>> orders = new HashMap<>();

    private final InvoiceRepository invoiceRepository;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Invoice createInvoice(Long orderId, List<Long> selectedItems) {
        List<OrderItem> items = List.of(
                new OrderItem("Item 1", 50.00),
                new OrderItem("Item 2", 30.00)
        );

        Invoice invoice = new Invoice(orderId, items);
        return invoiceRepository.save(invoice);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Invoice getInvoice(Long invoiceId) {
        return invoiceRepository.findById(invoiceId).orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
    }
}