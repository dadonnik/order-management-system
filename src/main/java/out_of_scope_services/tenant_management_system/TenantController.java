package out_of_scope_services.tenant_management_system;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TenantController {
    private final TenantService tenantService;

    TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @GetMapping("/tenants/{id}")
    public Tenant getTenant(@PathVariable Long id) {
        return tenantService.getTenantById(id);
    }
}
