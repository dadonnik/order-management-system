package out_of_scope_services.payment_systems.invoicing_system;

import java.util.List;

public interface InvoiceService {
    Invoice createInvoice(Long orderId, List<Long> selectedItems);

    Invoice getInvoice(Long invoiceId);
}