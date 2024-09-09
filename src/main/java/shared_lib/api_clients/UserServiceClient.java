package shared_lib.api_clients;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import out_of_scope_services.user_management_system.User;

import java.util.concurrent.CompletableFuture;

@Service
public class UserServiceClient {

    private final RestTemplate restTemplate;

    public UserServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async
    public CompletableFuture<User> getUserById(Long userId) {
        String url = "http://localhost:8080/api/users/" + userId;
        User user = restTemplate.getForObject(url, User.class);
        return CompletableFuture.completedFuture(user);
    }
}