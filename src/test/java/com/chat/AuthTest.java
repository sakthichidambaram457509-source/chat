package com.chat;

import com.chat.controller.AuthController;
import com.chat.dto.LoginRequest;
import com.chat.dto.LoginResponse;
import com.chat.model.User;
import com.chat.repository.UserRepository;
import com.chat.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthTest {

    private UserRepository userRepository;
    private JwtUtil jwtUtil;
    private BCryptPasswordEncoder passwordEncoder;
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        jwtUtil = new JwtUtil();
        passwordEncoder = new BCryptPasswordEncoder();
        authController = new AuthController(userRepository, jwtUtil, passwordEncoder);
    }

    @Test
    public void testSuccessfulRegisterAndLogin() {
        // Prepare User
        User rawUser = new User();
        rawUser.setUsername("testuser");
        rawUser.setEmail("test@example.com");
        rawUser.setPassword("password123");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setEmail("test@example.com");
        savedUser.setPassword(passwordEncoder.encode("password123"));

        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(savedUser));

        // Test Registration
        User registered = authController.register(rawUser);
        assertNotNull(registered);
        assertEquals("testuser", registered.getUsername());

        // Test Login
        LoginRequest loginReq = new LoginRequest();
        loginReq.setUsername("testuser");
        loginReq.setPassword("password123");

        LoginResponse response = authController.login(loginReq);
        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertNotNull(response.getToken());
        assertTrue(jwtUtil.validateToken(response.getToken()));
    }

    @Test
    public void testLoginWithInvalidPassword() {
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setPassword(passwordEncoder.encode("correctpassword"));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(savedUser));

        LoginRequest loginReq = new LoginRequest();
        loginReq.setUsername("testuser");
        loginReq.setPassword("wrongpassword");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authController.login(loginReq);
        });

        assertEquals("Invalid Credentials", exception.getMessage());
    }

    @Test
    public void testLoginWithNonExistentUser() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        LoginRequest loginReq = new LoginRequest();
        loginReq.setUsername("unknown");
        loginReq.setPassword("password");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authController.login(loginReq);
        });

        assertEquals("User Not Found", exception.getMessage());
    }
}
