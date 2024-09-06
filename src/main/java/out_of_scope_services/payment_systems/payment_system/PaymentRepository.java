package out_of_scope_services.payment_systems.payment_system;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByInvoiceId(Long invoiceId);
}