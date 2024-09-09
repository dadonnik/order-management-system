package shared_lib.api_clients;

import com.example.invoicing_system.dto.UpdateInvoiceRequest;
import com.example.invoicing_system.model.Invoice;
import com.example.invoicing_system.model.InvoiceStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class InvoiceServiceClient {

    private final RestTemplate restTemplate;

    public InvoiceServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async
    public CompletableFuture<Invoice> getInvoiceById(Long invoiceId) {
        String url = "http://localhost:8080/api/invoices/" + invoiceId;
        Invoice invoice = restTemplate.getForObject(url, Invoice.class);
        return CompletableFuture.completedFuture(invoice);
    }

    public Invoice updateInvoiceStatus(Long invoiceId, InvoiceStatus status) {
        String url = "http://localhost:8080/api/invoices/update";
        return restTemplate.postForObject(url, new UpdateInvoiceRequest(invoiceId, status), Invoice.class);
    }
}
