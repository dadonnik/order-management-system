package com.example.order_management_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.example.order_management_system",
		"out_of_scope_services",
		"shared_lib"
})
@EntityScan(basePackages = "out_of_scope_services")  // Ensure the entity package is scanned
@EnableJpaRepositories(basePackages = "out_of_scope_services")
public class OrderManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderManagementSystemApplication.class, args);
	}

}
