package com.example.payment_system.controller;

import com.example.payment_system.model.Payment;
import com.example.payment_system.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public Payment initializePayment(@RequestParam Long invoiceId) {
        return paymentService.initializePayment(invoiceId);
    }

    @PostMapping("/process")
    public Payment processPayment(@RequestParam Long paymentId) {
        return paymentService.handlePaymentWebhook(paymentId);
    }

    @GetMapping("/{invoiceId}")
    public Payment getPaidPaymentByInvoiceId(@PathVariable Long invoiceId) {
        return paymentService.getPaidPaymentByInvoiceId(invoiceId);
    }
}
