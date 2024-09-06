package out_of_scope_services.payment_systems.invoicing_system;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    @OneToMany
    private List<OrderItem> items;

    private double totalAmount;

    public Invoice(Long orderId, List<OrderItem> items) {
        this.orderId = orderId;
        this.items = items;
        this.totalAmount = calculateTotalAmount(items);
    }

    public Invoice() {

    }

    private double calculateTotalAmount(List<OrderItem> items) {
        return items.stream().mapToDouble(OrderItem::getPrice).sum();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}