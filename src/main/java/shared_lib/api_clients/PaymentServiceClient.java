package shared_lib.api_clients;

import com.example.payment_system.model.Payment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class PaymentServiceClient {

    private final RestTemplate restTemplate;

    public PaymentServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async
    public CompletableFuture<Payment> getPaidPaymentByInvoiceId(Long invoiceId) {
        String url = "http://localhost:8080/api/payments/" + invoiceId;
        Payment payment = restTemplate.getForObject(url, Payment.class);
        return CompletableFuture.completedFuture(payment);
    }
}