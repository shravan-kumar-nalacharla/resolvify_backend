package com.cms.controller;

import com.cms.dto.LoginRequest;
import com.cms.dto.UserDto;
import com.cms.entity.User;
import com.cms.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

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
        String username = request.getUsername();
        String password = request.getPassword();

        // PURE HARDCODED ADMIN — zero database dependency
        // Works even if H2 is empty, reset, or not seeded
        if ("123".equals(username) && "123".equals(password)) {
            Map<String, Object> adminResponse = new HashMap<>();
            adminResponse.put("id", 0L);
            adminResponse.put("username", "123");
            adminResponse.put("role", "ADMIN");
            return ResponseEntity.ok(adminResponse);
        }

        // Normal users — check database
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(password)) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        User user = userOpt.get();
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("role", user.getRole() != null ? user.getRole() : "USER");
        return ResponseEntity.ok(response);
    }
}
