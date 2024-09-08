package com.example.invoicing_system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    public Invoice createInvoice(@RequestBody InvoiceRequest invoiceRequest) {
        return invoiceService.createInvoice(invoiceRequest.getOrderId(), invoiceRequest.getSelectedItems());
    }

    @GetMapping("/{invoiceId}")
    public Invoice getInvoice(@PathVariable Long invoiceId) {
        return invoiceService.getInvoice(invoiceId);
    }

    @GetMapping("/{invoiceId}/receipt")
    public Receipt getInvoiceReceipt(@PathVariable Long invoiceId) {
        return invoiceService.getInvoiceReceipt(invoiceId);
    }

    @PostMapping("/update")
    public Invoice updateInvoiceStatus(@RequestBody UpdateInvoiceRequest updateInvoiceRequest) {
        return invoiceService.updateInvoiceStatus(updateInvoiceRequest.getInvoiceId(), updateInvoiceRequest.getStatus());
    }
}