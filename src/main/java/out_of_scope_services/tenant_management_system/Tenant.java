package out_of_scope_services.tenant_management_system;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String logoUrl;

    public Tenant() {
    }

    public Tenant(String name, String address, String logoUrl) {
        this.name = name;
        this.address = address;
        this.logoUrl = logoUrl;
    }
}
