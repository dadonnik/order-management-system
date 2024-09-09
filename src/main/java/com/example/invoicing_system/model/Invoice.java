package com.example.invoicing_system.model;

import jakarta.persistence.*;
import shared_lib.models.Price;

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
    private Price totalAmount;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    public Invoice(Long orderId, List<Long> items) {
        this.orderId = orderId;
        this.items = items;
        this.totalAmount = new Price("0");
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

    public Price getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Price totalAmount) {
        this.totalAmount = totalAmount;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }
}