package com.example.invoicing_system;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    public Invoice createInvoice(@Valid @RequestBody InvoiceRequest invoiceRequest) {
        return invoiceService.createInvoice(invoiceRequest.getOrderId(), invoiceRequest.getSelectedItems());
    }

    @GetMapping("/{invoiceId}")
    public Invoice getInvoice(@PathVariable Long invoiceId) {
        return invoiceService.getInvoice(invoiceId);
    }
}