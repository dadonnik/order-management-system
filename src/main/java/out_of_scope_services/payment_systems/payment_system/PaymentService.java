package out_of_scope_services.payment_systems.payment_system;

public interface PaymentService {
    Payment initializePayment(Long invoiceId);
}