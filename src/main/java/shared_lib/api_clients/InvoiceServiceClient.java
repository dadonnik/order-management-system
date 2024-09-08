package shared_lib.api_clients;

import com.example.invoicing_system.Invoice;
import com.example.invoicing_system.InvoiceStatus;
import com.example.invoicing_system.UpdateInvoiceRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class InvoiceServiceClient {

    private final RestTemplate restTemplate;

    public InvoiceServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Invoice getInvoiceById(Long invoiceId) {
        String url = "http://localhost:8080/api/invoices/" + invoiceId;
        return restTemplate.getForObject(url, Invoice.class);
    }

    public Invoice updateInvoiceStatus(Long invoiceId, InvoiceStatus status) {
        String url = "http://localhost:8080/api/invoices/update";
        return restTemplate.postForObject(url, new UpdateInvoiceRequest(invoiceId, status), Invoice.class);
    }
}