package shared_lib;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import out_of_scope_services.user_management_system.User;

@Service
public class UserServiceClient {

    private final RestTemplate restTemplate;

    public UserServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public User getUserById(Long userId) {
        String url = "http://localhost:8080/api/users/" + userId;
        return restTemplate.getForObject(url, User.class);
    }
}