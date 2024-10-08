package com.example.payment_system.model;

import com.example.payment_provider.PaymentProvider;
import jakarta.persistence.*;
import shared_lib.models.Money;

import java.time.LocalDateTime;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long invoiceId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "price", column = @Column(name = "amount"))
    })
    private Money amount;

    @Enumerated(EnumType.STRING)
    private PaymentProvider paymentProvider;
    private String transactionReference;
    private String cardSchema;

    private LocalDateTime createdAt;

    private LocalDateTime dueTo;

    public Payment() {
    }

    public Payment(Long invoiceId, Money amount, PaymentProvider paymentProvider) {
        this.invoiceId = invoiceId;
        this.amount = amount;
        this.paymentProvider = paymentProvider;
        this.status = PaymentStatus.PENDING;
        this.dueTo = LocalDateTime.now().plusMinutes(5);
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

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public PaymentProvider getPaymentProvider() {
        return paymentProvider;
    }

    public void setPaymentProvider(PaymentProvider paymentMethod) {
        this.paymentProvider = paymentMethod;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getCardSchema() {
        return cardSchema;
    }

    public void setCardSchema(String cardSchema) {
        this.cardSchema = cardSchema;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDueTo() {
        return dueTo;
    }

    public void setDueTo(LocalDateTime dueTo) {
        this.dueTo = dueTo;
    }
}