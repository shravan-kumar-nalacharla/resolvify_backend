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
        User user = new User(request.getUsername(), request.getPassword());
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(new UserDto(savedUser.getId(), savedUser.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());
        if (optionalUser.isPresent() && optionalUser.get().getPassword().equals(request.getPassword())) {
            User user = optionalUser.get();
            return ResponseEntity.ok(new UserDto(user.getId(), user.getUsername()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
