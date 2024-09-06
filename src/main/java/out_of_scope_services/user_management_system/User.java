package out_of_scope_services.user_management_system;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "\"USER\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tenantId;

    private String name;

    private UserRole role;

    @ElementCollection
    private List<Long> studentIds;

    public User() {}

    public User(Long tenantId, String name, UserRole role, List<Long> studentIds) {
        this.tenantId = tenantId;
        this.studentIds = studentIds;
        this.name = name;
        this.role = role;
    }
}
