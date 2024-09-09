package com.example.payment_system.model;

public enum PaymentStatus {
    CREATED,
    PENDING,
    PAID,
    FAILED
    // In real system will be more statuses
    // AUTHORIZED, REFUNDED, PARTIALLY_REFUNDED, CANCELLED, etc.
}
