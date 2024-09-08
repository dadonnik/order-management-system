package com.example.receipt_system;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @GetMapping
    public ResponseEntity<List<Receipt>> getReceipt(
            @RequestParam(required = false) Long receiptId,
            @RequestParam(required = false) Long invoiceId,
            @RequestParam(required = false) Long orderId) {

        List<Receipt> result = new ArrayList<>();
        if (receiptId != null) {
            Optional<Receipt> receipt = receiptService.getReceiptByReceiptId(receiptId);
            receipt.ifPresent(result::add);
        } else if (invoiceId != null) {
            Optional<Receipt> receipt = receiptService.getReceiptByInvoiceId(invoiceId);
            receipt.ifPresent(result::add);
        } else if (orderId != null) {
            List<Receipt> receipts = receiptService.getReceiptsByOrderId(orderId);
            result.addAll(receipts);
        } else {
            throw new IllegalArgumentException("At least one of receiptId, invoiceId or orderId must be provided");
        }

        return ResponseEntity.ok(result);
    }
}