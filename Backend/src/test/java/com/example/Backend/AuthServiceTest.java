package com.example.Backend;

import com.example.Backend.model.Role;
import com.example.Backend.model.Utilisateur;
import com.example.Backend.repository.UtilisateurRepository;
import com.example.Backend.security.JwtUtil;
import com.example.Backend.service.AuthService;
import com.example.Backend.service.OtpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UtilisateurRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private OtpService otpService;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private AuthService authService;

    private Utilisateur user;

    @BeforeEach
    void setup() {
        user = new Utilisateur();
        user.setEmail("hajar.elmhedden2133@gmail.com");
        user.setPasswordHash("$2a$10$BXSVrmK2BunKwBJ62sUuIuEYfGk4oKUDZQMAlugDtQ9y4V9YWx9he");

        Role role = new Role();
        role.setName("encadrant");
        user.setRole(role);
    }

    @Test
    void testLoginSuccess() {
        when(userRepository.findByEmail("hajar.elmhedden2133@gmail.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("HAJARhajar@123", "$2a$10$BXSVrmK2BunKwBJ62sUuIuEYfGk4oKUDZQMAlugDtQ9y4V9YWx9he")).thenReturn(true);
        when(jwtUtil.generateToken("hajar.elmhedden2133@gmail.com", "encadrant"))
                .thenReturn("jwt-token");

        String token = authService.login("hajar.elmhedden2133@gmail.com", "HAJARhajar@123");

        assertNotNull(token);
        assertEquals("jwt-token", token);
    }

    @Test
    void testLoginWrongPassword() {
        when(userRepository.findByEmail("hajar.elmhedden2133@gmail.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "$2a$10$BXSVrmK2BunKwBJ62sUuIuEYfGk4oKUDZQMAlugDtQ9y4V9YWx9he")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                authService.login("hajar.elmhedden2133@gmail.com", "wrong"));

        assertEquals("Mot de passe incorrect", ex.getMessage());
    }

    @Test
    void testResetPasswordSuccess() {
        when(otpService.validateOtp("hajar.elmhedden2133@gmail.com", "123456", true))
                .thenReturn(true);
        when(userRepository.findByEmail("hajar.elmhedden2133@gmail.com"))
                .thenReturn(Optional.of(user));

        authService.resetPasswordWithOtp("hajar.elmhedden2133@gmail.com", "123456", "newpass");
        verify(userRepository).save(any(Utilisateur.class));

    }

    @Test
    void testResetPasswordWrongOtp() {
        when(otpService.validateOtp("test@test.com", "000000", true))
                .thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                authService.resetPasswordWithOtp("test@test.com", "000000", "newpass"));

        assertEquals("OTP invalide ou expiré", ex.getMessage());
    }

}