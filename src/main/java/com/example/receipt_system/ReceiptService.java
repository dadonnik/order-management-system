package com.example.receipt_system;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReceiptService {

    private final ReceiptRepository receiptRepository;

    public ReceiptService(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    public Optional<Receipt> getReceiptByReceiptId(Long receiptId) {
        return receiptRepository.findById(receiptId);
    }

    public Optional<Receipt> getReceiptByInvoiceId(Long invoiceId) {
        return receiptRepository.findByInvoiceId(invoiceId);
    }

    public List<Receipt> getReceiptsByOrderId(Long orderId) {
        return receiptRepository.findAllByOrderId(orderId);
    }
}