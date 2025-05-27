package org.example.libraryspringboot.configuration;

import org.example.libraryspringboot.entity.User;
import org.example.libraryspringboot.repository.UserRepository;
import org.example.libraryspringboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Autowired
    UserRepository userRepository;

    @Bean
    public CommandLineRunner createAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder, UserService userService) {
        return args -> {
            if (userRepository.findUserByRole(User.Role.ADMIN) == null) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRole(User.Role.ADMIN);
                admin.setBlocked(false);
                userRepository.save(admin);
                System.out.println("✔ Администратор создан: admin/admin");
            } else System.out.println("Admin is already crteated");
        };
    }
}
