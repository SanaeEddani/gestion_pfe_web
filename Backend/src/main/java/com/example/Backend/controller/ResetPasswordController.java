package com.example.Backend.controller;

import com.example.Backend.service.AuthService;
import com.example.Backend.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/password")
@CrossOrigin(origins = "*") // Permet l'accès depuis toutes les origines (utile pour mobile)
public class ResetPasswordController {

    @Autowired
    private AuthService authService;

    @Autowired
    private OtpService otpService;

    /**
     * Envoi de l'OTP à l'email de l'utilisateur
     * Ex: POST /auth/password/send-otp?email=test@example.com
     */
    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam String email) {
        authService.sendOtp(email);
        return "OTP envoyé";
    }

    /**
     * Vérification de l'OTP saisi par l'utilisateur
     * Ex: POST /auth/password/verify-otp?email=test@example.com&otp=123456
     */
    @PostMapping("/verify-otp")
    public boolean verifyOtp(@RequestParam String email, @RequestParam String otp) {
        return otpService.validateOtp(email, otp);
    }

    /**
     * Réinitialisation du mot de passe avec OTP
     * Ex: POST /auth/password/reset-password?email=test@example.com&otp=123456&newPassword=MonNouveauMDP
     */
    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam String email,
            @RequestParam String otp,
            @RequestParam String newPassword) {

        authService.resetPasswordWithOtp(email, otp, newPassword);
        return "Mot de passe réinitialisé avec succès";
    }
}
