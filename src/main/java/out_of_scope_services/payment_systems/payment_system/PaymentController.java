package out_of_scope_services.payment_systems.payment_system;

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
}
