package out_of_scope_services.order_management_system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shared_lib.models.Price;

import java.util.List;

@Configuration
public class OrderDataLoader {

    @Bean
    CommandLineRunner loadOrderData(OrderRepository orderRepository) {
        return _ -> {
            List<OrderItem> items = List.of(
                    new OrderItem("Tuition Fee", new Price("50000")),
                    new OrderItem("Exam Fee", new Price("15000")),
                    new OrderItem("Transport Fee", new Price("10000")),
                    new OrderItem("Books", new Price("20000")),
                    new OrderItem("Uniforms", new Price("25000"))
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
