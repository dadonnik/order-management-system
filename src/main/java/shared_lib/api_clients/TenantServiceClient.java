package shared_lib.api_clients;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import out_of_scope_services.tenant_management_system.Tenant;

import java.util.concurrent.CompletableFuture;

@Service
public class TenantServiceClient {

    private final RestTemplate restTemplate;

    public TenantServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async
    public CompletableFuture<Tenant> getTenantById(Long tenantId) {
        String url = "http://localhost:8080/api/tenants/" + tenantId;
        Tenant tenant = restTemplate.getForObject(url, Tenant.class);
        return CompletableFuture.completedFuture(tenant);
    }
}