package com.example.invoicing_system;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    @ElementCollection
    private List<Long> items;

    private double totalAmount;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    public Invoice(Long orderId, List<Long> items) {
        this.orderId = orderId;
        this.items = items;
        this.totalAmount = 0;
        this.status = InvoiceStatus.PENDING;
    }

    public Invoice() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<Long> getItems() {
        return items;
    }

    public void setItems(List<Long> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }
}