package com.example.invoicing_system;

import java.util.List;

public interface InvoiceService {
    Invoice createInvoice(Long orderId, List<Long> selectedItems);

    Invoice getInvoice(Long invoiceId);
}