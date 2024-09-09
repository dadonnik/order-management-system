package out_of_scope_services.order_management_system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shared_lib.models.Money;

import java.util.List;

@Configuration
public class OrderDataLoader {

    @Bean
    CommandLineRunner loadOrderData(OrderRepository orderRepository) {
        return _ -> {
            List<OrderItem> items = List.of(
                    new OrderItem("Tuition Fee", new Money("50000")),
                    new OrderItem("Exam Fee", new Money("15000")),
                    new OrderItem("Transport Fee", new Money("10000")),
                    new OrderItem("Books", new Money("20000")),
                    new OrderItem("Uniforms", new Money("25000"))
            );

            Order order1 = new Order(
                    1L,
                    1L,
                    1L,
                    items
            );
            orderRepository.saveAll(List.of(order1));

            System.out.println("Sample orders inserted into the database.");
        };
    }
}
