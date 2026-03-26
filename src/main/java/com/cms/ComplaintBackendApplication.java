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
            if (!userRepository.findByUsername("123").isPresent()) {
                userRepository.save(new User("123", "123", "ADMIN"));
                System.out.println("Default admin user '123' created with password '123'");
            }
        };
    }
}
