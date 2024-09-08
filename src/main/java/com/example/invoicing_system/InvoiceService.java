package com.example.invoicing_system;

import java.util.List;

public interface InvoiceService {
    Invoice createInvoice(Long orderId, List<Long> selectedItems);

    Invoice getInvoice(Long invoiceId);

    Invoice updateInvoiceStatus(Long invoiceId, InvoiceStatus status);

    ReceiptResponse getInvoiceReceipt(Long invoiceId);
}