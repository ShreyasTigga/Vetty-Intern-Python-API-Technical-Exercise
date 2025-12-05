package com.example.crypto.controller;

import com.example.crypto.config.JwtUtil;
import com.example.crypto.entity.AppUser;
import com.example.crypto.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserRepository userRepo;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepo, JwtUtil jwtUtil) { this.userRepo = userRepo; this.jwtUtil = jwtUtil; }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        var user = userRepo.findByUsername(username).orElse(null);
        if (user == null || !user.getPassword().equals(password)) { // demo only — hash in production
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
        String token = jwtUtil.generateToken(username);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
