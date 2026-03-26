package com.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import com.cms.entity.User;
import com.cms.repository.UserRepository;

@SpringBootApplication
public class ComplaintBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComplaintBackendApplication.class, args);
	}

	    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            User admin = userRepository.findByUsername("123").orElse(new User("123", "123", "ADMIN"));
            admin.setRole("ADMIN");
            userRepository.save(admin);
            System.out.println("Ensured user '123' has persistent ADMIN rights");
        };
    }
}
