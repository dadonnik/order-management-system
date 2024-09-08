package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.example",
        "out_of_scope_services",
        "shared_lib"
})
@EntityScan(basePackages = {
        "com.example",
        "out_of_scope_services",
        "shared_lib"
})
@EnableJpaRepositories(basePackages = {
        "com.example",
        "out_of_scope_services",
        "shared_lib"
})
public class OrderManagementSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderManagementSystemApplication.class, args);
    }

}
