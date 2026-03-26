package com.cms.controller;

import com.cms.dto.LoginRequest;
import com.cms.dto.UserDto;
import com.cms.entity.User;
import com.cms.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }
        User user = new User(request.getUsername(), request.getPassword(), "USER");
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(new UserDto(savedUser.getId(), savedUser.getUsername(), savedUser.getRole()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // HARDCODED ADMIN CHECK — works even if H2 resets
        if ("123".equals(request.getUsername()) && "123".equals(request.getPassword())) {
            
            // Find or create admin user in current H2 session
            User admin = userRepository.findByUsername("123")
                .orElseGet(() -> {
                    User newAdmin = new User();
                    newAdmin.setUsername("123");
                    newAdmin.setPassword("123");
                    newAdmin.setRole("ADMIN");
                    return userRepository.save(newAdmin);
                });
            
            admin.setRole("ADMIN");
            userRepository.save(admin);
            
            UserDto dto = new UserDto(admin.getId(), admin.getUsername(), "ADMIN");
            return ResponseEntity.ok(dto);
        }
        
        // Normal user login flow
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        
        if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        
        User user = userOpt.get();
        String role = user.getRole() != null ? user.getRole() : "USER";
        UserDto dto = new UserDto(user.getId(), user.getUsername(), role);
        
        return ResponseEntity.ok(dto);
    }
}
