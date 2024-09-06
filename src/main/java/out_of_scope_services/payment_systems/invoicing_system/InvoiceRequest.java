package out_of_scope_services.payment_systems.invoicing_system;

import java.util.List;

public class InvoiceRequest {
    private Long orderId;
    private List<Long> selectedItems;

    public InvoiceRequest(Long orderId, List<Long> selectedItems) {
        this.orderId = orderId;
        this.selectedItems = selectedItems;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<Long> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<Long> selectedItems) {
        this.selectedItems = selectedItems;
    }
}
