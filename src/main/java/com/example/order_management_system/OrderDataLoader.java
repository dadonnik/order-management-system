package com.example.order_management_system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.List;

@Configuration
public class OrderDataLoader {

    @Bean
    CommandLineRunner loadOrderData(OrderRepository orderRepository) {
        return _ -> {
            List<OrderItem> items = List.of(
                    new OrderItem("Tuition Fee", 500),
                    new OrderItem("Exam Fee", 150),
                    new OrderItem("Transport Fee", 100),
                    new OrderItem("Books", 200),
                    new OrderItem("Uniforms", 250)
            );

            Order order1 = new Order(
                    1L,
                    1L,
                    1L,
                    new Date(),
                    items
            );
            orderRepository.saveAll(List.of(order1));

            System.out.println("Sample orders inserted into the database.");
        };
    }
}
