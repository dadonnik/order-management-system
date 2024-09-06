package out_of_scope_services.tenant_management_system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class TenantDataLoader {

    @Bean
    CommandLineRunner loadTenantData(TenantRepository tenantRepository) {
        return _ -> {
            Tenant tenant1 = new Tenant("Horizon International School", "123 Educational Blvd, Dubai, UAE", "https://logo1.com");
            Tenant tenant2 = new Tenant("Tenant 2", "Address 2", "https://logo2.com");
            Tenant tenant3 = new Tenant("Tenant 3", "Address 3", "https://logo3.com");
            tenantRepository.saveAll(List.of(tenant1, tenant2, tenant3));

            System.out.println("Sample tenants inserted into the database.");
        };
    }
}
