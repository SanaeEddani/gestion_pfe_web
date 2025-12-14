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
    private UtilisateurRepository utilisateurRepository;

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
        user.setEmail("test@test.com");
        user.setPasswordHash("hashedPwd");

        Role role = new Role();
        role.setName("USER");
        user.setRole(role);
    }

    @Test
    void testLoginSuccess() {
        when(utilisateurRepository.findByEmail("hajar.elmhedden2133@gmail.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123", "$2a$10$3IOjQAI6gBa0.iFds6k7cevTSALk2ZYIynMoGxcfSG4.v06PD.bya")).thenReturn(true);
        when(jwtUtil.generateToken("hajar.elmhedden2133@gmail.com", "encadrant"))
                .thenReturn("jwt-token");

        String token = authService.login("hajar.elmhedden2133@gmail.com", "123");

        assertNotNull(token);
        assertEquals("jwt-token", token);
    }

    @Test
    void testLoginWrongPassword() {
        when(utilisateurRepository.findByEmail("hajar.elmhedden2133@gmail.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hashedPwd")).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                authService.login("hajar.elmhedden2133@gmail.com", "wrong"));

        assertEquals("Mot de passe incorrect", ex.getMessage());
    }

    @Test
    void testResetPasswordSuccess() {
        when(otpService.validateOtp("hajar.elmhedden2133@gmail.com", "123456", true))
                .thenReturn(true);
        when(utilisateurRepository.findByEmail("hajar.elmhedden2133@gmail.com"))
                .thenReturn(Optional.of(user));

        authService.resetPasswordWithOtp("hajar.elmhedden2133@gmail.com", "123456", "newpass");
        verify(utilisateurRepository).save(any(Utilisateur.class));
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
