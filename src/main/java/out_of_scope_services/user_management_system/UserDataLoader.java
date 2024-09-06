package out_of_scope_services.user_management_system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UserDataLoader {

    @Bean
    CommandLineRunner loadUserData(UserRepository userRepository) {
        return _ -> {
            User user1 = new User(1L, "John Doe", UserRole.PARENT, List.of(1L, 2L));
            User user2 = new User(1L, "Jane Doe", UserRole.PARENT, List.of(3L, 4L));
            User user3 = new User(2L, "Alice", UserRole.PARENT, List.of(5L, 6L));
            User user4 = new User(2L, "Bob", UserRole.PARENT, List.of(7L, 8L));
            userRepository.saveAll(List.of(user1, user2, user3, user4));

            System.out.println("Sample parents inserted into the database.");
        };
    }
}
