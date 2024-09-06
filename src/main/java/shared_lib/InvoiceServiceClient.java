package shared_lib;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import out_of_scope_services.payment_systems.invoicing_system.InvoiceResponse;

@Service
public class InvoiceServiceClient {

    private final RestTemplate restTemplate;

    public InvoiceServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Method to get the invoice details by calling the Invoice Service
    public InvoiceResponse getInvoiceById(Long invoiceId) {
        String url = "http://localhost:8080/api/invoices/" + invoiceId;
        return restTemplate.getForObject(url, InvoiceResponse.class);
    }
}