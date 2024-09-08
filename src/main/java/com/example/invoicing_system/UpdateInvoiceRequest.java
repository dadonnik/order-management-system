package com.example.invoicing_system;

public class UpdateInvoiceRequest {
    private Long invoiceId;
    private InvoiceStatus status;

    public UpdateInvoiceRequest(Long invoiceId, InvoiceStatus status) {
        this.invoiceId = invoiceId;
        this.status = status;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }
}
