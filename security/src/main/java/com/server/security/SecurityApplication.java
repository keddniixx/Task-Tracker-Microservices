package com.server.security;

import com.server.security.model.User;
import com.server.security.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

    @Bean
    public CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                userRepository.save(User.builder().username("User1").password(passwordEncoder.encode("password123")).role("ROLE_USER").build());
                userRepository.save(User.builder().username("User2").password(passwordEncoder.encode("password123")).role("ROLE_USER").build());
                userRepository.save(User.builder().username("User3").password(passwordEncoder.encode("password123")).role("ROLE_USER").build());
            }
        };
    }
}
