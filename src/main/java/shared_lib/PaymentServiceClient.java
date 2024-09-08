package shared_lib;

import com.example.payment_system.Payment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentServiceClient {

    private final RestTemplate restTemplate;

    public PaymentServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Payment getPaidPaymentByInvoiceId(Long invoiceId) {
        String url = "http://localhost:8080/api/payments/" + invoiceId;
        return restTemplate.getForObject(url, Payment.class);
    }
}