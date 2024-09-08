package com.example.receipt_system;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    Optional<Receipt> findByInvoiceId(Long invoiceId);

    List<Receipt> findAllByOrderId(Long orderId);
}