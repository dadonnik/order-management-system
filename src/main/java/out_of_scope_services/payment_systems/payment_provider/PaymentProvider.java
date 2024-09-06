package out_of_scope_services.payment_systems.payment_provider;

import out_of_scope_services.payment_systems.payment_system.Payment;

public interface PaymentProvider {
    String processPayment(Payment payment);
}