package shared_lib.api_clients;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import out_of_scope_services.student_management_system.Student;

import java.util.concurrent.CompletableFuture;

@Service
public class StudentServiceClient {

    private final RestTemplate restTemplate;

    public StudentServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async
    public CompletableFuture<Student> getStudentById(Long studentId) {
        String url = "http://localhost:8080/api/students/" + studentId;
        Student student = restTemplate.getForObject(url, Student.class);
        return CompletableFuture.completedFuture(student);
    }
}