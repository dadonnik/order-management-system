package out_of_scope_services.payment_systems.payment_provider;

import org.springframework.stereotype.Service;
import out_of_scope_services.payment_systems.payment_system.Payment;

@Service
public class PayPalPaymentProvider implements PaymentProvider {

    @Override
    public String processPayment(Payment payment) {
        System.out.println("Processing payment with PayPal for invoice: " + payment.getInvoiceId() + " amount: $" + payment.getAmount());
        return "SUCCESS";
    }
}