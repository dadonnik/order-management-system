package out_of_scope_services.payment_systems.invoicing_system;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        // Assume you retrieve the items based on selectedItems
        List<OrderItem> items = List.of(
                new OrderItem(1L, "Item 1", 50.00),
                new OrderItem(2L, "Item 2", 30.00)
        );

        Invoice invoice = new Invoice(orderId, items);
        return invoiceRepository.save(invoice);  // Persist the invoice in the database
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Invoice getInvoice(Long invoiceId) {
        return invoiceRepository.findById(invoiceId).orElseThrow(() -> new IllegalArgumentException("Invoice not found"));
    }
}