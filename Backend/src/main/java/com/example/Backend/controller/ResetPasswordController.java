package com.example.Backend.controller;

import com.example.Backend.service.AuthService;
import com.example.Backend.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    public Map<String, String> sendOtp(@RequestParam String email) {
        authService.sendOtp(email); // envoi asynchrone
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "OTP envoyé");
        return response;
    }


    /**
     * Vérification de l'OTP saisi par l'utilisateur
     * Ex: POST /auth/password/verify-otp?email=test@example.com&otp=123456
     */
    @PostMapping("/verify-otp")
    public boolean verifyOtp(@RequestParam String email, @RequestParam String otp) {
        return otpService.validateOtp(email, otp,false);
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
