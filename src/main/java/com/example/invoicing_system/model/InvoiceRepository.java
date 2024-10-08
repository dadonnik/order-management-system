package com.example.invoicing_system.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByOrderIdAndStatusIn(Long orderId, List<InvoiceStatus> statuses);
}
