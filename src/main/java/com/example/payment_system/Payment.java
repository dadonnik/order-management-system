package com.example.payment_system;

import com.example.payment_provider.PaymentProvider;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long invoiceId;
    private PaymentStatus status;
    private double amount;
    private PaymentProvider paymentProvider;
    private String transactionReference;
    private String cardSchema;

    public Payment() {
    }

    public Payment(Long invoiceId, double amount, PaymentProvider paymentProvider) {
        this.invoiceId = invoiceId;
        this.amount = amount;
        this.paymentProvider = paymentProvider;
        this.status = PaymentStatus.PENDING;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
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
}