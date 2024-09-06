package out_of_scope_services.payment_systems.payment_system;

import org.springframework.stereotype.Service;
import out_of_scope_services.payment_systems.invoicing_system.InvoiceResponse;
import out_of_scope_services.payment_systems.payment_provider.PaymentProvider;
import out_of_scope_services.payment_systems.payment_provider.PaymentProviderFactory;
import shared_lib.InvoiceServiceClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final InvoiceServiceClient invoiceServiceClient;
    private final PaymentProviderFactory paymentProviderFactory;
    private final PaymentRepository paymentRepository;
    private Long paymentCounter = 1L;

    public PaymentServiceImpl(InvoiceServiceClient invoiceServiceClient, PaymentProviderFactory paymentProviderFactory, PaymentRepository paymentRepository) {
        this.invoiceServiceClient = invoiceServiceClient;
        this.paymentProviderFactory = paymentProviderFactory;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment initializePayment(Long invoiceId) {
        Payment existingPayment = paymentRepository.findByInvoiceId(invoiceId);
        if (existingPayment != null) {
            return existingPayment;
        }

        InvoiceResponse invoiceResponse = invoiceServiceClient.getInvoiceById(invoiceId);

        Payment payment = new Payment(invoiceResponse.getId(), invoiceResponse.getTotalAmount(), "STRIPE");
        payment = paymentRepository.save(payment);

        PaymentProvider provider = paymentProviderFactory.getProvider(payment.getPaymentMethod());
        String paymentStatus = provider.processPayment(payment);

        payment.setStatus(PaymentStatus.valueOf(paymentStatus));
        return paymentRepository.save(payment);
    }
}
