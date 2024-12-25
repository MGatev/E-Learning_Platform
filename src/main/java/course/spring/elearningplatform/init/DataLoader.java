package course.spring.elearningplatform.init;

import course.spring.elearningplatform.entity.Role;
import course.spring.elearningplatform.entity.User;
import course.spring.elearningplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public DataLoader(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;;
    }

    @Override
    public void run(String... args) throws Exception {
        List<User> admins = userRepository.findByRole(Role.ADMIN.getDescription());

        if (admins == null || admins.isEmpty()) {
            User admin = new User("admin", passwordEncoder.encode("admin"),
                    "Admin", "Admin", "admin@mail.com", Set.of(Role.ADMIN.getDescription()));
            userRepository.save(admin);
        }
    }
}