package com.example.payment_provider;

import com.example.payment_system.model.PaymentStatus;

public class PaymentProviderResponse {
    private PaymentStatus status;
    private String transactionReference;
    private String cardSchema;

    public PaymentProviderResponse(PaymentStatus status, String transactionReference, String cardSchema) {
        this.status = status;
        this.transactionReference = transactionReference;
        this.cardSchema = cardSchema;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
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
