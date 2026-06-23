package com.library.user_service;

import com.library.user_service.entity.User;
import com.library.user_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

	@Bean
	CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			// 1. Fetch the user if they exist
			userRepository.findByUsername("admin").ifPresentOrElse(
					admin -> {
						// 2. Check if the password is NOT a BCrypt hash (BCrypt hashes always start with $2a$)
						if (!admin.getPassword().startsWith("$2a$")) {
							admin.setPassword(passwordEncoder.encode("password"));
							userRepository.save(admin);
							System.out.println("Security Update: Fixed existing admin password to use BCrypt!");
						}
					},
					() -> {
						// 3. If they don't exist at all, create them from scratch
						User newAdmin = User.builder()
								.username("admin")
								.password(passwordEncoder.encode("password"))
								.role("ROLE_ADMIN")
								.build();
						userRepository.save(newAdmin);
						System.out.println("Default user created: admin / password");
					}
			);
		};
	}
}