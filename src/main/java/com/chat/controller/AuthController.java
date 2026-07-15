package com.chat.controller;
import jakarta.validation.Valid;
import com.chat.dto.LoginRequest;
import com.chat.dto.LoginResponse;
import com.chat.model.User;
import com.chat.repository.UserRepository;
import com.chat.util.JwtUtil;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@RestController
@RequestMapping("/auth")

public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(
            UserRepository userRepository,
            JwtUtil jwtUtil,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request
    ) {

        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("User Not Found"));

        boolean validPassword =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword()
                );

        if (!validPassword) {
            throw new RuntimeException(
                    "Invalid Credentials");
        }

        String token =
                jwtUtil.generateToken(user.getUsername());

        return new LoginResponse(
                user.getId(),
                user.getUsername(),
                token
        );
    }
    
    @PostMapping("/register")
    public User register(
            @Valid @RequestBody User user
    ) {
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        return userRepository.save(user);
    }
    
    @GetMapping("/validate")
    public String validate(
            @RequestParam String token
    ) {

        if (jwtUtil.validateToken(token)) {

            return jwtUtil.extractUsername(token);
        }

        return "Invalid Token";
    }
    
}