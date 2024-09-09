package com.example.invoicing_system.model;

import jakarta.persistence.*;
import shared_lib.models.Money;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    @ElementCollection
    private List<Long> items;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "price", column = @Column(name = "total_amount"))
    })
    private Money totalAmount;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    private LocalDateTime createdAt;

    public Invoice(Long orderId, List<Long> items, Money totalAmount) {
        this.orderId = orderId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.status = InvoiceStatus.PENDING;
    }

    public Invoice() {

    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
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

    public Money getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Money totalAmount) {
        this.totalAmount = totalAmount;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}