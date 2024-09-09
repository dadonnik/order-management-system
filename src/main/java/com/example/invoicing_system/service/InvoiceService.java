package com.example.invoicing_system.service;

import com.example.invoicing_system.model.Invoice;
import com.example.invoicing_system.model.InvoiceStatus;

import java.util.List;

public interface InvoiceService {
    Invoice createInvoice(Long orderId, List<Long> selectedItems);

    Invoice getInvoice(Long invoiceId);

    void updateInvoiceStatus(Long invoiceId, InvoiceStatus status);
}