package out_of_scope_services.student_management_system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class StudentDataLoader {
    @Bean
    CommandLineRunner loadStudentData(StudentRepository studentRepository) {
        return _ -> {
            Student student1 = new Student(1L, List.of(1L, 2L), "Carl Doe", 10, "https://someurl.com");
            Student student2 = new Student(1L, List.of(1L, 2L), "Martha Doe", 8, "https://someurl.com");
            Student student3 = new Student(2L, List.of(3L), "Alice's kid 1", 4, "https://someurl.com");
            Student student4 = new Student(2L, List.of(3L), "Alice's kid 2", 6, "https://someurl.com");
            Student student5 = new Student(2L, List.of(4L), "Bob's kid 1", 5, "https://someurl.com");
            Student student6 = new Student(2L, List.of(4L), "Bob's kid 2", 7, "https://someurl.com");
            studentRepository.saveAll(List.of(student1, student2, student3, student4, student5, student6));

            System.out.println("Sample students inserted into the database.");
        };
    }
}
