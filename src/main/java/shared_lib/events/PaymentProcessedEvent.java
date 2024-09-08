package shared_lib.events;

import com.example.payment_system.PaymentStatus;
import org.springframework.context.ApplicationEvent;

public class PaymentProcessedEvent extends ApplicationEvent {
    private final Long invoiceId;
    private final PaymentStatus status;

    public PaymentProcessedEvent(Object source, Long invoiceId, PaymentStatus status) {
        super(source);
        this.invoiceId = invoiceId;
        this.status = status;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public PaymentStatus getStatus() {
        return status;
    }
}