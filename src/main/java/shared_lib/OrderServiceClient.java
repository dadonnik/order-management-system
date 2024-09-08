package shared_lib;

import com.example.order_management_system.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderServiceClient {

    private final RestTemplate restTemplate;

    public OrderServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Order getOrderById(Long orderId) {
        String url = "http://localhost:8080/api/orders/" + orderId;
        return restTemplate.getForObject(url, Order.class);
    }
}