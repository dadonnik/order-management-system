package out_of_scope_services.order_management_system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import shared_lib.models.Money;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Money money;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    public OrderItem() {
    }

    public OrderItem(String name, Money money) {
        this.name = name;
        this.money = money;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Money getPrice() {
        return money;
    }

    public void setPrice(Money money) {
        this.money = money;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}

