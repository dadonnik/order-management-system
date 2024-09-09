package com.example.invoicing_system.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class InvoiceRequest {
    @NotNull(message = "Invoice ID must not be null")
    @Min(value = 1, message = "Invoice ID must be a positive number")
    private Long orderId;

    @NotNull(message = "Selected items must not be null")
    private List<Long> selectedItems;

    public InvoiceRequest(Long orderId, List<Long> selectedItems) {
        this.orderId = orderId;
        this.selectedItems = selectedItems;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<Long> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<Long> selectedItems) {
        this.selectedItems = selectedItems;
    }
}
