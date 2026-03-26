package com.cms.controller;

import com.cms.dto.LoginRequest;
import com.cms.dto.UserDto;
import com.cms.entity.User;
import com.cms.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
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
            return ResponseEntity.badRequest().body("Username already exists");
        }
        User user = new User(request.getUsername(), request.getPassword(), "USER");
        User saved = userRepository.save(user);
        return ResponseEntity.ok(new UserDto(saved.getId(), saved.getUsername(), saved.getRole()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request == null) return ResponseEntity.badRequest().body("Request body is missing");
        
        String username = request.getUsername() != null ? request.getUsername().trim() : "";
        String password = request.getPassword() != null ? request.getPassword().trim() : "";

        System.out.println("Login attempt: username='" + username + "'");

        // SCRATCH REBUILD: Hardcoded Admin bypass
        if ("123".equals(username) && "123".equals(password)) {
            Map<String, Object> admin = new HashMap<>();
            admin.put("id", 1L);
            admin.put("username", "123");
            admin.put("role", "ADMIN");
            return ResponseEntity.ok(admin);
        }

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            User user = userOpt.get();
            return ResponseEntity.ok(new UserDto(user.getId(), user.getUsername(), user.getRole()));
        }

        return ResponseEntity.status(401).body("Invalid credentials");
    }
}
