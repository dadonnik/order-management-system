package out_of_scope_services.tenant_management_system;

import org.springframework.stereotype.Service;

@Service
public class TenantServiceImpl implements TenantService {
    private final TenantRepository tenantRepository;
    TenantServiceImpl(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    public Tenant getTenantById(Long tenantId) {
        return tenantRepository.findById(tenantId).orElseThrow(() -> new RuntimeException("Tenant not found"));
    }
}
