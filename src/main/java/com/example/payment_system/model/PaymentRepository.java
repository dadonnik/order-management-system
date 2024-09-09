package com.example.payment_system.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByInvoiceIdAndStatusIn(Long invoiceId, List<PaymentStatus> statuses);
}